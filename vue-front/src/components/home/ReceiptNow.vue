<template>
  <div class="waiting-info-box">
    <b-badge pills card vertical class="admin col text-center">
      <span style="font-weight: 200; font-size: 30px;"> 수납 요청 미처리 목록 </span>
    </b-badge>
    <div class="border-box" v-for="(item) in this.$store.state.doctor.receiptOnList" :key="item.id">
      <div @click="selectHistoryBtn(item)">
        <div>
          <span class="font-weight-bold"> cn.{{ item.patientId }} </span>
        </div>
        <div class="patient-info">
          <span>진료기록 번호 : {{ item.id }} </span>
          <span>{{ formatDept(item.deptId) }} </span>
          <span>{{ item.visit }}</span>
          <span>{{ formatDate(item.entryDate) }}</span>
        </div>
        <div>
          <span v-html="item.memo"></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapActions, mapMutations} from "vuex";
import axios from "axios";

export default {
  name: "ReceiptNow",
  data() {
    return {
      waitingList: [],
      dept: '',
      receiptRequest: false,
      receiptCount: 0,
      receipts: [],
    }
  },
  computed: {},
  methods: {
    ...mapMutations('nurse', {
      setVuexHistoryData: 'setHistoryData',
      setVisitList: 'setVisitList',
      setReceiptData: 'setReceiptData',
      initHistoryData: 'initHistoryData',
    }),
    ...mapActions('nurse', {
      getHistoryList: 'getHistoryList',
    }),

    selectHistoryBtn(item) {
      // 선택시 class에 select 추가

      axios.post('/doctor/getHistoryAddData', {
        historyId: item.id,
      }).then(response => {
        let diseaseList = JSON.parse(response.data.diseaseList);
        let diagnoseList = JSON.parse(response.data.diagnoseList);
        let imgList = JSON.parse(response.data.imgList);
        this.setSelectSbList(diseaseList);
        this.setSelectCbList(diagnoseList);
        this.setSelectImgList(imgList);
      }).catch(function (error) {
        console.log(error);
      });

      this.setHistoryData(item);
      this.setVuexHistoryData(item);  // vuex에 저장 (수납내역에 사용하기위해)
      this.getVisitData(item.visit);
      this.getReceiptData(item);
      this.pyItems = [];
      this.pyItems.push({
        height: item.height,
        weight: item.weight,
        bpSystolic: item.bpSystolic,
        bpDiastolic: item.bpDiastolic,
        temperature: item.temperature
      });

      if(this.$store.state.login.role === 'ROLE_NURSE' || this.$store.state.login.role === 'ROLE_MASTER') {
        this.$router.push('/nurse')
      }
    },

    // 선택된 진료기록데이터 저장
    setHistoryData(item) {
      this.selectHistoryData = item;
    },
    setSelectSbList(item) {
      this.selectSbList = [];
      this.sbItems = [];
      let degree = (degreeItem) => {
        return degreeItem === "주상병" ? true : false;
      }
      item.forEach((i) => {
        this.selectSbList.push({main: degree(i.degree), code: i.code, name: i.name, historyId: i.historyId})
        this.sbItems.push({main: i.degree, code: i.code, name: i.name});
      })
    },
    setSelectCbList(item) {
      this.selectCbList = item;
      this.cbItems = [];
      item.forEach((i) => {
        this.cbItems.push({code: i.code, name: i.name, dose: i.dose, time: i.time, days: i.days});
      })
    },
    setSelectImgList(item) {
      this.selectImgList = item;
      this.slides = [];
      item.forEach((i) => {
        this.slides.push({imagePath: i.imagePath})
      })
    },
    getVisitData(item) {
      return axios.post('/nurse/getVisitData', {
        visitType: item,
      }).then(response => {
        let list = response.data;
        this.setVisitList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    getReceiptData(item) {
      return axios.post('/nurse/getReceiptData', {
        historyId: item.id,
      }).then(response => {
        let data = response.data;
        this.setReceiptData(data);
      }).catch(function (error) {
        console.log(error);
      });
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
    formatDept(deptId) {
      if(deptId===0){
        return '전체 대기 목록'
      } else {
        axios.get('/api/dept', {
          params: {
            deptId: deptId
          }
        }).then(response => {
          if (response.status === 200) {
            this.dept = response.data.dept
          } else {
            console.log('진료과목명 없음')
          }
        })

        return this.dept
      }
    },
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
  }
}
</script>

<style scoped>
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
  /*글자 줄바꿈 안되게 함.*/
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