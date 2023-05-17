import Vue from 'vue'
import Vuex from 'vuex'
import doctor from './doctor'
import mobile from './mobile'
import mobileDoctor from './mobileDoctor'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        // doctor: doctor
        doctor,
        mobile,
        mobileDoctor,
    },
    plugins: [
        createPersistedState({
            paths: ['mobile'],
        }),
    ]
})