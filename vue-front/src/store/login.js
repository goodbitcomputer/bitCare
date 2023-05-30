// import axios from 'axios'

//store.js
// import Vuex from 'vuex';

export default {
    namespaced: true,

    state: {
        role : "ROLE_ADMIN",
        name : "admin",
        list : [],
        deptList : [],
        registerModal : false,
    },
    getters: {
        getRole(state){
            return state.role
        },
        getHasName(state){
            return state.name
        },
        getDeptList(state){
            return state.deptList
        }
    },
    mutations: {
        setRole : function (state, role) {
            return state.role = role;
        },
        setName : function (state, name){
            return state.name = name;
        },
        setList : function (state, list){
            console.log(state.list)
            return state.list = list;
        },
        setDeptList : function (state, deptList){
            return state.deptList = deptList;
        },
        setRegisterModal : function (state, registerModal){
            return state.registerModal = registerModal;
        }
    },
    actions: {

    }
};