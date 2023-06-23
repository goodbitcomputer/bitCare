<template>
  <section id="mobile-doctor">
    <div id="patientList-box" class="patientList-box container text-center text-lg-start">
      <div class="util d-flex">
        <span>로그인 정보</span>
      </div>
      <div class="patient-box border-box">
        <div class="title">
          <span class="font-weight-bold">{{ this.$store.state.login.name }}</span>
        </div>
        <div class="patient-info">
          <span>{{ formatDept(this.deptId) }}</span>
          <span>{{ formatRole(this.$store.state.login.role) }}</span>
          <span>{{ this.phoneNumber }}</span>
        </div>
      </div>

      <div class="util d-flex">
        <span>프로필</span>
      </div>
      <div class="profile-box border-box">
        <div class="profile">
          <span v-html="detail"></span>
        </div>
      </div>

      <b-button pills card vertical class="admin col text-center" @click="showDetails">
        <span>내정보 수정</span>
      </b-button>
    </div>
  </section>
</template>

<script>
import axios from "axios";
import {mapMutations, mapState} from "vuex";

export default {
  data() {
    return {
      dept: '',
      deptId: 0,
      detail: '',
      phoneNumber: '',
    }
  },
  name: "MobileSetting",
  components: {},
  created() {
    this.getSessionLogIn();
  },
  computed: {
    ...mapState('login',
        ['role','name','list','registerModal','updateModal']
    ),
  },
  methods: {
    ...mapMutations('mobileDoctor', {
      setNextStep: 'setNextStep',
    }),
    ...mapMutations('login', {
      setRole: 'setRole',
      setName: 'setName',
      setList: 'setList',
      setRegisterModal: 'setRegisterModal',
      setUpdateModal: 'setUpdateModal',
    }),
    NextBtn(item) {
      this.setNextStep(item);
    },
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              this.deptId = logIn.deptId;
              this.detail = logIn.detail;
              this.phoneNumber = logIn.phoneNumber;
            } else {
              this.$router.push('/mobile/login')
            }
          })
          .catch(error => {
            console.log(error);
          });
    },
    formatDept(deptId) {
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
    },
    formatRole(role) {
      if (role === 'ROLE_DOCTOR') {
        return '의사'
      } else if (role === 'ROLE_NURSE') {
        return '간호사'
      }
    },
    logOut() {
      axios.post('/logOut', {}
      ).then((response) => {
        if (response.status === 200) {
          if (response.data.logOut === 'success') {
            this.setName('admin')
            this.setRole('ROLE_ADMIN')
            this.$router.push('/mobile/home')
          }
        }
      }).catch((err) => {
        this.loginError = true;
        throw new Error(err)
      })
    },
    showDetails() {
      this.showDetailsModal = true;
      this.setUpdateModal(this.showDetailsModal);
    },
    closeModal() {
      this.showDetailsModal = false;
      this.setRegisterModal(this.showDetailsModal);
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