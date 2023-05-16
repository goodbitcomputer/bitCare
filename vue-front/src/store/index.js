import Vue from 'vue'
import Vuex from 'vuex'
import doctor from './doctor'
import alarm from './alarm'
// import about from './about'
import mobile from './mobile'
import createPersistedState from 'vuex-persistedstate';
import login from "./login";
Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        // doctor: doctor
        doctor,
        alarm,
        mobile,
        login
    },
    plugins: [
        createPersistedState(),
    ]
})

/*
router.beforeResolve((to, from, next) => {
    console.log(login.state.role)
    let roleStatus = login.state.role // 권한 상태
    if (to.meta.roles && !to.meta.roles.includes(roleStatus)) {
        //alert('해당 페이지에 접근 권한이 없습니다.')
        next(from)
    } else {
        next()
    }
})
 */