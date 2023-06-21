<template>
  <div>
    <div class="border-box">
      <div class="d-flex">
        <span class="flex-grow-1" style="font-size: 1.2em; font-weight: 700">대기 환자</span>
        <span style="cursor:pointer" @click="waitingRefresh">새로고침</span>
      </div>
      <div class="util d-flex">
        <button class="border-box col" @click="waitingBtn">진료대기</button>
        <button class="border-box col" @click="completedBtn">진료완료</button>
      </div>
    </div>
    <div class="waiting-info-box">
      <div class="border-box" v-for="(item) in waitingList" :key="item.id">
        <div @click="selectPatientBtn(item)">
          <div>
            <span class="font-weight-bold">{{ item.name }} </span>
            <span>외래진료</span>
          </div>
          <div class="patient-info">
            <span>cn.{{ item.patientId }} </span>
            <span>{{ dateMsg(item.birth) }} </span>
            <span>{{ ageMsg(item.birth) }}세 </span>
            <span>{{ item.gender }}</span>
            <span class="font-weight-bold">{{ item.state }}</span>
          </div>
          <div>
            <span>{{ item.symptom }}</span>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import axios from "axios";
import {mapActions, mapMutations} from "vuex";

export default {
  name: "DoctorWait",
  data() {
    return {
      waitingList: [],
    }
  },
  mounted() {
    this.getWaitingData();
    // waitingList 새로고침
    this.$EventBus.$on('completedRefresh', () => {
      this.waitingRefresh();
    });
  },
  computed: {},
  methods: {
    ...mapMutations('doctor', {
      setWaitingData: 'setWaitingData',
      initHistoryList: 'initHistoryList'
    }),
    ...mapActions('doctor', {
      getHistoryList: 'getHistoryList'
    }),
    selectPatientBtn(item) {
      this.setWaitingData(item);
      this.getHistoryList(item.patientId);

      // historyPage의 historyData 초기화
      this.$EventBus.$emit('initHistory')
    },

    dateMsg(item) {
      let dateTemp = new Date(item)
      let year = dateTemp.getFullYear();
      let month = dateTemp.getMonth() + 1;
      if (month < 10) {
        month = "0" + month;
      }
      let date = dateTemp.getDate();

      return year + "." + month + "." + date;
    },
    ageMsg(item) {
      let dateTemp = new Date(item)
      let dateNow = new Date();

      let tempYear = dateTemp.getFullYear();
      let nowYear = dateNow.getFullYear();
      let age = parseInt(nowYear) - parseInt(tempYear) + 1;
      return age;
    },
    setWaitingList(items) {
      this.waitingList = [];
      console.log("setWaitingList");
      items.forEach((item) => {
        console.log(item.birth);
        console.log(typeof item.birth);
        this.waitingList.push(item);
      })
    },
    // waiting data reset_logic
    getWaitingData() {
      return axios.get('/doctor/getWaitingData', {}).then(response => {
        let list = response.data;
        this.setWaitingList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    // 진료대기 버튼 로직
    waitingBtn() {
      return axios.get('/doctor/getWaitingData', {}).then(response => {
        let list = response.data;
        this.setWaitingList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    // 진료완료 버튼 로직
    completedBtn() {
      return axios.get('/doctor/getWaitingCmopletedData', {}).then(response => {
        let list = response.data;
        this.setWaitingList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    // 환자대기리스트 새로고침
    waitingRefresh() {
      this.getWaitingData();
      this.setWaitingData("");
      this.initHistoryList();
    }
  }
}
</script>

<style lang="scss" scoped>
.util button {
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

#sidebar-1 {
  position: static !important;
  display: block !important;
}

.waiting-info-box span, p {
  white-space: nowrap;
}

.patient-info {
  flex-wrap: nowrap;
}

.patient-info span {
  font-size: 10px;
  // 글자 줄바꿈 안되게 함.
  white-space: nowrap;
}

.patient-info span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 3px 0px 3px;
}
</style>