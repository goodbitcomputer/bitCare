import Vue from 'vue'
import Vuex from 'vuex'
import doctor from './doctor'
import mobile from './mobile'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        // doctor: doctor
        doctor,
        mobile,

    },
    plugins: [
        createPersistedState(),
    ]
})