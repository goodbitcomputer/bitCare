<template>
  <div class="container-fluid">
    <div class="align-items-center">
      <div class="container-fluid">
        <div class="justify-content-center mb-2">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="username">아이디</label>
              <input type="text" v-model="username" name="username" class="form-control flex-grow-1" placeholder="아이디">
            </div>
          </div>
        </div>
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
        <div class="justify-content-center" style="height: 54px">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="dept">진료 과목</label>
              <b-select v-model="dept" name="dept" class="form-control flex-grow-1" placeholder="진료 과목">
                <option v-for="dept in this.$store.state.login.deptList" :key="dept.id">{{dept.dept}}</option>
              </b-select>
            </div>
          </div>
        </div>
        <div class="justify-content-center" style="height: 54px">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="name">권한</label>
              <b-select v-model="role" name="role" class="form-control flex-grow-1" placeholder="권한">
                <option v-for="role in roles" :key="role">{{role}}</option>
              </b-select>
            </div>
          </div>
        </div>
        <div class="justify-content-center">
          <div class="">
            <div class="form-floating d-flex">
              <label class="text-nowrap col-2" for="detail">프로필</label>
              <vue-editor id="detailEditor" v-model="detail" :editor-toolbar="customToolbar" placeholder="프로필"></vue-editor>
            </div>
          </div>
        </div>
        <div class="justify-content-center text-center">
          <button class="btn btn-outline-primary col-6 m-1" @click="validateInput()">계정 추가</button>
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
      roles: ['의사', '간호사'],
      customToolbar: [
        ["bold", "italic", "underline"]
      ],
      selectRole: ""
    }
  },
  created(){
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
    getEmployeeList(){
      axios.get('/api/selectAll')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let employeeList = JSON.parse(JSON.stringify(response.data.employeeList));
              this.setList(employeeList)
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
        if(this.role === '간호사'){
          this.selectRole = 'ROLE_NURSE'
        } else if(this.role === '의사'){
          this.selectRole = 'ROLE_DOCTOR'
        }
        axios.post('/api/register', {}, {
          params: {
            dept: this.dept,
            username: this.username,
            password: this.password,
            name: this.name,
            phoneNumber: phoneStr,
            role: this.selectRole,
            detail: this.detail
          }
        }).then((response) => {
          if (response.status === 200) {
            if (response.data.register === 'success') {
              this.getEmployeeList()
              window.Swal.fire({
                icon: 'success',
                title: 'success',
                text: '계정이 추가됐습니다 !',
                timer: 3000
              })
              setTimeout(() => this.setRegisterModal(false), 3000)
            }else{
              window.Swal.fire({
                icon: 'error',
                title: 'error',
                html: '계정 추가에 실패했습니다. <br> 중복된 아이디가 있는지 확인해주세요.',
                timer: 3000
              })
            }
          }
        }).catch((err) => {
          throw new Error(err)
        })
      }
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

#detailEditor{
  width: 570px;
  height: 210px;
  overflow-y: auto !important;
}
</style>