// import axios from 'axios'

//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        showModal: false,
        messageModal: false,
        isDeleted: false,
        messageCount: 0,
        messageList: [],
        alarmCount: 0,
        alarmList: [],
        selectedMessage: {}
    },
    getters: {
        getCount(state){
            return state.count
        }
    },
    mutations: {
        setAlarm : function (state, items) {
            return state.alarmList = items;
        },
        setAlarmCount : function (state, items) {
            return state.alarmCount = items;
        },
        setMessage : function (state, items) {
            return state.messageList = items;
        },
        setCount : function (state, messageCount) {
            return state.messageCount = messageCount;
        },
        setModal : function (state, showModal) {
            return state.showModal = showModal;
        },
        setMessageModal : function (state, messageModal) {
            return state.messageModal = messageModal;
        },
        setSelectedMessage : function (state, selectedMessage) {
            return state.selectedMessage = selectedMessage;
        }
    },
    actions: {

    }
};