// import axios from 'axios'

//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        role : "ROLE_ADMIN"
    },
    getters: {
        getRole(state){
            return state.role
        }
    },
    mutations: {
        setRole : function (state, role) {
            console.log(state.role)
            return state.role = role;
        },
    },
    actions: {

    }
};