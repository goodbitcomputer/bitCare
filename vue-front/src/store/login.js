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
        updateModal : false,
        dept : 0,
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
            return state.list = list;
        },
        setDeptList : function (state, deptList){
            return state.deptList = deptList;
        },
        setRegisterModal : function (state, registerModal){
            return state.registerModal = registerModal;
        },
        setDept : function (state, dept){
            return state.dept = dept;
        },
        setUpdateModal : function (state, updateModal){
            return state.updateModal = updateModal;
        },
    },
    actions: {

    }
};