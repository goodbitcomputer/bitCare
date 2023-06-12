<template>
  <div class="background">
    <div class="cover">
      <div class="login-container">
        <div class="form-container">
          <div class="logo-container" style="margin-bottom: 40px">
            <div class="row div-row-logo">
              <div class="col-12">
                <img class="BI1" src="@/assets/BI/BI1W.png">
              </div>
            </div>
            <br>
            <div class="logIn-form">
              <form @submit.prevent="login()">
                <input v-model="username" class="form-control ID" name="username"
                       placeholder="아이디" type="text"/>
                <input v-model="password" class="form-control PW" name="password"
                       placeholder="비밀번호" style="margin-bottom: 5px" type="password"/>
                <b-checkbox v-model="rememberMe" class="check-box autoLogIn" name="autoLogIn"> 자동 로그인
                </b-checkbox>
                <button class="btn form-control bg-primary" style="color: white;font-weight: bold"
                        type="submit" variant="success">
                  로그인
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {mapMutations, mapState} from "vuex"

export default {
  name: 'LogIn',
  created() {
    this.autoLogin()
  },
  data() {
    return {
      loginSuccess: false,
      loginError: false,
      username: '',
      password: '',
      error: false,
      rememberMe: false
    }
  },
  computed: {
    ...mapState('login',
        ['role','name','dept']
    )
  },
  methods: {
    ...mapMutations('login', {
      setRole: 'setRole',
      setName: 'setName',
    }),
    autoLogin(){
      axios.get('/api/login')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data && response.data.isLoggedIn) {
              let logIn = JSON.parse(JSON.stringify(response.data.logIn))
              this.setName(logIn.name)
              this.setRole(logIn.role)
              this.$router.push('/home')
            } else{
              this.setName('admin')
              this.setRole('ROLE_ADMIN')
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error)
          })

      axios.post('/autoLogin', {}
      ).then((response) => {
        if (response.status === 200) {
          if (response.data.logIn === 'success') {
            this.setRole(response.data.role)
            this.setName(response.data.name)
            this.$router.push('/home')
          }
        }
      }).catch((err) => {
        this.loginError = true
        throw new Error(err)
      })
    },
    login() {
      axios.post('/auth', {
        username: this.username,
        password: this.password,
      }, {
        params: {
          rememberMe: this.rememberMe
        }
      }).then((response) => {
        if (response.status === 200) {
          if (response.data.logIn === 'success') {
            this.setRole(response.data.role)
            this.setName(response.data.name)
            this.$router.push('/home')
          }else{
            window.Swal.fire({
              icon: 'error',
              title: 'error',
              html: '로그인 실패<br>로그인 정보를 정확하게 입력해주세요.',
              timer: 3000
            })
          }
        }
      }).catch((err) => {
        this.loginError = true
        throw new Error(err)
      })
    }
  }
}
</script>

<style>
.background {
  width: 100%;
}

.cover {
  background-color: rgba(0, 0, 0, 0.3);
}


.background::after {
  width: 100%;
  height: 100%;
  content: "";
  background-image: url("@/assets/index.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
}

.login-container {
  display: flex;
  justify-content: right;
  align-items: center;
  height: 100vh;
}

.logo-container {
  flex: 1;
  justify-content: center;
  align-items: center;
  float: right;
  text-align: right;
  padding-right: 30px;
}

.form-container {
  text-align: center;
}


.logIn-form {
  opacity: 0.7;
  float: right;
  text-align: right;
  padding-right: 50px;
}

.BI1 {
  width: 50%;
}

.ID, .PW {
  opacity: 0.5;
}

.autoLogIn {
  margin-bottom: 5px;
}

</style>