<template>
  <div class="container-fluid">
    <div class="align-items-center">
      <div class="container-fluid">
        <div class="justify-content-center">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="password">비밀번호</label>
              <input type="password" v-model="password" name="password" class="form-control flex-grow-1" placeholder="비밀번호">
            </div>
          </div>
        </div>
        <div class="justify-content-center">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="name">이름</label>
              <input type="text" v-model="name" name="name" class="form-control flex-grow-1" placeholder="이름">
            </div>
          </div>
        </div>
        <div class="justify-content-center">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="phoneNumber">전화번호</label>
              <input type="text" v-model="phoneNumber" name="phoneNumber" class="form-control flex-grow-1" placeholder="전화번호">
            </div>
          </div>
        </div>
        <div class="justify-content-center">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="dept">진료 과목</label>
              <b-select v-model="dept" name="dept" class="form-control flex-grow-1" placeholder="진료 과목">
                <option v-for="dept in this.$store.state.login.deptList" :key="dept.id">{{dept.dept}}</option>
              </b-select>
            </div>
          </div>
        </div>
        <div class="justify-content-center">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="detail">프로필</label>
              <vue-editor id="updateEditor" v-model="detail" :editor-toolbar="customToolbar" placeholder="프로필"></vue-editor>
            </div>
          </div>
        </div>
        <div class="justify-content-center text-center">
          <button class="btn btn-outline-primary col-6 m-1" @click="validateInput()">정보 수정</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {VueEditor} from "vue2-editor";
import {mapMutations, mapState} from "vuex";
import axios from "axios";

export default {
  name: "EmployeeRegister",
  data() {
    return {
      dept: '',
      username: '',
      password: '',
      name: '',
      phoneNumber: '',
      role: '',
      detail: '',
      roles: ['ROLE_DOCTOR', 'ROLE_NURSE'],
      customToolbar: [
        ["bold", "italic", "underline"]
      ]
    }
  },
  created(){
    this.getSessionLogIn()
    this.getDeptList()
  },
  computed: {
    ...mapState('login',
        ['deptList','list','registerModal']
    )
  },
  methods: {
    ...mapMutations('login',{
      setDeptList: 'setDeptList',
      setList: 'setList',
      setRegisterModal: 'setRegisterModal'
    }),
    ...mapMutations('mobileDoctor', {
      setNextStep: 'setNextStep',
    }),
    phonePadMsg() {
      //.replace(/[^0-9]/g,"")
      let newStr = this.phoneNumber.replace(/^(\d{0,3})(\d{0,4})(\d{0,4}){0,13}$/g, "$1-$2-$3").replace(/(-{1,2})$/g, "");
      let testStr = new RegExp(/^(010|011|016|017|019)-[0-9]{3,4}-[0-9]{4}$/);
      if(testStr.test(newStr)){
        return newStr;
      } else{
        return "";
      }
    },
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
    validateInput(){
      let phoneStr = this.phonePadMsg()
      if(phoneStr === ""){
        window.Swal.fire({
          icon: 'error',
          title: 'error',
          html: '전화 번호를 제대로 입력해주세요.<br>01x-xxxx-xxxx(-생략)',
          timer: 3000
        })
      }else {
        axios.post('/api/infoUpdate', {}, {
          params: {
            id: this.id,
            dept: this.dept,
            password: this.password,
            name: this.name,
            phoneNumber: phoneStr,
            detail: this.detail
          }
        }).then((response) => {
          if (response.status === 200) {
            if (response.data.update === 'success') {
              this.getSessionLogIn()
              window.Swal.fire({
                icon: 'success',
                title: 'success',
                text: '수정 완료 !',
                timer: 3000
              })
              setTimeout(() => this.setRegisterModal(false), 3000)
              this.setNextStep(1);
            }else{
              window.Swal.fire({
                icon: 'error',
                title: 'error',
                html: '수정 실패',
                timer: 3000
              })
            }
          }
        }).catch((err) => {
          throw new Error(err)
        })
      }
    },
    getSessionLogIn() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/login')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn));
              this.id = logIn.id;
              this.role = logIn.role;
              this.dept = this.formatDept(logIn.deptId);
              this.detail = logIn.detail;
              this.name = logIn.name;
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
  },
  components: {
    VueEditor
  }
}
</script>

<style>
label {
  padding: 0 0 0 0;
}

#updateEditor{
  width: 260px;
  height: 30vh;
  overflow-y: auto !important;
}
</style>