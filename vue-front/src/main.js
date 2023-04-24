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





import Vue from 'vue'
import App from './App'
import store from '@/store/index.js'  // Same as './store/index.js'
import router from './routes' // Same as './routes/index.js'
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

import common from '@/assets/js/common.js'
Vue.use(common)

new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App)
})
