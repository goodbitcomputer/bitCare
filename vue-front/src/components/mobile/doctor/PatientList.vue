<template>
  <!-- ======= Hero Section ======= -->
  <section id="mobile-doctor">
    <div id="patientList-box" class="patientList-box text-center text-lg-start" data-aos="fade-up">
      <div class="util d-flex" data-aos="fade-up">
        <button class="border-box col" @click="waitingBtn">진료대기</button>
        <button class="border-box col" @click="completedBtn">진료완료</button>
      </div>

      <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200" v-for="(item) in waitingList"
           :key="item.id">
        <div @click="selectPatientBtn(item)">
          <div class="title">
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
  </section><!-- End Hero -->

</template>

<script>

import axios from "axios";
import {mapActions, mapMutations} from "vuex";

export default {
  name: "MobilePatientList",

  data() {
    return {
      waitingList: [],  // 대기환자 리스트

    }
  },
  mounted() {
    this.divHeightFix();
    this.waitingBtn();
  },
  methods: {
    ...mapMutations('mobileDoctor', {
      setWaitingData: 'setWaitingData',
      setNextStep: 'setNextStep',
    }),
    ...mapActions('mobileDoctor', {
      getHistoryList: 'getHistoryList'
    }),
    divHeightFix() {
      let div = document.getElementById('patientList-box');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;
      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      } else {
        div.style.height = 'auto';
      }
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
    // 대기환자 리스트 저장
    setWaitingList(items) {
      this.waitingList = [];
      items.forEach((item) => {
        this.waitingList.push(item);
      })
    },
    // 대기환자 선택 버튼
    selectPatientBtn(item) {
      this.setWaitingData(item);
      this.getHistoryList(item.patientId);
      this.setNextStep(3);

      // historyPage의 historyData 초기화
      // this.$EventBus.$emit('initHistory')
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


  },
}
</script>

<style scoped>
#mobile-doctor {
  width: 100%;
  /*height: 100vh;*/
  /*background: url("/public/assets/img/main/hero-bg.jpg");*/
  background-color: #A1C7E0;
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
  background: rgba(0, 0, 0, 0.6);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

.patientList-box {
  padding-top: 60px;
  margin : 0 3px;
}

.patientList-box .util button {
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

.patient-box {
  text-align: left;
  background-color: white;
  /*background: rgba(12, 11, 9, 0.7);*/
  height: 80px;
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

.patient-info {
  flex-wrap: nowrap;
}

.patient-info span {
  font-size: 10px;
  /*// 글자 줄바꿈 안되게 함.*/
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

.border-box {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid ;*/
  border-radius: 5px;
}

</style>