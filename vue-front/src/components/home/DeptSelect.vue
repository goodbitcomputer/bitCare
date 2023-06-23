<template>
  <div class="waiting-info-box">
    <b-badge pills card vertical class="admin col text-center" @click="selectAllButton()">
      <div>
        <span class="font-weight-bold" style="font-weight: 200; font-size: 20px;"> 전체 대기 목록 </span>
      </div>
    </b-badge>
    <div class="border-box col text-center" v-for="item in this.$store.state.login.deptList" :key="item.id">
      <div @click="selectDeptButton(item)">
        <div>
          <span class="font-weight-bold" style="font-weight: 200; font-size: 20px;">{{ item.dept }} </span>
          <br>
          <span v-if="item.id === loginDeptId" class="font-weight-bold" style="font-weight: 200; font-size: 20px;"> (나의 진료과) </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapActions, mapMutations, mapState} from "vuex";
import axios from "axios";

export default {
  name: "DeptSelect",
  data() {
    return {
      waitingList: [],
      loginDeptId: 0,
    }
  },
  created() {
    this.getDeptList()
    this.getSessionLogIn()
    this.getWaitingData()
    this.setSelectDept(0)
  },
  computed: {
    ...mapState('login',
        ['deptList','list','registerModal']
    ),
    ...mapState('doctor',
        ['selectDept']
    )
  },
  methods: {
    ...mapMutations('login',{
      setDeptList: 'setDeptList',
      setList: 'setList',
      setRegisterModal: 'setRegisterModal'
    }),
    ...mapMutations('doctor', {
      setWaitingData: 'setWaitingData',
      initHistoryList: 'initHistoryList',
      setSelectDept: 'setSelectDept',
      setWaitingList: 'setWaitingList',
      setAllWaitingCount: 'setAllWaitingCount',
    }),
    ...mapActions('doctor', {
      getHistoryList: 'getHistoryList'
    }),
    getDeptList() {
      axios.get('/api/getDeptList')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let deptList = JSON.parse(JSON.stringify(response.data.deptList));
              this.setDeptList(deptList)
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
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
    // waiting data reset_logic
    getWaitingData() {
      return axios.get('/doctor/getWaitingNow', {
        params: {
          deptId: 0
        }
      }).then(response => {
        let list = response.data;
        this.setWaitingList(list);
        if(this.$store.state.doctor.selectDept === 0){
          this.setAllWaitingCount(list.length)
        }
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
    selectDeptButton(item){
      this.setSelectDept(item.id)
      axios.get('/doctor/getWaitingNow', {
        params: {
          deptId: this.$store.state.doctor.selectDept
        }
      }).then(response => {
        let list = response.data;
        this.setWaitingList(list);
        if(this.$store.state.doctor.selectDept === 0){
          this.setAllWaitingCount(list.length)
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    selectAllButton(){
      this.setSelectDept(0)
      axios.get('/doctor/getWaitingNow', {
        params: {
          deptId: this.$store.state.doctor.selectDept
        }
      }).then(response => {
        let list = response.data;
        this.setWaitingList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              this.loginDeptId = logIn.deptId;
            }
          })
          .catch(error => {
            console.log(error);
          });
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