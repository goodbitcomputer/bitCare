<template>
  <div>
    <div class="mt-3">
      <table class="table message-table">
        <thead>
        <tr>
          <th>
            알람 목록
            <button type="button" class="btn btn-danger btn-sm" id="allDelete" @click="allDeleteMessage()">
              전체 삭제
            </button>
          </th>
        </tr>
        </thead>
        <tbody v-if="this.$store.state.alarm.alarmList != null">
        <tr v-for="message in this.$store.state.alarm.alarmList" :key="message.id">
          <td>
            <div>{{ message.sender }}</div>
            <div>{{ typeString(message.type) }}</div>
            <div>{{ formatDate(message.entryDate) }}</div>
            <div>
              <button type="button" @click="showDetails(message)">
                자세히 보기
              </button>
              <button type="button" class="btn btn-danger btn-sm" id="deleteButton" @click="deleteMessage(message.id)">
                삭제
              </button>
            </div>
          </td>
        </tr>
        </tbody>
        <tbody v-else>
        <tr>
          <td>
            <span>새로운 알람이 없습니다!</span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import axios from "axios";
import {mapMutations, mapState} from "vuex";

export default {
  data() {
    return {
      stompClient: null,
      isLogin: false,
      recvList: [],
      sender: "",
      message: "",
      receiver: "",
      state: "new",
      message_file: "",
      entryDate: "",
      type: "",
      count: 0,
      selectedMessage: "",
      showDetailsModal: this.showModal,
      readMessage: "",
      getMessage: "",
    }
  },
  name: "AlarmList",
  created() {
    this.recvList = this.alarmList;
    this.count = this.alarmCount;
    this.getSessionLogIn();
    // App.vue가 생성되면 소켓 연결을 시도합니다.
  },
  mounted() {
    this.settingRecvList();
  },
  computed: {
    ...mapState('alarm',
        ['alarmList', 'alarmCount', 'messageList', 'messageCount', 'showModal', 'selectedMessage', 'messageModal', 'catastrophe', 'selectedAnnouncement', 'showAnnouncementModal']
    ),
  },
  methods: {
    ...mapMutations('alarm', {
      setAlarm: 'setAlarm',
      setAlarmCount: 'setAlarmCount',
      setMessage: 'setMessage',
      setCount: 'setCount',
      setModal: 'setModal',
      setMessageModal: 'setMessageModal',
      setSelectedMessage: 'setSelectedMessage',
      setCatastrophe: 'setCatastrophe',
      setSelectedAnnouncement: 'setSelectedAnnouncement',
      setShowAnnouncementModal: 'setShowAnnouncementModal',
    }),
    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    typeString(type) {
      if (type === 'message') {
        return '쪽지'
      } else {
        return '공지'
      }
    },
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              this.sender = logIn.name;
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    sendMessage(e) {
      if (e.keyCode === 13 && this.sender !== '' && this.message !== '') {
        this.connect()
        setTimeout(() => this.send(), 100)
      }
    },
    send() {
      if (this.stompClient && this.stompClient.connected) {
        if (this.sender != null) {
          this.type = "message";
        } else {
          this.type = "announcement";
        }
        const msg = {
          receiver: this.receiver,
          sender: this.sender,
          content: this.message,
          state: this.state,
          messageFile: this.message_file,
          type: this.type,
          entryDate: Date.now()
        };
        this.stompClient.send("/app/receive/" + this.receiver, JSON.stringify(msg), {});
      }
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.message = ''
    },
    connect() {
      const serverURL = "http://localhost:8080/receive"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({
            'client-id': this.sender
          },
          () => {
            // 소켓 연결 성공
            this.connected = true;
            // 서버의 메시지 전송 endpoint를 구독합니다.
            // 이런형태를 pub sub 구조라고 합니다.
            this.stompClient.subscribe("/send/" + this.sender, res => {
              // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
              if (res.body === "cancel") {
                this.settingRecvList()
              } else {
                this.recvList.push(JSON.parse(res.body))
                this.setAlarm(this.recvList)
                this.setAlarmCount(this.count)
              }
            });
          },
          (error) => {
            // 소켓 연결 실패
            console.log(error)
            this.connected = false;
          }
      );
    },
    /* DB 데이터 가져오기 */
    settingRecvList() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/receiveList')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              this.isLogin = true
              let receiveList = JSON.parse(JSON.stringify(response.data.receiveList));
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
    },
    deleteMessage(messageId) {
      // API를 호출해서 해당 메시지를 삭제합니다.
      // 성공적으로 삭제되면 this.settingRecvList()를 호출합니다.
      axios.get('api/deleteAlarm', {
        params: {
          id: messageId
        }
      }).then(response => {
        if (response.status === 200) {
          console.log('삭제 성공')
        } else {
          console.log('삭제 실패')
        }
      })

      setTimeout(() => this.settingRecvList(), 100)
    },
    allDeleteMessage() {
      axios.get('api/allDeleteAlarm', {
        params: {
          name: this.$store.state.login.name
        }
      }).then(response => {
        if (response.status === 200) {
          console.log('삭제 성공')
        } else {
          console.log('삭제 실패')
        }
      })

      setTimeout(() => this.settingRecvList(), 100)
    },
    alarmLength() {
      if (this.recvList != null) {
        this.count = this.recvList.filter(element => "new" === element.state).length
      } else {
        this.count = 0
      }
      this.setAlarmCount(this.count)
    },
    showDetails(message) {
      if (message.type === 'message') {
        axios.get('/api/getMessageByAlarmId', {
          params: {
            id: message.id,
          }
        }).then(response => {
          if (response.status === 200 && response.data.receiveMessage != null) {
            this.getMessage = response.data.receiveMessage
          } else{
            alert('삭제된 쪽지입니다.')
            this.deleteMessage(message.id)
          }
        })

        setTimeout(() => {
          axios.get('api/receiveMessage', {
            params: {
              id: this.getMessage.id,
            }
          }).then(response => {
            if (response.status === 200 && response.data.receiveMessage != null) {
              this.readMessage = response.data.receiveMessage
              this.setSelectedMessage(this.readMessage)
              this.showDetailsModal = true;
              this.setModal(this.showDetailsModal);
              this.setMessageModal(this.showDetailsModal);
            } else {
              alert('삭제된 메시지입니다.')
              this.deleteMessage(message.id)
            }
          })
        }, 100)

        setTimeout(() => this.readOn(this.readMessage), 100)
        this.readMessage = "";
      } else {
        axios.get('/api/getMessageByAlarmId', {
          params: {
            id: message.id,
          }
        }).then(response => {
          if (response.status === 200 && response.data.receiveMessage != null) {
            this.getMessage = response.data.receiveMessage
          } else{
            alert('삭제된 공지입니다.')
            this.deleteMessage(message.id)
          }
        })

        setTimeout(() => {
          axios.get('api/receiveMessage', {
            params: {
              id: this.getMessage.id,
            }
          }).then(response => {
            if (response.status === 200 && response.data.receiveMessage != null) {
              this.readMessage = response.data.receiveMessage
              this.setSelectedAnnouncement(this.readMessage);
              this.setShowAnnouncementModal(true);
            } else {
              alert('삭제된 공지입니다.')
              this.deleteMessage(message.id)
            }
          })
        }, 100)

        setTimeout(() => this.readOn(this.readMessage), 100)
        this.readMessage = "";
      }
    },
    readOn(message) {
      this.connect()
      setTimeout(() => this.read(message), 100)
    },
    read(message) {
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          connectType: "read",
          id: message.id
        };
        this.stompClient.send("/app/receive/" + message.sender, JSON.stringify(msg), {});
      }
      setTimeout(() => this.stompClient.disconnect(), 100)
      this.messageContent = ''
      setTimeout(() => this.settingRecvList(), 100)
    },
  }
}
</script>

<style>
/* Custom 스타일 */
.message-header {
  font-weight: bold;
  font-size: 1.2rem;
}

.message-table {
  border-collapse: separate;
  border-spacing: 0 10px;
}

#deleteButton {
  font-size: 10px;
  top: -65px;
  right: -30px;
  position: relative;
}

#allDelete {
  font-size: 10px;
  right: -25px;
  position: relative;
}
</style>