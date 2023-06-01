<template>
  <div>
    <div v-for="message in this.$store.state.alarm.messageList" :key="message.id">
      <div @click="showDetails(message.id)">
        <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200">
          <div class="title">
            <span class="font-weight-bold">{{ message.sender }} </span>
            <span v-if="message.receiveState === 'new'">new!</span>
          </div>
          <div class="patient-info">
            <span>{{ formatDate(message.entryDate) }}</span>
          </div>
        </div>
      </div>
    </div>
    <div v-if="this.$store.state.alarm.messageList === null">
      <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200">
        <span>받은 메시지가 없습니다.</span>
      </div>
    </div>
    <div>
      <b-modal v-model="this.$store.state.alarm.messageModal" size="sm" title="쪽지 내용" @hidden="closeModal">
        <div>
          <h2>
            {{ this.$store.state.alarm.selectedMessage.sender }}
            <button type="button" class="btn btn-primary btn-sm">
              답장
            </button>
          </h2>
        </div>
        <div v-html="this.$store.state.alarm.selectedMessage.content"></div>
      </b-modal>
    </div>
  </div>
</template>

<script>
import {mapMutations, mapState} from "vuex";
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import axios from "axios";

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
      selectedMessage: "",
      showDetailsModal: false,
      readMessage: "",
    }
  },
  name: "ReceiveMessage",
  created() {
    this.recvList = this.messageList;
    this.count = this.messageCount;
    this.getSessionLogIn();
    // App.vue가 생성되면 소켓 연결을 시도합니다.
  },
  mounted() {
    this.settingRecvList();
  },
  computed: {
    ...mapState('alarm',
        ['messageList', 'messageCount', 'messageModal', 'selectedMessage']
    ),
  },
  methods: {
    ...mapMutations('alarm', {
      setMessage: 'setMessage',
      setCount: 'setCount',
      setMessageModal: 'setMessageModal',
      setSelectedMessage: 'setSelectedMessage'
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
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            console.log(response.data);
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              console.log('현재 로그인된 사용자: ' + logIn.name);
              this.sender = logIn.name;
              console.log(this.recvList)
            } else {
              console.log('로그인되어 있지 않습니다.');
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    settingRecvList() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/receiveMessageList')
          .then(response => {
            console.log(response.data);
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              this.isLogin = true
              let receiveList = JSON.parse(JSON.stringify(response.data.receiveList));
              console.log(receiveList)
              this.recvList = receiveList
              this.setMessage(this.recvList)
              this.alarmLength()
              console.log(this.recvList)
            } else {
              console.log('로그인되어 있지 않습니다.');
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
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect({
            'client-id': this.sender
          },
          () => {
            // 소켓 연결 성공
            this.connected = true;
            console.log('소켓 연결 성공');
            // 서버의 메시지 전송 endpoint를 구독합니다.
            // 이런형태를 pub sub 구조라고 합니다.
            this.stompClient.subscribe("/send/" + this.sender, res => {
              console.log('구독으로 받은 메시지 입니다.', res.body)
              // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
              this.recvList = this.messageList
              this.recvList.push(JSON.parse(res.body))
              this.setMessage(this.recvList)
              this.setCount(this.count)
            });
          },
          (error) => {
            // 소켓 연결 실패
            console.log('소켓 연결 실패', error)
            this.connected = false;
          }
      );
    },
    alarmLength() {
      if (this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.state).length
      }
      this.setCount(this.count)
    },
    showDetails(messageId) {
      this.showDetailsModal = true;
      axios.get('api/receiveMessage', {
        params: {
          id: messageId
        }
      }).then(response => {
        if (response.status === 200) {
          this.readMessage = response.data.receiveMessage
          this.setSelectedMessage(this.readMessage)
        } else {
          console.log('메시지 불러오기 실패')
        }
      })
      this.setMessageModal(this.showDetailsModal);
      setTimeout(() => this.readOn(this.readMessage), 100)
      this.readMessage = "";
    },
    readOn(message) {
      this.connect()
      setTimeout(() => this.read(message), 100)
    },
    read(message) {
      console.log("read message:" + message.id);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          connectType: "read",
          id: message.id
        };
        this.stompClient.send("/app/receive/" + message.sender, JSON.stringify(msg), {});
      }
      console.log("전송 취소 요청 완료. 소켓 연결 해제")
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.messageContent = ''
      setTimeout(() => this.settingRecvList(), 100)
    },
    closeModal() {
      this.showDetailsModal = false;
      this.setMessageModal(this.showDetailsModal);
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