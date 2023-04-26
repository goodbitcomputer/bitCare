import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from './Home.vue'
import Doctor from './doctor/Doctor.vue'
import Nurse from './nurse/Nurse.vue'
import LogIn from './LogIn.vue'
import Alarm from './alarm/Alarm.vue'
// import NotFound from './NotFound'

Vue.use(VueRouter)

export default new VueRouter({
  scrollBehavior() {
    return { x: 0, y: 0 }
  },
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/alarm',
      component: Alarm
    },
    {
      path: '/logIn/',
      component: LogIn
    },
    {
      path: '/',
      component: Home
    },
    {
      path: '/doctor/',
      component: Doctor
    },
    {
      path: '/nurse/',
      component: Nurse
    },
    // {
    //   path: '/:notFound(.*)',
    //   component: NotFound
    // }
  ]
})