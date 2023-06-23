<template>
  <section id="mobile-doctor">
    <div id="patientList-box" class="patientList-box container text-center text-lg-start" data-aos="fade-up">
      <div class="util d-flex" data-aos="fade-up">
        <span>로그인 정보</span>
      </div>
      <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200">
        <div class="title">
          <span class="font-weight-bold">{{ this.$store.state.login.name }}</span>
        </div>
        <div class="patient-info">
          <span>{{ formatDept(this.deptId) }}</span>
          <span>{{ formatRole(this.$store.state.login.role) }}</span>
          <span>{{ this.phoneNumber }}</span>
        </div>
      </div>

      <div class="util d-flex" data-aos="fade-up">
        <span>프로필</span>
      </div>
      <div class="profile-box border-box" data-aos="fade-up" data-aos-delay="200">
        <div class="profile">
          <span v-html="detail"></span>
        </div>
      </div>

      <b-button class="logout btn-outline-primary col text-center" @click="logOut">로그아웃</b-button>
      <b-button pills card vertical class="admin col text-center" data-aos="fade-up" @click="showDetails">
        <span>내정보 수정</span>
      </b-button>
    </div>

    <div>
      <b-modal v-model="this.$store.state.login.updateModal" id="modal" hide-footer size="lg" title="내정보 수정" @hidden="closeModal">
        <div id="register">
          <EmployeeUpdate/>
        </div>
      </b-modal>
    </div>
  </section>
</template>

<script>
import axios from "axios";
import {mapMutations, mapState} from "vuex";
import EmployeeUpdate from "@/components/mobile/doctor/EmployeeUpdate.vue";

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
  components: {EmployeeUpdate},
  created() {
    this.getSessionLogIn();
  },
  mounted() {
    this.divHeightFix();
  },
  computed: {
    ...mapState('login',
        ['role','name','list','registerModal', 'updateModal']
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
    divHeightFix() {
      let div = document.getElementById('patientList-box');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;

      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      }
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
      this.setUpdateModal(this.showDetailsModal);
    },
  }
}
</script>

<style scoped>

#mobile-doctor {
  width: 100%;
  /*height: 100vh;*/
  background: url("/public/assets/img/main/hero-bg.jpg");
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
  background: rgba(0, 0, 0, 0.2);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

.patientList-box {
  padding-top: 150px;
  padding-bottom: 60px;
}

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


@media (max-width: 992px) {
  #patientList-box {
    padding-top: 110px;
  }
}

@media (max-width: 335px) {
  #patientList-box {
    padding-top: 140px;
  }
}

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