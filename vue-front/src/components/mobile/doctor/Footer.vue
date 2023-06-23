<template>
  <div>
    <!-- ======= Header ======= -->
    <header id="footer" class="fixed-bottom d-flex align-items-center">
      <div id="main-box" class="container">
        <button id="main-content" class="col text-center" @click="NextBtn(1)"><b-img src="/assets/mobile/img/home_house_icon2.png"/></button>
        <button id="main-content" class="col text-center" @click="NextBtn(2)"><b-img src="/assets/mobile/img/persons_icon2.png"/></button>
        <button id="main-content" class="col text-center" @click="NextBtn(3)"><b-img src="/assets/mobile/img/person_icon2.png"/></button>
        <button id="main-content" class="col text-center" @click="NextBtn(5)">
          <span v-if="(state==='new') && (this.$store.state.alarm.messageCount > 0)" class="note-new">{{ this.$store.state.alarm.messageCount }}</span>
          <span v-else-if="this.$store.state.alarm.messageCount > 0" class="note-num">{{ this.$store.state.alarm.messageCount }}</span>
          <b-img id="icon" src="/assets/mobile/img/comment_message_icon2.png"/>
        </button>
        <button id="main-content" class="col text-center" @click="NextBtn(6)"><b-img src="/assets/mobile/img/dots_icon2.png"/></button>
        <!-- Uncomment below if you prefer to use an image logo -->
        <!-- <a href="index.html" class="logo me-auto me-lg-0"><img src="assets/img/logo.png" alt="" class="img-fluid"></a>-->

      </div>
    </header>
    <!-- End Header -->
  </div>
</template>

<script>
import {mapMutations, mapState,} from 'vuex';
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import axios from "axios";

export default {
  data(){
    return{
      tab: 1,
      count: this.messageCount,
      stompClient: null,
      recvList: this.alarmList,
      receiver: "",
      message: "",
      sender: "",
      state: "",
      message_file: "",
      showDetailsModal: this.showModal,
      isLogin: false
    }
  },
  name: "NavBar",
  created(){
    this.count = this.messageCount;
    this.recvList = this.alarmList;
    this.getSessionLogIn();
    this.settingRecvList();
    this.alarmLength();
    // App.vue가 생성되면 소켓 연결을 시도합니다.
    setTimeout(() => this.connect(), 100)
  },
  computed: {
    ...mapState('alarm',
        ['alarmList','messageList', 'messageCount', 'alarmCount', 'sendList', 'sendCount', 'showModal','messageModal']
    )
  },
  methods: {
    ...mapMutations('mobileDoctor', {
      setNextStep: 'setNextStep',
    }),
    NextBtn(item) {
      this.setNextStep(item);
    },
    ...mapMutations('alarm', {
      setAlarm: 'setAlarm',
      setAlarmCount: 'setAlarmCount',
      setSendList: 'setSendList',
      setSendCount: 'setSendCount',
      setModal: 'setModal',
      setMessageModal: 'setMessageModal',
      setMessage: 'setMessage',
      setCount: 'setCount',
    }),
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              this.receiver = logIn.name;
            } else{
              this.$router.push('/mobile/login')
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    settingRecvList() {
      axios.get('/api/receiveList')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              this.isLogin = true
              let receiveList = JSON.parse(JSON.stringify(response.data.receiveList))
              this.recvList = receiveList
              this.setAlarm(this.recvList)
              this.alarmLength()
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });

      axios.get('/api/receiveMessageList')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              this.isLogin = true
              let receiveList = JSON.parse(JSON.stringify(response.data.receiveList));
              this.setMessage(receiveList)
              if(receiveList != null) {
                this.count = receiveList.filter(element => "new" === element.receiveState).length
              }
              this.setCount(this.count)
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });

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
    alarmLength() {
      if (this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.receiveState).length
      }
      this.setAlarmCount(this.count)
    },
    sendLength() {
      if(this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.receiveState).length
      }
      this.setSendCount(this.count)
    },
    connect() {
      const serverURL = "http://localhost:8080/receive"
      let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket, options);
      this.stompClient.connect({},
          () => {
            // 소켓 연결 성공
            this.connected = true;
            // 서버의 메시지 전송 endpoint를 구독합니다.
            // 이런형태를 pub sub 구조라고 합니다.
            this.stompClient.subscribe("/send/" + this.receiver, res => {
              // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
              if((res.body !== 'cancel') && (res.body !=='read')) {
                this.state = JSON.parse(res.body).state
                if (this.alarmList != null) {
                  this.recvList = this.alarmList
                } else {
                  this.recvList = []
                }
                this.settingRecvList()
                setTimeout(() => this.state = "", 1501)
              }else{
                this.settingRecvList()
              }
            });
          },
          (error) => {
            // 소켓 연결 실패
            console.log(error);
            this.connected = false
          }
      );
    },
  }
}
</script>

<style scoped>
#footer {
  background: rgba(12, 11, 9, 0.6);
  border-bottom: 1px solid rgba(12, 11, 9, 0.6);
  transition: all 0.5s;
  z-index: 997;
  padding: 15px 0;
}
#footer img{
  width: 25px;
  /*filter: opacity(0.5) drop-shadow(0 0 0 white);*/
}
#main-box {
  display: flex;
  justify-content: space-around;
}

#main-content{
  position: relative;
  max-width: 50px;
}

.note-num {
  position: absolute;
  right: 5px;
  z-index: 3;
  height: 17px;
  width: 17px;
  line-height: 17px;
  text-align: center;
  font-weight: bold;
  background-color: red;
  border-radius: 15px;
  display: inline-block;
}

.note-new {
  position: absolute;
  right: 5px;
  z-index: 3;
  height: 17px;
  width: 17px;
  line-height: 17px;
  text-align: center;
  background-color: red;
  border-radius: 15px;
  display: inline-block;
  animation: pulse 1.5s 1;
}

.note-new:after {
  position: absolute;
  right: 5px;
  z-index: 3;
  height: 17px;
  width: 17px;
  line-height: 17px;
  text-align: center;
  background-color: red;
  border-radius: 15px;
  display: inline-block;
  animation: sonar 1.5s 1;
}

#icon {
  position: relative;
  top: 3px;
}

@keyframes sonar {
  0% {
    transform: scale(.9);
    opacity: 1;
  }
  100% {
    transform: scale(2);
    opacity: 0;
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  20% {
    transform: scale(1.4);
  }
  50% {
    transform: scale(.9);
  }
  80% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>