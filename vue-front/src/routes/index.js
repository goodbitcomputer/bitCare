import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from './Home.vue'
import Doctor from './doctor/Doctor.vue'
import Nurse from './nurse/Nurse.vue'
import LogIn from "./LogIn.vue";
import Mobile from "@/routes/mobile/Mobile.vue";
import MobileRegister from "@/routes/mobile/Register.vue";
import MobileLogin from "@/routes/mobile/Login.vue";
import MobileDoctor from "@/routes/mobile/Doctor.vue";
import ImageEditor from "@/routes/doctor/ImageEditor.vue";
import Alarm from './alarm/Alarm.vue'
import store from "@/store";
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
      component: Alarm,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE']
      }
    },
    {
      path: '/login',
      component: LogIn,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE']
      }
    },
    {
      path: '/',
      component: Home,
      meta: {
        roles: ['ROLE_DOCTOR', 'ROLE_NURSE']
      }
    },
    {
      path: '/doctor',
      component: Doctor,
      meta: {
        roles: ['ROLE_DOCTOR']
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

          alert('해당 페이지에 접근 권한이 없습니다.\n접근권한을 가진 계정으로 로그인 하십시오.\n현재 로그인한 계정의 권한 : '+roleName)

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
        roles: ['ROLE_NURSE']
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

          alert('해당 페이지에 접근 권한이 없습니다.\n접근권한을 가진 계정으로 로그인 하십시오.\n현재 로그인한 계정의 권한 : '+roleName)

          next(from)
        } else {
          next()
        }
      }
    },
    {
      path: '/doctor/editor',
      component: ImageEditor,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE']
      }
    },
    {
      path: '/mobile/home/',
      component: Mobile,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE']
      }
    },
    {
      path: '/mobile/login/',
      component: MobileLogin,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE']
      }
    },
    {
      path: '/mobile/register/',
      component: MobileRegister,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE']
      }
    },
    {
      path: '/mobile/doctor/',
      component: MobileDoctor,
      meta: {
        roles: ['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_NURSE']
      }
    },
    // {
    //   path: '/:notFound(.*)',
    //   component: NotFound
    // }
  ],
})