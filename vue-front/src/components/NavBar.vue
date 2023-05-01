<template>
  <b-navbar toggleable="lg" class="b-navbar navbar-expand-md navbar-dark bg-dark">
    <div class="container">
      <b-navbar-brand> BitCare </b-navbar-brand>

      <b-navbar-toggle v-b-toggle.sidebar-backdrop>
      </b-navbar-toggle>
      <!-- 부트스트랩의 일부, 요소를 숨기거나 나타내게 할 수 있음 -->
      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <router-link to="/" class="nav-item nav-link">Home</router-link>
          <router-link to="/nurse" class="nav-item nav-link">Nurse</router-link>
          <router-link to="/doctor" class="nav-item nav-link">Doctor</router-link>
        </b-navbar-nav>
        <b-navbar-nav class="ml-auto">
          <router-link to="/alarm" class="nav-item nav-link">
            <div>
              <b-icon-bell title="알람"></b-icon-bell>
            </div>
          </router-link>
          <router-link to="/admin" class="nav-item nav-link">
            <div>
              <b-icon-person-fill title="계정"></b-icon-person-fill>
            </div>
          </router-link>
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
              <router-link to="/">Home</router-link>
            </p>
            <p>
              <router-link to="/nurse">Nurse</router-link>
            </p>
            <p>
              <router-link to="/doctor">Doctor</router-link>
            </p>
            <p>
              <router-link to="/">환자 정보</router-link>
            </p>
            <p>
              <router-link to="/nurse">의료진 정보</router-link>
            </p>
            <p>
              <router-link to="/doctor">쪽지</router-link>
            </p>
          </div>
        </b-sidebar>
    </div>
  </b-navbar >

</template>

<script>
import {mapMutations, mapState,} from 'vuex';
import axios from "axios";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

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
    getSessionLogIn() {
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
            this.stompClient.subscribe("/send/" + this.receiver, res => {
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
    }
  }
}
</script>

<style lang="scss" scoped>
:root {
  --accent-color: #f3ff87;
  --text-color: #f0f4f5;
  --background-color: #0b002b;}

a {
  text-decoration: none;
  color: var(--text-color);}

.b-navbar {
  position: sticky;
  top: 0; /* 필수 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--background-color);
  padding: 8px 12px;}

.b-nav_logo {
  font-size: 24px;
  color: var(--text-color);}

.b-nav_logo i {
  color: var(--accent-color);}

.b-nav_logo span {
  color: var(--accent-color);}

.b-nav_menu {
  display: flex;
  list-style: none;
  padding-left: 0;}

.b-nav_menu li {
  padding: 8px 12px;}

.b-nav_menu li:hover {
  color: var(--background-color);
  background-color: var(--accent-color);
  border-radius: 4px;}

.b-nav_menu li a:hover {
  color: var(--background-color);}

.b-nav_icons {
  display: flex;
  color: #7dc0ff;
  list-style: none;
  padding-left: 0;}

.b-nav_icons li {
  padding: 8px 5px;}

.b-navbar_toggleBtn {
  display: none;
  color:var(--text-color);
  position: absolute;
  right: 32px;
  font-size: 24px;}

</style>