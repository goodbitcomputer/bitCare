<template>
  <!-- ======= Hero Section ======= -->
  <section id="mobile-doctor">
    <div id="mobileDoctor-box" class="text-center text-lg-start" data-aos="fade-up">
      <div class="util" data-aos="fade-up">
        <div class="title">
          <span class="font-weight-bold">{{ this.patientData.name }}</span>
          <span>cn.{{ this.patientData.id }}</span>
          <span>{{ dateMsg(this.patientData.birth) }}</span>
          <span>{{ ageMsg(this.patientData.birth) }}세</span>
          <span>{{ this.patientData.gender }}</span>
        </div>
      </div>

      <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200" v-for="(item) in historyList"
           :key="item.id">
        <div @click="selectHistoryBtn(item)">
          <div class=" title ellipsis-name">
            <span class="font-weight-bold">{{ dateMsg(item.entryDate) }}</span>
            <span>{{ item.visit }}</span>
            <span>{{ item.dept }}</span>
            <span>{{ item.name }}</span>
          </div>
          <div class="patient-info">
            <div class="d-flex ellipsis-name" style="height: 20px;">
              <div>
                <span>증상: </span>
              </div>
              <div class="ellipsis-name" v-html="item.symptomDetail"></div>
            </div>
            <div class="ellipsis-name">
              <span>주상병: </span><span>{{ item.diseaseName }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section><!-- End Hero -->

</template>

<script>
import {mapMutations, mapState} from "vuex";
import axios from "axios";

export default {
  name: "MobileSearchHistoryList",

  mounted() {
    // this.divHeightFix();
  },
  computed: {
    ...mapState('search',
        ['historyList', 'patientData']
    ),
  },
  methods: {
    ...mapMutations('search', {
      setNextStep: 'setNextStep',
      setHistoryData: 'setHistoryData',
      setSbList: 'setSbList',
      setCbList: 'setCbList',
      setImgList: 'setImgList',
      setPhysicalData: 'setPhysicalData',

    }),
    // divHeightFix() {
    //   let div = document.getElementById('mobileDoctor-box');
    //   let divHeight = div.offsetHeight;
    //   let windowHeight = window.innerHeight;
    //
    //   if (divHeight < windowHeight) {
    //     div.style.height = '100vh';
    //   } else {
    //     div.style.height = 'auto';
    //   }
    // },
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

    setHistoryLogic(item, diseaseList, diagnoseList, imgList){
      // 상병리스트
      let sbItems = [];
      diseaseList.forEach((i)=>{
        sbItems.push({main: i.degree, code: i.code, name: i.name});
      })
      // 처방리스트
      let cbItems = [];
      diagnoseList.forEach((i) => {
        cbItems.push({code: i.code, name: i.name, dose: i.dose, time: i.time, days: i.days});
      })
      // 이미지리스트
      let imgItems = imgList;
      let pyItems = [];
      pyItems.push({
        height: item.height,
        weight: item.weight,
        bpSystolic: item.bpSystolic,
        bpDiastolic: item.bpDiastolic,
        temperature: item.temperature
      });

      // vuex에 저장
      this.setHistoryData(item);
      this.setSbList(sbItems);
      this.setCbList(cbItems);
      this.setImgList(imgItems);
      this.setPhysicalData(pyItems);
    },

    // 진료기록리스트에서 특정 진료기록 선택
    selectHistoryBtn(item) {
      axios.post('/search/getHistoryAddData', {
        historyId: item.id,
      }).then(response => {
        let diseaseList = JSON.parse(response.data.diseaseList);
        let diagnoseList = JSON.parse(response.data.diagnoseList);
        let imgList = JSON.parse(response.data.imgList);
        this.setHistoryLogic(item, diseaseList, diagnoseList, imgList);
        this.setNextStep(3);  // 환자기록 페이지로 넘어감
      }).catch(function (error) {
        console.log(error);
      });
    },

  },
}
</script>

<style scoped>
#mobile-doctor {
  width: 100%;
  /*height: 100vh;*/
  /*background-color: #A1C7E0;*/
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

/*#mobile-doctor:before {*/
/*  content: "";*/
/*  background: rgba(0, 0, 0, 0.6);*/
/*  position: absolute;*/
/*  bottom: 0;*/
/*  top: 0;*/
/*  left: 0;*/
/*  right: 0;*/
/*}*/

#mobileDoctor-box {
  /*padding-top: 70px;*/
  /*padding-bottom: 65px;*/
  margin : 0 3px;
}

#mobileDoctor-box .util {
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

#mobileDoctor-box * {
  /*color: #cccccc;*/
  white-space: nowrap;
}

.patient-box {
  text-align: left;
  background-color: white;
  /*background: rgba(12, 11, 9, 0.7);*/
  height: 80px;
}

.title span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 5px 0px 5px;
}

.util {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid transparent;*/
  border-radius: 5px;
}

.border-box {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid transparent;*/
  border-radius: 5px;
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%; /* 요소의 최대 너비를 지정합니다. */
}


p {
  margin: 0 !important;
}

@media (max-width: 590px) {
  #mobileDoctor-box {
    /*padding-top: 90px;*/
  }
}

</style>