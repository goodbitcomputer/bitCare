<template>
  <!-- 쪽지 작성 페이지 -->
    <div>
          받는이:
      <input list="employees" v-model="$store.state.alarm.responseReceiver">
      <button type="button" class="btn btn-primary btn-sm" id="sendButton" @click="sendMessage">Send</button>
      <b-datalist id="employees">
        <option v-for="employee in $store.state.login.list" :key="employee.id">{{employee.name}}</option>
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
        ['sendCount', 'sendList', 'responseReceiver'],
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
      setSendCount: 'setSendCount',
      setResponseReceiver: 'setResponseReceiver',
      setMessageTab: 'setMessageTab',
    }),
    ...mapMutations('login',{
      setList: 'setList'
    }),
    connect() {
      this.sender = this.$store.state.login.name;
      const serverURL = "/receive"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({
            'client-id': this.sender
          },
          () => {
            // 소켓 연결 성공
            this.connected = true;
          },
          (error) => {
            // 소켓 연결 실패
            console.log(error)
            this.connected = false;
          }
      );
    },
    getEmployeeList(){
      axios.get('/api/selectAllRole')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let employeeList = JSON.parse(JSON.stringify(response.data.employeeList));
              this.setList(employeeList)
              this.sendLength()
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    settingSendList(){
      axios.get('/api/sendMessageList')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              this.isLogin = true
              let sendList = JSON.parse(JSON.stringify(response.data.sendList));
              this.setSendList(sendList)
              this.sendLength()
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    sendMessage() {
      let isEmployee = false
      for(let i=0; i<this.$store.state.login.list.length; i++){
        if(this.$store.state.alarm.responseReceiver === this.$store.state.login.list[i].name){
          isEmployee = true
        }
      }

      if (isEmployee && this.messageContent !== '') {
        this.connect()
        setTimeout(() => this.send(), 100)
      }else if(!isEmployee){
        window.Swal.fire({
          icon: 'error',
          title: 'error',
          html: '존재하지않는 사용자입니다. <br> 수신자를 정확하게 입력해주세요.',
          timer: 3000
        })
      }
    },
    send() {
      if (this.stompClient && this.stompClient.connected) {

        this.type = "message";

        const msg = {
          connectType: "send",
          receiver: this.$store.state.alarm.responseReceiver,
          sender: this.$store.state.login.name,
          content: this.messageContent,
          receiveState: this.state,
          sendState: this.state,
          messageFile: this.message_file,
          type: this.type,
          entryDate: Date.now()
        };
        this.stompClient.send("/app/receive/" + this.$store.state.alarm.responseReceiver, JSON.stringify(msg), {});
      }
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.messageContent = ''
      this.setResponseReceiver('');
      this.setMessageTab(0);
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

<style>
#sendButton {
  font-size: 15px;
  right: -30px;
  position: relative;
}

#edit{
  height: 250px;
  overflow-y: auto;
}
</style>