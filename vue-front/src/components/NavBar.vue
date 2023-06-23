<template>
  <div id="sticky-navbar">
    <b-navbar toggleable="lg" class="b-navbar navbar-expand-md navbar-dark bg-dark">
      <div id="nav" class="container">
        <b-navbar-brand>
          <img src="@/assets/history/logo.png" width="100px">
        </b-navbar-brand>

        <b-navbar-toggle v-b-toggle.sidebar-backdrop>
        </b-navbar-toggle>
        <!-- 부트스트랩의 일부, 요소를 숨기거나 나타내게 할 수 있음 -->
        <b-collapse id="nav-collapse" is-nav>
          <b-navbar-nav class="mr-auto">
            <router-link to="/home" class="nav-item nav-link">Home</router-link>
            <router-link to="/nurse" class="nav-item nav-link">Nurse</router-link>
            <router-link to="/doctor" class="nav-item nav-link">Doctor</router-link>
          </b-navbar-nav>

          <div class="search-box d-flex justify-content-center">
            <b-collapse id="collapseWidthExample">
              <form class="search d-flex justify-content-end align-items-center" @submit.prevent="search()">
                <input class="form-control m-0" type="text" placeholder="Search" v-model="keyword">
              </form>
            </b-collapse>
            <b-button v-b-toggle.collapseWidthExample id="searchButton">
              <img src="/assets/img/main/search.png" style="width: 24px;height: 24px">
            </b-button>
          </div>

          <b-navbar-nav class="ml-auto">
            <b-button type="button" class="nav-item nav-link" id="alarm">
              <div v-if="this.$store.state.alarm.alarmCount > 0">
                <span v-if="state==='new'" class="note-new">{{ this.$store.state.alarm.alarmCount }}</span>
                <span v-else class="note-num">{{ this.$store.state.alarm.alarmCount }}</span>
                <b-icon-bell-fill title="새로운 알람!" id="notification"></b-icon-bell-fill>
              </div>
              <div v-else>
                <b-icon-bell title="알람"></b-icon-bell>
              </div>
            </b-button>

            <b-button type="button" class="nav-item nav-link" id="message" @click="showDetails()">
              <div>
                <b-icon-messenger title="쪽지함" id="notification"></b-icon-messenger>
              </div>
            </b-button>

            <router-link v-if="this.$store.state.login.role === 'ROLE_MASTER'" to="/admin" class="nav-item nav-link">
              <div>
                <b-icon-three-dots title="관리자 페이지"></b-icon-three-dots>
              </div>
            </router-link>

            <b-button type="button" class="nav-item nav-link" id="info">
              <div style="min-width: 84px">
                <b-icon-person-fill style="width: 24px; height: 24px;" title="내 정보"></b-icon-person-fill>
                {{ this.$store.state.login.name }}님
              </div>
            </b-button>

            <b-button type="button" class="nav-item nav-link" id="logOut" @click="logOut()">
              <div>
                LogOut
              </div>
            </b-button>
          </b-navbar-nav>
        </b-collapse>

        <b-sidebar
            id="sidebar-backdrop"
            title="Bit Care"
            :backdrop-variant="variant"
            backdrop
            shadow

        >
          <div class="px-3 py-2">
            <p>
              <router-link to="/home" id="alarmSideBar">Home</router-link>
            </p>
            <p>
              <router-link to="/nurse" id="alarmSideBar">Nurse</router-link>
            </p>
            <p>
              <router-link to="/doctor" id="alarmSideBar">Doctor</router-link>
            </p>
            <p>
              <router-link to="/home" id="alarmSideBar">환자 정보</router-link>
            </p>
            <p>
              <router-link to="/nurse" id="alarmSideBar">의료진 정보</router-link>
            </p>
            <p>
              <b-button type="button" id="alarmSideBar" @click="showDetails()">
              <span v-if=" this.$store.state.alarm.alarmCount > 0">
                알람 <span class="badge badge-danger">{{ this.$store.state.alarm.alarmCount }}</span>
              </span>
                <span v-else>
                알람
              </span>
              </b-button>
            </p>
          </div>
        </b-sidebar>
      </div>
      <b-popover
          custom-class="wide-popover"
          fallback-placement="clockwise"
          target="alarm"
          placement="bottom"
          width="320"
          height="500"
          triggers="hover focus blur">
        <div id="alarmList">
          <Alarm/>
        </div>
      </b-popover>

      <b-popover
          custom-class="wide-popover"
          fallback-placement="clockwise"
          target="info"
          placement="bottomleft"
          width="320"
          height="500"
          triggers="hover focus blur">
        <div id="infoList">
          <Waiting/>
        </div>
      </b-popover>

      <div>
        <b-modal v-model="this.$store.state.alarm.showModal" id="modal" hide-footer size="lg" title="쪽지" @hidden="closeModal">
          <div id="messageList">
            <Message/>
          </div>
        </b-modal>
      </div>

      <div>
        <b-modal v-model="this.$store.state.login.updateModal" id="modal" hide-footer size="lg" title="내정보 수정"
                 @hidden="closeUpdateModal">
          <div id="register">
            <EmployeeUpdate/>
          </div>
        </b-modal>
      </div>
    </b-navbar>
  </div>
</template>

<script>
/**
 * ---------------------------------------------------------------------------
 * 2023.05.16
 *
 * NavBar.vue
 * Stomp, SockJS로 웹소켓 통신을 구현한 vue 컴포넌트
 */
import {mapMutations, mapState,} from 'vuex';
import axios from "axios";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import Message from "@/routes/message/Message.vue";
import Alarm from "@/routes/alarm/Alarm.vue";
import Waiting from "@/components/home/Wating.vue";
import EmployeeUpdate from "@/components/mobile/doctor/EmployeeUpdate.vue";

export default {
  components: {Message, Alarm, Waiting, EmployeeUpdate},
  data() {
    return {
      count: this.messageCount,
      stompClient: null,
      recvList: this.alarmList,
      receiver: "",
      message: "",
      sender: "",
      state: "",
      message_file: "",
      showDetailsModal: this.showModal,
      isLogin: false,
      keyword: "",
    }
  },
  created() {
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
        ['alarmList', 'messageList', 'messageCount', 'alarmCount', 'sendList', 'sendCount', 'showModal', 'messageModal', 'announcementList']
    ),
    ...mapState('login',
        ['role', 'name', 'registerModal', 'updateModal']
    )
  },
  methods: {
    ...mapMutations('alarm', {
      setAlarm: 'setAlarm',
      setAlarmCount: 'setAlarmCount',
      setSendList: 'setSendList',
      setSendCount: 'setSendCount',
      setModal: 'setModal',
      setMessageModal: 'setMessageModal',
      setMessage: 'setMessage',
      setCount: 'setCount',
      setAnnouncementList: 'setAnnouncementList',
    }),
    ...mapMutations('login', {
      setRole: 'setRole',
      setName: 'setName',
      setRegisterModal: 'setRegisterModal',
      setUpdateModal: 'setUpdateModal',
    }),
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              this.receiver = logIn.name;
            } else {
              this.$router.push('/login')
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    connect() {
      const serverURL = "/receive"
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
              if ((res.body !== 'cancel') && (res.body !== 'read')) {
                this.state = JSON.parse(res.body).state
                if (this.alarmList != null) {
                  this.recvList = this.alarmList
                } else {
                  this.recvList = []
                }
                this.settingRecvList()
                setTimeout(() => this.state = "", 1501)
              } else {
                this.settingRecvList()
              }
            });
          },
          (error) => {
            // 소켓 연결 실패
            console.log(error)
            this.connected = false
          }
      );
    },
    /* 새로운 메세지 수 */
    alarmLength() {
      if (this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.state).length
      }
      this.setAlarmCount(this.count)
    },
    sendLength() {
      if (this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.state).length
      }
      this.setSendCount(this.count)
    },
    /* DB 데이터 가져오기 */
    settingRecvList() {
      // Axios를 사용하여 RESTful API 호출
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
              if (receiveList != null) {
                this.count = receiveList.filter(element => "new" === element.state).length
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


      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/announcementList')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let announcementList = JSON.parse(JSON.stringify(response.data.announcementList));
              this.announcement = announcementList
              this.setAnnouncementList(this.announcement)
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    showDetails() {
      this.showDetailsModal = true;
      this.setModal(this.showDetailsModal);
    },
    closeModal() {
      this.showDetailsModal = false;
      this.setModal(this.showDetailsModal);
      this.setMessageModal(this.showDetailsModal);
    },
    closeUpdateModal() {
      this.showDetailsModal = false;
      this.setUpdateModal(this.showDetailsModal);
    },
    logOut() {
      axios.post('/logOut', {}
      ).then((response) => {
        if (response.status === 200) {
          if (response.data.logOut === 'success') {
            this.setName('admin')
            this.setRole('ROLE_ADMIN')
            this.$router.push('/login')
          }
        }
      }).catch((err) => {
        this.loginError = true;
        throw new Error(err)
      })
    },
    search() {
      this.$router.push({path: "/search", query: {keyword: this.keyword}});
      // window.location.reload(true);
      this.$router.go();
    },
  }
}
</script>

<style lang="scss" scoped>
:root {
  --accent-color: #f3ff87;
  --text-color: #f0f4f5;
  --background-color: #0b002b;
}

a {
  text-decoration: none;
  color: var(--text-color);
}

.wide-popover {
  max-width: 320px;
}

.b-navbar {
  position: sticky;
  top: 0; /* 필수 */
  z-index: 500;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--background-color);
  padding: 8px 12px;
  max-height: 50px;
}



.b-nav_logo {
  font-size: 24px;
  color: var(--text-color);
}

.b-nav_logo i {
  color: var(--accent-color);
}

.b-nav_logo span {
  color: var(--accent-color);
}

.b-nav_menu {
  display: flex;
  list-style: none;
  padding-left: 0;
}

.b-nav_menu li {
  padding: 8px 12px;
}

.b-nav_menu li:hover {
  color: var(--background-color);
  background-color: var(--accent-color);
  border-radius: 4px;
}

.b-nav_menu li a:hover {
  color: var(--background-color);
}

.b-nav_icons {
  display: flex;
  color: #7dc0ff;
  list-style: none;
  padding-left: 0;
}

.b-nav_icons li {
  padding: 8px 5px;
}

.b-navbar_toggleBtn {
  display: none;
  color: var(--text-color);
  position: absolute;
  right: 32px;
  font-size: 24px;
}

.note-num {
  position: relative;
  top: -5px;
  right: -25px;
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
  position: relative;
  top: -5px;
  right: -25px;
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
  position: relative;
  top: -5px;
  right: -25px;
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

#notification {
  position: relative;
}

#message, #alarm, #logOut, #info, #searchButton {
  background-color: var(--background-color);
  border: none;
  outline: none;
  box-shadow: none;
}

#alarmSideBar {
  background-color: var(--background-color);
  border: none;
  outline: none;
  box-shadow: none;
  color: var(--background-color);
  z-index: 1500;
}

#modal {
  z-index: 999;
  display: block;
}

#alarmList {
  height: 480px;
  overflow-y: auto;
}

#infoList {
  height: 480px;
  overflow-y: auto;
}

#messageList {
  height: 480px;
  overflow-y: auto;
}

#sticky-navbar{
  position: sticky;
  top: 0; /* 필수 */
  z-index: 500;
}

@media (min-width: 581px) {
  /*class="fixed-top d-flex justify-content-center"*/
  .search-box {
    top: 8px;
  }
}

@media (max-width: 580px) {
  .search-box {
    top: 59px;
  }
}

</style>