// import Vue from 'vue'
// import App from './App.vue'
// import router from './router'
//
// Vue.config.productionTip = false
//
// new Vue({
//   router,
//   render: h => h(App)
// }).$mount('#app')


Vue.config.devtools = true
// EventBus 생성
Vue.prototype.$EventBus = new Vue();

import Vue from 'vue'
import App from './App'
import store from '@/store/index.js'  // Same as './store/index.js'
import router from '@/routes' // Same as './routes/index.js'
//bootstrap
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons)

// axios
import axios from 'axios'
Vue.prototype.$axios = axios // axios를 import 해온 뒤, 전역변수로 선언. 이때 $axios가 아니라 그냥 prototype.axios해도 되는데 프로젝트 안 레거시를 맞추기 위해 붙임
// 이미지로딩
// import loadImage from './plugins/loadImage'
// Vue.use(loadImage)

// swiper
// import { Swiper, SwiperSlide } from "vue-awesome-swiper";
import VueAwesomeSwiper from 'vue-awesome-swiper'
import "swiper/css/swiper.css";
Vue.use(VueAwesomeSwiper);



import common from '@/assets/js/common.js';
Vue.use(common)
// ckEditor 사용
import CKEditor  from '@ckeditor/ckeditor5-vue2';
Vue.use(CKEditor )

Vue.config.productionTip = false

// new Vue({
//   el: '#app',
//   store,
//   router,
//   render: h => h(App)
// })
new Vue({
  render: h => h(App),
  store,
  router,
  components: { App }
}).$mount('#app')

router.beforeEach((to, from, next) => {
  let roleStatus = store.state.login.role // 권한 상태
  let roleName;
  if (!to.meta.roles.includes(roleStatus)) {
    if(roleStatus === 'ROLE_DOCTOR'){
      roleName = '의사'
    } else if(roleStatus === 'ROLE_NURSE'){
      roleName = '간호사'
    }

    window.Swal.fire({
      icon: 'error',
      title: 'error',
      html: '해당 페이지에 접근 권한이 없습니다.<br>접근권한을 가진 계정으로 로그인 하십시오.<br>현재 로그인한 계정의 권한 : '+roleName,
      timer: 3000
    })

    next(from)
  } else {
    next()
  }
})