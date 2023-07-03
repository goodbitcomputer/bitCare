<template>
  <div>
    <b-button v-b-modal.chat-bot class="">
      <b-icon icon="plus-circle" variant="white"></b-icon>
      챗봇
    </b-button>
    <b-modal id="chat-bot" size="lg" scrollable centered title="Bitcare ChatBot" button-size="sm">
      <!--    <div id="main-content" class="container">-->
      <!--      <div class="row">-->
      <!--        <div class="col-md-6">-->
      <!--          <div class="form-group">-->
      <!--            <label for="connect">웹소켓 연결:</label>-->
      <!--            <button id="connect" class="btn btn-default" @click="connect">연결</button>-->
      <!--            <button id="disconnect" class="btn btn-default" @click="disconnect">-->
      <!--              해제-->
      <!--            </button>-->
      <!--          </div>-->
      <!--        </div>-->
      <!--        <div class="col-md-6">-->
      <!--          <div class="form-group">-->
      <!--            <label for="msg">문의사항</label>-->
      <!--            <input type="text" id="msg" class="form-control" placeholder="내용을 입력하세요....">-->
      <!--          </div>-->
      <!--          <button id="send" class="btn btn-default" @click="sendMessage">보내기</button>-->
      <!--        </div>-->
      <!--      </div>-->
      <!--      <div class="row">-->
      <!--        <div class="col-md-12">-->
      <!--          <div id="conversation" class="table table-striped">-->
      <!--            <div v-for="item in msgList" :key="item.name">-->
      <!--              <span>{{ item.name }}: </span><span>{{ item.message }}</span>-->
      <!--            </div>-->
      <!--          </div>-->
      <!--        </div>-->
      <!--      </div>-->
      <!--    </div>-->

      <section class="chat">
<!--        <div class="header-chat">-->
<!--          <i class="icon fa fa-user-o" aria-hidden="true"></i>-->
<!--          <p class="name">BIT Bot</p>-->
<!--          <i class="icon clickable fa fa-ellipsis-h right" aria-hidden="true"></i>-->
<!--        </div>-->
        <div class="messages-chat">
          <div v-for="item in msgList" :key="item.name">
            <div class="message" v-if="item.name === 'bit'">
              <div class="photo">
                <img class="photo-img" src="@/assets/img/chat_bit_logo.png">
                <div class="online"></div>
              </div>
              <p class="text"> {{ item.message }}😃 </p>
            </div>
            <div class="message text-only" v-if="item.name === 'my'">
              <div class="response">
                <p class="text"> {{ item.message }} </p>
              </div>
            </div>
          </div>
        </div>
        <form @submit.prevent="sendMessage()">
          <div class="footer-chat">
            <img class="icon clickable" style="font-size:25pt;" aria-hidden="true" src="@/assets/img/smile.png">
            <input type="text" id="msg" class="write-message" v-model="chatMsg" placeholder="내용을 입력하세요..">
            <button class="send"><img class="" src="@/assets/img/send.png"></button>
          </div>
        </form>
      </section>
    </b-modal>
  </div>
</template>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

let stompClient = null;

export default {
  name: "ChatBotBox",
  data() {
    return {
      chatMsg: "",
      msgList: [],
    }
  },
  mounted() {
    setTimeout(() => this.connect(), 100);
  },
  methods: {
    setConnected(connected) {
      document.getElementById('connect').prop("disabled", connected);
      document.getElementById('disconnect').prop("disabled", connected);
      document.getElementById('send').prop("disabled", connected);
      if (connected) {
        document.getElementById('conversation').show;
      } else {
        document.getElementById('conversation').hide;
      }
      document.getElementById('msg').html("");
    },

    connect() {
      let socket = new SockJS('/ws');
      let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
      stompClient = Stomp.over(socket, options);
      stompClient.connect({}, (frame) => {
        // this.setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', (message) => {
          this.showBitMessage(message.body); //서버에 메시지 전달 후 리턴받는 메시지
        });
      });
    },

    disconnect() {
      if (stompClient !== null) {
        stompClient.disconnect();
      }
      // this.setConnected(false);
      console.log("Disconnected");
    },

    sendMessage() {
      let message = document.getElementById('msg').value;
      console.log(message);
      this.showMyMessage(message);

      console.log(JSON.stringify({chatMessage: message}));
      stompClient.send("/app/sendMessage", message, {}); //서버에 보낼 메시지

      this.chatMsg = "";
    },

    showMyMessage(message) {
      // $("#communicate").append("<tr><td>" + message + "</td></tr>");
      // document.getElementById('communicate').append("<tr><td>" + message + "</td></tr>");
      this.msgList.push({
        name: "my",
        message: message,
      })

      this.fixScroll()
      setTimeout(() => this.fixScroll(), 50);
    },
    showBitMessage(message) {
      this.msgList.push({
        name: "bit",
        message: message,
      })

      this.fixScroll();
      setTimeout(() => this.fixScroll(), 50);
    },
    fixScroll() {
      let chatUl = document.querySelector('.messages-chat');
      chatUl.scrollTop = chatUl.scrollHeight; // 스크롤의 위치를 최하단으로

    }
  },
}
</script>

<style scoped>

body {
  background-color: #f5f5f5;
}

#main-content {
  max-width: 940px;
  padding: 2em 3em;
  margin: 0 auto 20px;
  background-color: #fff;
  border: 1px solid #e5e5e5;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  border-radius: 5px;
}

/* chatUi */
.chat {
  /*width: calc(65% - 85px);*/
}

.header-chat {
  background-color: #FFF;
  height: 90px;
  box-shadow: 0px 3px 2px rgba(0, 0, 0, 0.100);
  display: flex;
  align-items: center;
}

.chat .header-chat .icon {
  margin-left: 30px;
  color: #515151;
  font-size: 14pt;
}

.chat .header-chat .name {
  margin: 0 0 0 20px;
  text-transform: uppercase;
  font-family: 'Montserrat', sans-serif;
  font-size: 13pt;
  color: #515151;
}

.chat .header-chat .right {
  position: absolute;
  right: 40px;
}

.chat .messages-chat {
  overflow-y: scroll;
  height: 400px;
  padding: 10px 35px;
}

/* 스크롤바 숨기기 */
.chat .messages-chat::-webkit-scrollbar {
  width: 0;
}

.chat .messages-chat .message {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.chat .messages-chat .message .photo {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 45px;
  height: 45px;
  background: #E6E7ED;
  -moz-border-radius: 50px;
  -webkit-border-radius: 50px;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
}
.chat .messages-chat .message .photo .photo-img {
  width: 35px;
  height: 35px;
  -moz-border-radius: 50px;
  -webkit-border-radius: 50px;
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
}

.chat .messages-chat .text {
  margin: 0 35px;
  background-color: #f6f6f6;
  padding: 7px;
  border-radius: 12px;
}

.text-only {
  margin-left: 45px;
}

.time {
  font-size: 10px;
  color: lightgrey;
  margin-bottom: 10px;
  margin-left: 85px;
}

.response-time {
  float: right;
  margin-right: 40px !important;
}

.response {
  float: right;
  margin-right: 0px !important;
  margin-left: auto; /* flexbox alignment rule */
}

.response .text {
  background-color: #e3effd !important;
}

.footer-chat {
  /*width: calc(65% - 66px);*/
  /*height: 80px;*/
  display: flex;
  align-items: center;
  justify-items: center;
  justify-content: center;
  /*position: absolute;*/
  bottom: 0;
  background-color: transparent;
  border-top: 2px solid #EEE;

}

.chat .footer-chat img {
  /*margin-left: 30px;*/
  color: #C0C0C0;
  width: 40px;
  height: 40px;
}

.chat .footer-chat .send img {
  margin-left: 0px;
  color: #C0C0C0;
  width: 30px;
  height: 30px;
}

.chat .footer-chat .send {
  margin-left: 20px;
  color: #fff;
  background-color: #4f6ebd;
  /*position: absolute;*/
  right: 50px;
  /*padding: 12px 12px 12px 12px;*/
  border-radius: 50px;
  width: 40px;
  height: 40px;
}

.chat .footer-chat .name {
  margin: 0 0 0 20px;
  text-transform: uppercase;
  font-family: 'Montserrat', sans-serif;
  font-size: 13pt;
  color: #515151;
}

.chat .footer-chat .right {
  position: absolute;
  right: 40px;
}

.write-message {
  border: none !important;
  width: 60%;
  height: 50px;
  margin: 0 0 0 20px;
  padding: 10px;
}

.footer-chat *::-webkit-input-placeholder {
  color: #C0C0C0;
  font-size: 13pt;
}

.footer-chat input *:-moz-placeholder {
  color: #C0C0C0;
  font-size: 13pt;
}

.footer-chat input *::-moz-placeholder {
  color: #C0C0C0;
  font-size: 13pt;
  margin-left: 5px;
}

.footer-chat input *:-ms-input-placeholder {
  color: #C0C0C0;
  font-size: 13pt;
}

.clickable {
  cursor: pointer;
}

</style>