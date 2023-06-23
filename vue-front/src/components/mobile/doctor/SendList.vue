<template>
  <div>
    <div v-for="message in this.$store.state.alarm.sendList" :key="message.id">
      <div @click="showDetails(message)">
        <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200">
          <div class="title">
            <span class="font-weight-bold">{{ message.receiver }} </span>
            <span>{{ formatState(message.receiveState) }}</span>
          </div>
          <div class="patient-info">
            <span>{{ formatDate(message.entryDate) }}</span>
          </div>
        </div>
      </div>
    </div>
    <div v-if="this.$store.state.alarm.sendList === null">
      <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200">
        <span>보낸 메시지가 없습니다.</span>
      </div>
    </div>
    <div>
      <b-modal v-model="showDetailsModal" size="lg" hide-footer title="쪽지 내용">
        <button v-if="selectedMessage.receiveState === 'new'" type="button" @click="sendCancel(selectedMessage)">
          발송 취소
        </button>
        <div v-html="selectedMessage.content"></div>
      </b-modal>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import axios from "axios";
import {mapState, mapMutations} from "vuex";

export default {
  data() {
    return {
      stompClient: null,
      isLogin: false,
      recvList: [],
      sender: "",
      message: "",
      receiver: "",
      state: "new",
      message_file: "",
      entryDate: "",
      type: "",
      selectedMessage: {},
      showDetailsModal: false,
      fields: [
        {key: 'sender', label: '보낸 사람'},
        {key: 'content', label: '내용'},
        {key: 'entryDate', label: '수신 일자'},
        {key: 'action', label: '작업'}
      ]
    }
  },
  name: "SendList",
  created() {
    this.recvList = this.sendList;
    this.count = this.sendCount;
    this.getSessionLogIn();
    // App.vue가 생성되면 소켓 연결을 시도합니다.
  },
  mounted() {
    this.settingSendList();
  },
  computed: {
    ...mapState('alarm',
        ['sendCount', 'sendList']
    ),
  },
  methods: {
    ...mapMutations('alarm', {
      setSendList: 'setSendList',
      setSendCount: 'setSendCount'
    }),
    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    formatState(stateString) {
      if (stateString === 'new') {
        return '읽지 않음'
      } else {
        return '읽음'
      }
    },
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              this.sender = logIn.name;
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    connect() {
      const serverURL = "http://localhost:8080/receive"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({
            'client-id': this.sender
          },
          () => {
            // 소켓 연결 성공
            this.connected = true;
            // 서버의 메시지 전송 endpoint를 구독합니다.
            // 이런형태를 pub sub 구조라고 합니다.
            this.stompClient.subscribe("/send/" + this.sender, res => {
              // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
              if (res.body === 'cancel') {
                this.settingSendList()
              }
            });
          },
          (error) => {
            // 소켓 연결 실패
            console.log(error)
            this.connected = false;
          }
      );
    },
    /* DB 데이터 가져오기 */
    settingSendList() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/sendMessageList')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              this.isLogin = true
              let sendList = JSON.parse(JSON.stringify(response.data.sendList));
              this.recvList = sendList
              this.setSendList(this.recvList)
              this.sendLength()
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    sendLength() {
      if (this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.state).length
      }
      this.setSendCount(this.count)
    },
    showDetails(message) {
      this.selectedMessage = message;
      this.showDetailsModal = true;
    },
    sendCancel(message) {
      this.connect()
      setTimeout(() => this.cancel(message), 100)
    },
    cancel(message) {
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          connectType: "cancel",
          id: message.id
        };
        this.stompClient.send("/app/receive/" + message.receiver, JSON.stringify(msg), {});
      }
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.messageContent = ''
      this.showDetailsModal = false
      setTimeout(() => this.settingSendList(), 100)
    }
  }
}
</script>

<style scoped>
.patient-box {
  text-align: left;
  background-color: white;
  /*background: rgba(12, 11, 9, 0.7);*/
  height: 80px;
}

/*.patient-info span, p {*/
/*  color: #999999;*/
/*  white-space: nowrap;*/
/*}*/

.patient-info {
  flex-wrap: nowrap;
}

.patient-info span {
  font-size: 15px;
  /*// 글자 줄바꿈 안되게 함.*/
  white-space: nowrap;
}

.patient-info span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 3px 0px 3px;
}

.border-box {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid ;*/
  border-radius: 5px;
}
</style>