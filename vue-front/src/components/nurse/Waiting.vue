<template>
  <div>
    <div class="main-left">
<!--      <div class="border-box waiting-icon">-->
<!--        <b-icon icon="plus-circle" variant=""></b-icon>-->
<!--      </div>-->
      <div class="main">
        <div class="main-left title-border-box">
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
          <div class="main-border-box" v-for="(item) in waitingList" :key="item.id">
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
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {mapActions, mapMutations} from "vuex";

export default {
  name: "NurseWait",
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
    ...mapMutations('nurse', {
      setWaitingData: 'setWaitingData',
      initHistoryList: 'initHistoryList',
      initHistoryData: 'initHistoryData'
    }),
    ...mapActions('nurse', {
      getHistoryList: 'getHistoryList'
    }),
    selectPatientBtn(item) {
      this.initHistoryData();
      this.setWaitingData(item);
      this.getHistoryList(item.patientId);

      // historyPage의 historyData 초기화
      this.$EventBus.$emit('initNurseHistory')
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
      items.forEach((item) => {
        this.waitingList.push(item);
      })
    },
    // waiting data reset_logic
    getWaitingData() {
      return axios.get('/nurse/getWaitingData', {}).then(response => {
        let list = response.data;
        this.setWaitingList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    // 진료대기 버튼 로직
    waitingBtn() {
      this.waitingRefresh();
      return axios.get('/nurse/getWaitingData', {}).then(response => {
        let list = response.data;
        this.setWaitingList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    // 진료완료 버튼 로직
    completedBtn() {
      this.waitingRefresh();
      return axios.get('/nurse/getWaitingCmopletedData', {}).then(response => {
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
      this.initHistoryData();
    }
  }
}
</script>

<style lang="scss" scoped>
//.main-left {
//  transition: 0.3s;
//  height: 100%;
//  width: 50px;
//  overflow: hidden;
//  //background: chartreuse;
//  transition-duration: 0.3s;
//}
//
//.main-left .main {
//  display: none;
//  white-space: nowrap;
//}
//
//.main-left .waiting-icon {
//  display: inline-block;
//}
//
//.main-left:hover {
//  height: 100%;
//  width: 200px;
//  overflow: hidden;
//}
//
//.main-left:hover .waiting-icon {
//  display: none;
//}
//
//.main-left:hover * {
//  overflow: hidden;
//  display: block;
//  width: auto;
//}
.title-border-box {
  margin: 5px;
  padding: 0 5px;
  border-width: 2px;
  border-style: solid;
  border-color: #003A63;
  border-image: initial;
  border-radius: 10px;
}
.main-border-box {
  margin: 5px;
  padding: 0 5px;
  border-width: 1px;
  border-style: solid;
  border-color: #ccc;
  border-image: initial;
  border-radius: 10px;
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