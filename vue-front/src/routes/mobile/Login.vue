<template>
  <div class="temp">
    <NavBar/>
    <!-- ======= Hero Section ======= -->
    <section id="hero" class="d-flex align-items-center">
      <div class="container pt-0" data-aos="zoom-in" data-aos-delay="100">
        <div class="page-box justify-content-center">
          <div class="logo-box d-flex align-items-center" data-aos="zoom-in"
               data-aos-delay="200">
            <div>
              <b-img class="" src="/assets/mobile/img/bitcare_logo2.png"></b-img>
              <h2 class="text-center">All in One EMR Cloud Platform</h2>
            </div>
          </div>
          <div class="login-box">
            <h1>Welcome to <span>BitCare</span></h1>
            <!--            <h2>All in One EMR Cloud Platform</h2>-->

            <div class="login-info-box">
              <b-input class="m-auto" v-model="username" name="username" placeholder="ID"></b-input>
              <b-input class="m-auto" v-model="password" name="password" style="margin: 5px 0 !important;"
                       type="password" placeholder="Password"></b-input>
              <input type="checkbox" name="remember-me" v-model="rememberMe"/>
              <label for="remember-me">자동 로그인</label>
              <button @click="login" class="w-25 btn btn-primary" variant="success" type="submit">Login</button>
            </div>
          </div>


        </div>
      </div>
    </section><!-- End Hero -->
  </div>
</template>

<script>
import "/public/assets/css/style.scss";
import NavBar from "@/components/mobile/NavBar.vue";
import axios from "axios";
// import axios from "axios";

export default {
  name: "MobileLogin",
  components: {
    NavBar,
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
  mounted() {
    this.autoLogin()
  },
  methods: {
    // 비동기 통신
    // login() {
    //   console.log(this.username);
    //   console.log(this.password);
    //
    //   return axios.post('/logIn_proc', null, {
    //     params: {
    //       username: this.username,
    //       password: this.password
    //     }
    //   }).then((result) => {
    //     console.log(result);
    //     if (result.status === 200) {
    //       this.$router.push('/mobile/doctor')
    //     }
    //   }).catch(function (error) {
    //     console.log(error);
    //   });
    // },
    autoLogin() {
      axios.post('/autoLogin', {}
      ).then((response) => {
        if (response.status === 200) {
          if (response.data.logIn === 'success') {
            this.$store.state.alarm.alarmList = []
            this.$store.state.login.role = response.data.role
            this.$router.push('/mobile/doctor')
          } else {
            console.log(response.data)
          }
        }
      }).catch((err) => {
        this.loginError = true;
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
            this.$store.state.alarm.alarmList = []
            this.$store.state.login.role = response.data.role
            this.$router.push('/mobile/doctor')
          } else {
            console.log(response.data)
          }
        }
      }).catch((err) => {
        this.loginError = true;
        throw new Error(err)
      })
    }

  }
}
</script>

<style scoped>
.login-box {
  margin-top: 50px;
}

.logo-box {
  margin: 0 0 20px 0;
}

.login-box {
  margin: 0px 50px;
}

@media (min-width: 992px) {
  .page-box {
    display: flex;
  }

  .logo-box {
    width: 500px;
    margin: 0 40px 0 0;
  }

  .login-box {
    width: 500px;
  }

  .login-info-box {

  }
}
</style>