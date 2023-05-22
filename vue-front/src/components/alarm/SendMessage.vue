<template>
  <!-- 쪽지 작성 페이지 -->
    <div>
          받는이: <input
            v-model="receiver"
            type="text">
      <vue-editor v-model="messageContent"></vue-editor>
      <button type="button" class="btn btn-primary btn-sm" id="sendButton" @click="sendMessage">Send</button>
    </div>
</template>

<script>
import {VueEditor} from "vue2-editor";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: "SendMessage",
  data() {
    return {
      receiver: "",
      messageContent: "",
      type: "",
      state: "new",
      messageFile: ""
    }
  },
  methods: {
    connect() {
      this.sender = this.$store.state.login.name;
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
          },
          (error) => {
            // 소켓 연결 실패
            console.log('소켓 연결 실패', error)
            this.connected = false;
          }
      );
    },
    sendMessage() {
      if (this.sender !== '' && this.message !== '') {
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
          sender: this.$store.state.login.name,
          content: this.messageContent,
          state: this.state,
          messageFile: this.message_file,
          type: this.type,
          entryDate: Date.now()
        };
        this.stompClient.send("/app/receive/" + this.receiver, JSON.stringify(msg), {});
      }
      console.log("메세지 전송 완료. 소켓 연결 해제")
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.messageContent = ''
    },
  },
  components: {
    VueEditor
  }
}
</script>

<style scoped>
#sendButton {
  font-size: 20px;
  right: -650px;
  position: relative;
}
</style>