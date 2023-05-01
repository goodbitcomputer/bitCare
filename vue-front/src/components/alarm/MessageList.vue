<template>
  <div>
    <h1>
    유저이름: {{receiver}}
    </h1>

    받는이: <input
      v-model="sender"
      type="text">

    내용: <input
      v-model="message"
      type="text"
      @keyup="sendMessage">

    <div
        v-for="(item, idx) in recvList"
        :key="idx">

      <h3>{{item.sender}} to {{ item.receiver }}</h3>
      <h3>내용: {{ item.content }}</h3>
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
      recvList: this.messageList,
      receiver: "",
      message: "",
      sender: "",
      state: "new",
      message_file: ""
    }
  },
  name: "MessageList",
  created() {
    this.recvList = this.messageList;
    this.getSessionLogIn();
    // App.vue가 생성되면 소켓 연결을 시도합니다.
    this.connect();
  },
  computed: {
    ...mapState('alarm',
        ['messageList']
    ),
  },
  methods: {
    ...mapMutations('alarm', {
      setMessage: 'setMessage',
    }),
    getSessionLogIn(){
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            console.log(response.data);
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              console.log('현재 로그인된 사용자: ' + logIn.name);
              this.receiver = logIn.name;
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
      if (e.keyCode === 13 && this.receiver !== '' && this.message !== '') {
        this.send()
        this.message = ''
      }
    },
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          sender: this.sender,
          receiver: this.receiver,
          content: this.message,
          state: this.state,
          messageFile: this.message_file
        };
        this.stompClient.send("/app/receive/"+this.sender, JSON.stringify(msg), {});
      }
    },
    connect() {
      const serverURL = "http://localhost:8080/receive"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect({
            'client-id': this.receiver
          },
          () => {
            // 소켓 연결 성공
            this.connected = true;
            console.log('소켓 연결 성공');
            // 서버의 메시지 전송 endpoint를 구독합니다.
            // 이런형태를 pub sub 구조라고 합니다.
            this.stompClient.subscribe("/send/"+this.receiver, res => {
              console.log('구독으로 받은 메시지 입니다.', res.body);
              // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
              this.recvList.push(JSON.parse(res.body))
              this.setMessage(this.recvList)
            });
          },
          (error) => {
            // 소켓 연결 실패
            console.log('소켓 연결 실패', error);
            this.connected = false;
          }
      );
    },
    disconnect(){
      this.stompClient.disconnect();
    }
  }
}
</script>

<style scoped>

</style>