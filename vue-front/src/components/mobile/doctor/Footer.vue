<template>
  <div>
    <!-- ======= Header ======= -->
    <header id="footer" class="fixed-bottom d-flex align-items-center">
      <div id="main-box" class="container ">

        <button class="col text-center" @click="NextBtn(1)"><b-img src="/assets/mobile/img/home_house_icon2.png"/></button>
        <button class="col text-center" @click="NextBtn(2)"><b-img src="/assets/mobile/img/persons_icon2.png"/></button>
        <button class="col text-center" @click="NextBtn(3)"><b-img src="/assets/mobile/img/person_icon2.png"/></button>
        <button class="col text-center" @click="NextBtn(5)"><b-img src="/assets/mobile/img/comment_message_icon2.png"/></button>
        <button class="col text-center" @click="NextBtn(6)"><b-img src="/assets/mobile/img/dots_icon2.png"/></button>
        <!-- Uncomment below if you prefer to use an image logo -->
        <!-- <a href="index.html" class="logo me-auto me-lg-0"><img src="assets/img/logo.png" alt="" class="img-fluid"></a>-->

        <nav id="navbar" class="navbar order-last order-lg-0">

        </nav><!-- .navbar -->

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
      divHeightFix: 'divHeightFix'
    }),
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            console.log(response.data);
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
    alarmLength() {
      if (this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.state).length
      }
      this.setCount(this.count)
    },
    connect() {
      console.log("연결 시도")
      const serverURL = "http://localhost:8080/receive"
      let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket, options);
      this.stompClient.connect({},
          () => {
            // 소켓 연결 성공
            this.connected = true;
            console.log("연결 성공")
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
                setTimeout(() => {this.divHeightFix()},10)
                setTimeout(() => this.state = "", 1501)
              }else{
                this.settingRecvList()
                setTimeout(() => {this.divHeightFix()},10)
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
  justify-content: space-between;
}
</style>