<template>
  <!-- 쪽지 작성 페이지 -->
  <div>
    <vue-editor v-model="messageContent" id="edit"></vue-editor>
    <div class="util d-flex">
      <button class="border-box col" @click="sendMessage">공지 쓰기</button>
    </div>
  </div>
</template>

<script>
import {VueEditor} from "vue2-editor";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import {mapMutations, mapState} from "vuex";
import axios from "axios";

export default {
  name: "WriteAnnouncement",
  data() {
    return {
      receiver: "",
      messageContent: "",
      type: "",
      state: "new",
      messageFile: ""
    }
  },
  computed: {
    ...mapState('alarm',
        ['sendCount', 'sendList', 'announcementList'],
    ),
    ...mapState('login',
        ['list']
    )
  },
  methods: {
    ...mapMutations('alarm', {
      setSendList: 'setSendList',
      setSendCount: 'setSendCount',
      setAnnouncementList: 'setAnnouncementList',
    }),
    ...mapMutations('login',{
      setList: 'setList'
    }),
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
      if (this.sender !== '' && this.messageContent !== '') {
        this.connect()
        setTimeout(() => this.send(), 100)
      }
    },
    send() {
      console.log("Send message:" + this.messageContent);
      if (this.stompClient && this.stompClient.connected) {

        this.type = "announcement";

        const msg = {
          connectType: "send",
          sender: this.$store.state.login.name,
          content: this.messageContent,
          receiveState: this.state,
          sendState: this.state,
          messageFile: this.message_file,
          type: this.type,
          entryDate: Date.now()
        };
        this.stompClient.send("/app/receive/admin", JSON.stringify(msg), {});
      }
      console.log("메세지 전송 완료. 소켓 연결 해제")
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.messageContent = ''
      setTimeout(() => this.settingAnnouncementList(), 100)
    },
    settingAnnouncementList() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/announcementList')
          .then(response => {
            console.log(response.data);
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let announcementList = JSON.parse(JSON.stringify(response.data.announcementList));
              console.log(announcementList)
              this.announcement = announcementList
              this.setAnnouncementList(this.announcement)
              console.log(this.announcement)
            } else {
              console.log('로그인되어 있지 않습니다.');
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
  },
  components: {
    VueEditor
  }
}
</script>

<style>
.util button {
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

#edit{
  height: 250px;
  overflow-y: auto;
}
</style>