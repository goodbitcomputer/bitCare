import Vue from 'vue'
import Vuex from 'vuex'
import doctor from './doctor'
import alarm from './alarm'
import mobile from './mobile'
import mobileDoctor from './mobileDoctor'
import createPersistedState from 'vuex-persistedstate';
import login from "./login";
Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        // doctor: doctor
        doctor,
        alarm,
        mobile,
        login,
        mobileDoctor,
    },
    plugins: [
        createPersistedState({
            paths: ['mobile', 'login'],
        }),
    ]
})