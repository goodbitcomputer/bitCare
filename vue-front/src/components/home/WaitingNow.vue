<template>
  <div class="waiting-info-box">
    <b-badge pills card vertical class="admin col text-center">
      <span style="font-weight: 200; font-size: 30px;"> {{ formatDept(this.$store.state.doctor.selectDept) }}</span>
    </b-badge>
    <div class="border-box" v-for="(item) in this.$store.state.doctor.waitingList" :key="item.id" style="height: 70px">
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
</template>

<script>
import {mapActions, mapMutations, mapState} from "vuex";
import axios from "axios";

export default {
  name: "WaitingNow",
  data() {
    return {
      waitingList: [],
      dept: '',
      loginDeptId: 0,
    }
  },
  computed: {
    ...mapState('doctor',
        ['selectDept']
    )
  },
  created(){
    this.getSessionLogIn()
  },
  methods: {
    ...mapMutations('doctor', {
      setWaitingData: 'setWaitingData',
      initHistoryList: 'initHistoryList',
      setWaitingList: 'setWaitingList',
    }),
    ...mapActions('doctor', {
      getHistoryList: 'getHistoryList'
    }),
    selectPatientBtn(item) {
      if(this.$store.state.doctor.selectDept === this.loginDeptId) {
        this.setWaitingData(item);
        this.getHistoryList(item.patientId);

        // historyPage의 historyData 초기화
        this.$EventBus.$emit('initHistory')

        if(this.$store.state.login.role === 'ROLE_DOCTOR' || this.$store.state.login.role === 'ROLE_MASTER') {
          this.$router.push('/doctor')
        }
      }
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