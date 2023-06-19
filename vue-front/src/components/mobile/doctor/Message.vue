<template>
  <section id="mobile-doctor">
    <div id="patientList-box" class="patientList-box container text-center text-lg-start" data-aos="fade-up">
      <div class="util d-flex" data-aos="fade-up">
        <button class="border-box col" @click="ReceiveMessage()">받은 메시지</button>
        <button class="border-box col" @click="SendList()">보낸 메시지</button>
        <button class="border-box col" @click="SendMessage()">메시지 전송</button>
      </div>
      <div v-if="this.$store.state.alarm.messageTab === 0">
        <ReceiveMessage/>
      </div>
      <div v-else-if="this.$store.state.alarm.messageTab === 1">
        <SendList/>
      </div>
      <div v-else-if="this.$store.state.alarm.messageTab === 2">
        <SendMessage/>
      </div>
    </div>
  </section>
</template>

<script>
import ReceiveMessage from "@/components/mobile/doctor/ReceiveMessage.vue"
import SendList from "@/components/mobile/doctor/SendList.vue"
import SendMessage from "@/components/mobile/doctor/SendMessage.vue"
import {mapMutations, mapState} from "vuex";

export default {
  name: "MobileMessage",
  data() {
    return {
      tab: 0,
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
  components: {
    SendMessage,
    ReceiveMessage,
    SendList,
  },
  mounted() {
    this.divHeightFix();
  },
  computed: {
    ...mapState('alarm',
        ['messageTab'],
    ),
  },
  methods: {
    ...mapMutations('alarm', {
      setMessageTab: 'setMessageTab',
    }),
    ReceiveMessage() {
      this.setMessageTab(0)
      let div = document.getElementById('patientList-box');
      div.style.height = 'auto';
      setTimeout(() => {this.divHeightFix()},10)
    },
    SendList() {
      this.setMessageTab(1)
      let div = document.getElementById('patientList-box');
      div.style.height = 'auto';
      setTimeout(() => {this.divHeightFix()},10)
    },
    SendMessage() {
      this.setMessageTab(2)
      let div = document.getElementById('patientList-box');
      div.style.height = 'auto';
      setTimeout(() => {this.divHeightFix()},10)
    },
    divHeightFix() {
      let div = document.getElementById('patientList-box');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;

      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      }
    },
  }
}
</script>

<style scoped>

#mobile-doctor {
  width: 100%;
  /*height: 100vh;*/
  background: url("/public/assets/img/main/hero-bg.jpg");
  background-size: cover;
  /* 배경이미지 반복여부 */
  background-repeat: no-repeat;
  /* 배경의 위치는 그 세션의 정가운데에 위치 */
  background-position: center;
  /* 더이상 요소가 스크롤될때 같이 스크롤되지않고 이미지가 뷰포트부분에 고정, 화면이 스크롤 되더라도 같이 움직이지않는구조로 만듬 */
  background-attachment: fixed;
  position: relative;
  padding: 0;
}

#mobile-doctor:before {
  content: "";
  background: rgba(0, 0, 0, 0.2);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

.patientList-box {
  padding-top: 150px;
  padding-bottom: 65px;
}

.patientList-box .util button {
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

.patient-box * {
  /*color: #cccccc;*/
  white-space: nowrap;
}

.patient-box .title * {
  /*color: white;*/
  white-space: nowrap;
}

/*.patient-info span, p {*/
/*  color: #999999;*/
/*  white-space: nowrap;*/
/*}*/


@media (max-width: 992px) {
  #patientList-box {
    padding-top: 110px;
  }
}

@media (max-width: 335px) {
  #patientList-box {
    padding-top: 140px;
  }
}
</style>