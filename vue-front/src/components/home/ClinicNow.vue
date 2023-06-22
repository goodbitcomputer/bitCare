<template>
  <div id="patientList-box" class="patientList-box container text-center text-lg-start">
    <b-badge pills card vertical class="admin col text-center">
      <span style="font-weight: 200; font-size: 30px;"> 총 진료 대기 인원 : {{ this.$store.state.doctor.allWaitingCount }} 명</span>
    </b-badge>
    <b-badge pills card vertical class="admin col text-center">
      <span style="font-weight: 200; font-size: 30px;"> 수납 요청 처리 : {{ this.$store.state.doctor.receiptCount }} 건 </span>
    </b-badge>
  </div>
</template>

<script>
import axios from "axios";
import {mapMutations} from "vuex";

export default {
  data(){
    return{
      receipts: [],
      receiptRequest: false,
      result: '',
    }
  },
  name: "ClinicNow",
  created(){
    this.getNotReceiptHistory()
  },
  methods: {
    ...mapMutations('doctor', {
      setAllHistoryList: 'setAllHistoryList',
      setReceiptOnList: 'setReceiptOnList',
      setReceiptCount: 'setReceiptCount',
    }),
    getNotReceiptHistory(){
      axios.get('/doctor/getNotReceiptHistory', {}).then(response => {
        let list = response.data;
        this.setReceiptOnList(list)
        this.setReceiptCount(list.length)
      }).catch(function (error) {
        console.log(error);
      });
    },
  }
}
</script>

<style scoped>
.patientList-box .util {
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

.patientList-box .admin {
  background: rgba(12, 11, 9, 0.7);
  color: white;
  width: 100%;
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

.patient-box {
  text-align: left;
  background-color: white;
  /*background: rgba(12, 11, 9, 0.7);*/
  width: 100%;
  height: 70px;
}

.profile-box {
  text-align: left;
  background-color: white;
  /*background: rgba(12, 11, 9, 0.7);*/
  width: 100%;
  height: 300px;
}

/*.patient-info span, p {*/
/*  color: #999999;*/
/*  white-space: nowrap;*/
/*}*/

.patient-info span {
  font-size: 15px;
  /*// 글자 줄바꿈 안되게 함.*/
  white-space: nowrap;
}

.profile span{
  font-size: 17px;
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

.patientList-box .logout {
  width: 100%;
}
</style>