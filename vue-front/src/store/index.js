import Vue from 'vue'
import Vuex from 'vuex'
import doctor from './doctor'
import alarm from './alarm'
// import about from './about'
import mobile from './mobile'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        // doctor: doctor
        doctor,
        alarm
        // about
    }
        mobile,

    },
    plugins: [
        createPersistedState(),
    ]
})