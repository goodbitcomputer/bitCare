<template>
  <div class="waiting-info-box">
    <b-badge pills card vertical class="admin col text-center">
      <span style="font-weight: 200; font-size: 30px;"> 수납 요청 미처리 목록 </span>
    </b-badge>
    <div class="border-box" v-for="(item) in this.$store.state.doctor.receiptOnList" :key="item.id"
         style="height: 70px">
      <div @click="selectHistoryBtn(item)">
        <div>
          <span class="font-weight-bold"> 진료기록 번호 : {{ item.id }} </span>
        </div>
        <div class="patient-info">
          <span>cn.{{ item.patientId }} </span>
          <!--          <span>{{ formatName(item.patientId)}}</span>-->
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
import {mapMutations, mapState} from "vuex";
import axios from "axios";

export default {
  name: "ReceiptNow",
  data() {
    return {
      patient: "",
    }
  },
  computed: {
    ...mapState('nurse',
        ['historyList', 'waitingData']
    ),
    isWaitingData() {
      if (this.waitingData === undefined) return false;
      else return this.waitingData === "" ? false : true;
    }
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

      setTimeout(() => {
        this.$router.push('/receipt_list')
      }, 100)
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


    // // 사진촬영버튼
    // photography() {
    //   this.setPhotoListToNewCameraList(this.savePhotoList);
    //   this.setNextStep(8);
    // },

    // // 저장 버튼
    // saveBtn() {
    //   this.saveFileList = [];
    //
    //   if (this.savePhotoList.length === 0) {
    //     Swal.fire({
    //       icon: 'error',
    //       title: '실패 !!!',
    //       text: '추가된 사진이 없습니다.',
    //       showConfirmButton: false,
    //       timer: 1000
    //     })
    //   } else if (this.bodyCategoryId === "") {
    //     Swal.fire({
    //       icon: 'error',
    //       title: '실패 !!!',
    //       text: '이미지 카테고리를 선택해주세요.',
    //       showConfirmButton: false,
    //       timer: 1000
    //     })
    //   } else {
    //     this.savePhotoList.forEach((item) => {
    //       this.saveFileList.push(item.file)
    //     })
    //
    //     let formData = new FormData();
    //     formData.append("bodyCategoryId", new Blob([JSON.stringify(this.bodyCategoryId)], {type: "application/json"}));
    //     formData.append("historyId", new Blob([JSON.stringify(this.historyData.id)], {type: "application/json"}));
    //     formData.append("memo", new Blob([JSON.stringify(this.memoEditor.getData())], {type: "application/json"}));
    //     for (let i = 0; i < this.saveFileList.length; i++) {
    //       formData.append("uploadFiles", this.saveFileList[i]);// 키,값으로 append
    //     }
    //     return axios.post('/mobile/doctor/photoSave_proc', formData, {
    //       headers: {
    //         'Content-Type': 'multipart/form-data'
    //       }
    //     }).then((response) => {
    //       if (response.data === true) {
    //         Swal.fire({
    //           icon: 'success',
    //           title: '성공 !!!',
    //           text: 'file save',
    //           showConfirmButton: false,
    //           timer: 1000
    //         }).then(() => {
    //           this.setNextStep(3);
    //         })
    //       } else {
    //         Swal.fire({
    //           icon: 'error',
    //           title: '실패 !!!',
    //           text: 'fail',
    //           showConfirmButton: false,
    //           timer: 1000
    //         }).then(() => {
    //           this.setNextStep(3);
    //         })
    //       }
    //     }).catch(function (error) {
    //       console.log(error);
    //     })
    //   }
    // },

    // // imageCategory item 선택시 data 변경
    // dropClick(item) {
    //   this.bodyCategoryName = item.categoryName;
    //   this.bodyCategoryId = item.id;
    // },

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