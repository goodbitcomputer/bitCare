import Vue from 'vue'
import VueRouter from 'vue-router'
import Root from './Root.vue'
import Home from './Home.vue'
import Doctor from './doctor/Doctor.vue'
import Nurse from './nurse/Nurse.vue'
import LogIn from "./LogIn.vue";
import Mobile from "@/routes/mobile/Mobile.vue";
import MobileRegister from "@/routes/mobile/Register.vue";
import MobileLogin from "@/routes/mobile/Login.vue";
import MobileDoctor from "@/routes/mobile/Doctor.vue";
import MobileEditor from "@/routes/mobile/ImageEditor.vue";
import MobileCamera from "@/routes/mobile/Camera.vue";
import MobilePayment from "@/routes/mobile/Payment.vue";
import MobileSearch from "@/routes/mobile/MobileSearch.vue";
import Search from "@/routes/Search.vue";
import Receipt from "@/routes/receipt/receipt.vue";
import ImageEditor from "@/routes/doctor/ImageEditor.vue";
import Alarm from './alarm/Alarm.vue'
import store from "@/store";
import Admin from "@/routes/Admin.vue";
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
      path: '/',
      component: Root,
      meta: {
        roles: ['ROLE_DOCTOR', 'ROLE_NURSE', "ROLE_ADMIN"]
      }
    },
    {
      path: '/alarm',
      component: Alarm,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/login',
      component: LogIn,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/Home',
      component: Home,
      meta: {
        roles: ['ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER', 'ROLE_ADMIN']
      }
    },
    {
      path: '/admin',
      component: Admin,
      meta: {
        roles: ['ROLE_MASTER']
      },
      beforeEnter: function(to, from, next) {
        let roleStatus = store.state.login.role // 권한 상태
        let roleName
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
      }
    },
    {
      path: '/doctor',
      component: Doctor,
      meta: {
        roles: ['ROLE_DOCTOR', 'ROLE_MASTER']
      },
      beforeEnter: function(to, from, next) {
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
      }
    },
    {
      path: '/nurse',
      component: Nurse,
      meta: {
        roles: ['ROLE_NURSE', 'ROLE_MASTER', 'ROLE_ADMIN']
      },
      beforeEnter: function(to, from, next) {
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
      }
    },
    {
      path: '/doctor/editor/selectByPatientIdAndHistoryId',
      component: ImageEditor,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/mobile/home/',
      component: Mobile,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/mobile/login/',
      component: MobileLogin,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/mobile/register/',
      component: MobileRegister,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/mobile/doctor/',
      component: MobileDoctor,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/mobile/editor/',
      component: MobileEditor,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/mobile/camera/',
      component: MobileCamera,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/mobile/payment/',
      component: MobilePayment,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/mobile/search/',
      component: MobileSearch,
      meta: {
        roles: ['ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/search/',
      component: Search,
      meta: {
        roles: ['ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    {
      path: '/receipt_list/',
      component: Receipt,
      meta: {
        roles: ['ROLE_DOCTOR', 'ROLE_NURSE', 'ROLE_MASTER']
      }
    },
    // {
    //   path: '/:notFound(.*)',
    //   component: NotFound
    // }
  ],
})