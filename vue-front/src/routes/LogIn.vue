<template>
  <div class="login-container">
    <div class="logo-container">
      <!-- 로고 이미지 추가 -->
    </div>
    <div class="form-container">
      <form @submit.prevent="login()">
        <div>
          <label for="username">Username:</label>
          <input type="text" name="username" v-model="username" />
        </div>
        <div>
          <label for="password">Password:</label>
          <input type="password" name="password" v-model="password" />
        </div>
        <div>
          <input type="checkbox" name="remember-me" v-model="rememberMe" />
          <label for="remember-me">자동 로그인</label>
          <button variant="success" type="submit">Login</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'LogIn',
  data() {
    return {
      loginSuccess: false,
      loginError: false,
      username: '',
      password: '',
      error: false,
      rememberMe : false
    }
  },
  methods: {
    async login() {
      try {
        const result = await axios.get('/logIn', {
          auth: {
            username: this.username,
            password: this.password
          },
          withCredentials: true, // 쿠키 전달을 허용합니다.
          params: {
            rememberMe: this.rememberMe // 자동 로그인 여부를 파라미터로 보냅니다.
          }
        });
        if (result.status === 200) {
          this.loginSuccess = true
          await this.$router.push('/')
        }
      } catch (err) {
        this.loginError = true;
        throw new Error(err)
      }
    }
  }
}

</script>

<style>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.logo-container {
  flex: 1;
  text-align: center;
}

.form-container {
  flex: 1;
  padding: 30px;
  background-color: #f2f2f2;
}
</style>
