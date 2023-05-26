<template>
  <!-- 쪽지 작성 페이지 -->
    <div>
          받는이:
      <input list="employees" v-model="receiver">
      <button type="button" class="btn btn-primary btn-sm" id="sendButton" @click="sendMessage">Send</button>
      <b-datalist id="employees">
        <option v-for="employee in this.$store.state.login.list" :key="employee.id">{{employee.name}}</option>
      </b-datalist>
      <vue-editor v-model="messageContent" id="edit"></vue-editor>
    </div>
</template>

<script>
import {VueEditor} from "vue2-editor";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import axios from "axios";
import {mapMutations, mapState} from "vuex";

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
  computed: {
    ...mapState('alarm',
        ['sendCount', 'sendList'],
    ),
    ...mapState('login',
        ['list']
    )
  },
  created(){
    this.getEmployeeList()
  },
  methods: {
    ...mapMutations('alarm', {
      setSendList: 'setSendList',
      setSendCount: 'setSendCount'
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
    getEmployeeList(){
      axios.get('/api/selectAll')
          .then(response => {
            console.log(response.data);
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let employeeList = JSON.parse(JSON.stringify(response.data.employeeList));
              this.setList(employeeList)
              this.sendLength()
              console.log(employeeList)
            } else {
              console.log('로그인되어 있지 않습니다.');
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    settingSendList(){
      axios.get('/api/sendMessageList')
          .then(response => {
            console.log(response.data);
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              this.isLogin = true
              let sendList = JSON.parse(JSON.stringify(response.data.sendList));
              this.setSendList(sendList)
              this.sendLength()
              console.log(sendList)
            } else {
              console.log('로그인되어 있지 않습니다.');
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
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
          connectType: "send",
          receiver: this.receiver,
          sender: this.$store.state.login.name,
          content: this.messageContent,
          receiveState: this.state,
          sendState: this.state,
          messageFile: this.message_file,
          type: this.type,
          entryDate: Date.now()
        };
        this.stompClient.send("/app/receive/" + this.receiver, JSON.stringify(msg), {});
      }
      console.log("메세지 전송 완료. 소켓 연결 해제")
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.messageContent = ''
      setTimeout(() => this.settingSendList(), 100)
    },
    sendLength() {
      if(this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.state).length
      }
      this.setSendCount(this.count)
    },
  },
  components: {
    VueEditor
  }
}
</script>

<style scoped>
#sendButton {
  font-size: 15px;
  right: -30px;
  position: relative;
}

#edit{
  height: 300px !important;
  overflow-y: auto !important;
}
</style>