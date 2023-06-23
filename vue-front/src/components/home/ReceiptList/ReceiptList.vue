<template>
  <div>
    <div class="main-left">
      <!--      <div class="border-box waiting-icon">-->
      <!--        <b-icon icon="plus-circle" variant=""></b-icon>-->
      <!--      </div>-->
      <div class="main">
        <div class="main-left title-border-box">
          <div class="d-flex">
            <span class="flex-grow-1" style="font-size: 1.2em; font-weight: 700">수납 미요청 리스트</span>
          </div>
        </div>
        <div class="waiting-info-box">
          <div class="" v-for="(item) in this.$store.state.doctor.receiptOnList" :key="item.id">
            <div class="waiting-box main-border-box" @click="selectHistoryBtn(item)">
              <div>
                <span class="font-weight-bold">진료기록 번호 : {{ item.id }} </span>
                <span>{{ item.visit }}</span>
              </div>
              <div class="patient-info">
                <span>cn.{{ item.patientId }} </span>
                <span>{{ formatDate(item.entryDate) }}</span>
                <span class="font-weight-bold"> {{ formatDept(item.deptId) }} </span>
              </div>
              <div>
                <span v-html="item.memo"></span>
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
import {mapMutations, mapState} from "vuex";

export default {
  name: "NurseWait",

  computed: {
    ...mapState('nurse',
        ['waitingData']
    ),
    isWaitingData() {
      if (this.waitingData === undefined) return false;
      else return this.waitingData === "" ? false : true;
    },
  },
  methods: {
    ...mapMutations('receipt', {
      setHistoryData: 'setHistoryData',
      setPatientData: 'setPatientData',
      setSbList: 'setSbList',
      setCbList: 'setCbList',
      setImgList: 'setImgList',
      setPhysicalData: 'setPhysicalData',
      setVisitList: 'setVisitList',
    }),

    ...mapMutations('nurse',{
      setReceiptData: 'setReceiptData'
    }),

    selectHistoryBtn(item) {
      this.setHistoryData(item)

      axios.get('/nurse/select_one', {
        params: {
          patientId: item.patientId
        }
      }).then(response => {
        if (response.status === 200) {
          this.patient = response.data.selectPatient
          this.setPatientData(this.patient)
        }
      })

      axios.post('/search/getHistoryAddData', {
        historyId: item.id,
      }).then(response => {
        let diseaseList = JSON.parse(response.data.diseaseList);
        let diagnoseList = JSON.parse(response.data.diagnoseList);
        let imgList = JSON.parse(response.data.imgList);
        // 상병리스트
        let sbItems = [];
        diseaseList.forEach((i) => {
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
      }).catch(function (error) {
        console.log(error);
      });

      this.getReceiptData(item)
      this.getVisitData(item.visit);
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
    formatDept(deptId) {
      if (deptId === 0) {
        return '전체 대기 목록'
      } else {
        axios.get('/api/dept', {
          params: {
            deptId: deptId
          }
        }).then(response => {
          if (response.status === 200) {
            this.dept = response.data.dept
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

  },
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

.waiting-box:hover {
  background-color: #cccccc;
  cursor: pointer;
}

.waiting-select {
  background-color: #cccccc;
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