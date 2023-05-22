// import axios from 'axios'

//store.js
// import Vuex from 'vuex';

export default {
    namespaced: true,

    state: {
        role : "ROLE_ADMIN",
        name : "admin"
    },
    getters: {
        getRole(state){
            return state.role
        },
        getHasName(state){
            return state.name
        }
    },
    mutations: {
        setRole : function (state, role) {
            console.log(state.role)
            return state.role = role;
        },
        setName : function (state, name){
            console.log(state.name)
            return state.name = name;
        }
    },
    actions: {

    }
};