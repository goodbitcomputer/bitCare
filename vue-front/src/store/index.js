import Vue from 'vue'
import Vuex from 'vuex'
import doctor from './doctor'
// import about from './about'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        // doctor: doctor
        doctor,
        // about
    }
})