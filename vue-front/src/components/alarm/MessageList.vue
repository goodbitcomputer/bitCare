<template>
  <div>
    <div class="mt-3">
      <table class="table message-table">
        <thead>
        <tr>
          <th>발신자</th>
          <th></th>
          <th>수신 일자</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="message in this.$store.state.alarm.messageList" :key="message.id">
          <td>{{ message.sender }}</td>
          <td>{{ formatState(message.receiveState) }}</td>
          <td>{{ formatDate(message.entryDate) }}</td>
          <td>
            <div>
              <button type="button" @click="showDetails(message.id)">
                자세히 보기
              </button>
              <button type="button" class="btn btn-danger btn-sm" @click="deleteMessage(message)">
                삭제
              </button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div>
      <b-modal v-model="this.$store.state.alarm.messageModal" size="lg" title="쪽지 내용" @hidden="closeModal">
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
      ],
      readMessage: "",
    }
  },
  name: "MessageList",
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
    formatState(stateString){
      if(stateString === 'new'){
        return 'new!'
      }else{
        return ''
      }
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
    sendMessage(e) {
      if (e.keyCode === 13 && this.sender !== '' && this.message !== '') {
        this.connect()
        setTimeout(() => this.send(), 100)
      }
    },
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        if (this.sender != null) {
          this.type = "message";
        } else {
          this.type = "announcement";
        }
        const msg = {
          receiver: this.receiver,
          sender: this.sender,
          content: this.message,
          state: this.state,
          messageFile: this.message_file,
          type: this.type,
          entryDate: Date.now()
        };
        this.stompClient.send("/app/receive/" + this.receiver, JSON.stringify(msg), {});
      }
      console.log("메세지 전송 완료. 소켓 연결 해제")
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.message = ''
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
    /* DB 데이터 가져오기 */
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
    deleteMessage(message) {
      // API를 호출해서 해당 메시지를 삭제합니다.
      // 성공적으로 삭제되면 this.settingRecvList()를 호출합니다.
      axios.get('api/deleteReceiveMessage', {
        params: {
          id: message.id
        }
      }).then(response => {
        if (response.status === 200) {
          console.log('삭제 성공')
        } else {
          console.log('삭제 실패')
        }
      })

      setTimeout(() => this.settingRecvList(), 100)
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