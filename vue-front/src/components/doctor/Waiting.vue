<template>
  <div>
    <div class="title-border-box">
      <div class="d-flex">
        <span class="flex-grow-1" style="font-size: 1.2em; font-weight: 700">{{ waitingMsg }}</span>
        <div class="refresh-img-box" style="cursor:pointer" @click="waitingRefresh">
          <img src="@/assets/img/refresh.png">
        </div>
      </div>
      <div class="util d-flex">
        <button class="border-box col" @click="waitingBtn">진료대기</button>
        <button class="border-box col" @click="completedBtn">진료완료</button>
      </div>
    </div>
    <div class="waiting-info-box">
      <div v-for="(item, index) in waitingList" :key="item.id">
        <div class="waiting-box border-box" :class="{'waiting-select' : (isWaitingData && (index === selectedIndex) ? true:false)}"
             @click="selectPatientBtn(item, index)">
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
import {mapActions, mapMutations, mapState} from "vuex";

export default {
  name: "DoctorWait",
  data() {
    return {
      isWaitingBtn: 1,  // 대기환자, 완료환자 버튼 클릭 유무
      waitingList: [],
      selectedIndex: -1,
    }
  },
  mounted() {
    this.getWaitingData();
    // waitingList 새로고침
    this.$EventBus.$on('completedRefresh', () => {
      this.waitingRefresh();
    });
  },

  computed: {
    ...mapState('doctor',
        ['waitingData']
    ),
    isWaitingData() {
      if (this.waitingData === undefined) return false;
      else return this.waitingData === "" ? false : true;
    },
    waitingMsg() {
      if(this.isWaitingBtn === 1) return "대기 환자";
      else return "완료 환자";
    },
  },
  methods: {
    ...mapMutations('doctor', {
      setWaitingData: 'setWaitingData',
      initHistoryList: 'initHistoryList',
      initWriteHistory: 'initWriteHistory',
    }),
    ...mapActions('doctor', {
      getHistoryList: 'getHistoryList'
    }),
    selectPatientBtn(item, index) {
      this.setWaitingData(item);
      this.getHistoryList(item.patientId);

      // 선택시 write historyData+physical 초기화
      this.initWriteHistory();

      // 선택시 class에 select 추가
      this.selectedIndex = index;

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
        this.isWaitingBtn = 1;  // 대기환자, 완료환자 구분하는 index
        this.setWaitingList(list);
        this.setWaitingData("");
        this.initHistoryList();
      }).catch(function (error) {
        console.log(error);
      });
    },
    // 진료완료 버튼 로직
    completedBtn() {
      return axios.get('/doctor/getWaitingCmopletedData', {}).then(response => {
        let list = response.data;
        this.isWaitingBtn = 2;  // 대기환자, 완료환자 구분하는 index
        this.setWaitingList(list);
        this.setWaitingData("");
        this.initHistoryList();
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
.refresh-img-box {
  width: 25px;
  margin: 0 auto;
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.refresh-img-box img {
  width: inherit;
  object-fit: contain;
}

.title-border-box {
  margin: 5px;
  padding: 0 5px;
  border-width: 2px;
  border-style: solid;
  border-color: #003A63;
  border-image: initial;
  border-radius: 10px;
}
.waiting-box:hover {
  background-color: #cccccc;
  cursor: pointer;
}
.waiting-select {
  background-color: #cccccc;
}

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
  display: flex;
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