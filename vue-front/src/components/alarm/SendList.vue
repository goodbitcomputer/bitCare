<template>
  <div>
    <div class="mt-3">
      <table class="table message-table">
        <thead>
        <tr>
          <th>수신자</th>
          <th>수신 상태</th>
          <th>발신 일자</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="message in this.$store.state.alarm.sendList" :key="message.id">
          <td>{{ message.receiver }}</td>
          <td>{{ formatState(message.receiveState) }}</td>
          <td>{{ formatDate(message.entryDate) }}</td>
          <td>
            <div>
              <button type="button" @click="showDetails(message)">
                자세히 보기
              </button>
            </div>
            <div v-if="message.receiveState === 'new'">
              <button type="button" @click="sendCancel(message)">
                발송 취소
              </button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div>
      <b-modal v-model="showDetailsModal" size="lg" title="쪽지 내용">
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
      selectedMessage: "",
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
    formatState(stateString){
      if(stateString === 'new'){
        return '읽지 않음'
      }else{
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
              if(res.body === 'cancel'){
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
      if(this.recvList != null) {
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
      setTimeout(() => this.cancel(message),100)
    },
    cancel(message){
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          connectType: "cancel",
          id: message.id
        };
        this.stompClient.send("/app/receive/" + message.receiver, JSON.stringify(msg), {});
      }
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.messageContent = ''
      setTimeout(() => this.settingSendList(), 100)
    }
  }
}
</script>

<style>
/* Custom 스타일 */
.message-header {
  font-weight: bold;
  font-size: 1.2rem;
}

.message-table {
  border-collapse: separate;
  border-spacing: 0 10px;
}

.message-modal {
  z-index: 1050;
}

.message-modal-header {
  background-color: #f0f0f0;
  font-weight: bold;
  font-size: 1.2rem;
}
</style>