import Vue from 'vue'
import Vuex from 'vuex'
import doctor from './doctor'
import nurse from './nurse'
import editor from './editor'
import alarm from './alarm'
import mobile from './mobile'
import mobileDoctor from './mobileDoctor'
import mobilePayment from './mobilePayment'
import search from './search'
import receipt from './receipt'
import createPersistedState from 'vuex-persistedstate';
import login from "./login";
Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        // doctor: doctor
        doctor,
        nurse,
        editor,
        alarm,
        mobile,
        login,
        mobileDoctor,
        mobilePayment,
        search,
        receipt,
    },
    plugins: [
        createPersistedState({
            paths: ['mobile', 'login', 'editor','alarm'],
        }),
    ]
})