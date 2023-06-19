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
        sendCount: 0,
        sendList: [],
        alarmCount: 0,
        alarmList: [],
        announcementList: [],
        selectedMessage: {},
        selectedAnnouncement: {},
        announcementModal: false,
        catastrophe: false,
        showAnnouncementModal: false,
        responseReceiver: '',
        messageTab: 0,
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
        setSendList : function (state, sendList) {
            return state.sendList = sendList;
        },
        setSendCount : function (state, sendCount) {
            return state.sendCount = sendCount;
        },
        setModal : function (state, showModal) {
            return state.showModal = showModal;
        },
        setMessageModal : function (state, messageModal) {
            return state.messageModal = messageModal;
        },
        setSelectedMessage : function (state, selectedMessage) {
            return state.selectedMessage = selectedMessage;
        },
        setAnnouncementList : function (state, announcementList) {
            return state.announcementList = announcementList;
        },
        setSelectedAnnouncement : function (state, selectedAnnouncement) {
            return state.selectedAnnouncement = selectedAnnouncement;
        },
        setAnnouncementModal : function (state, announcementModal) {
            return state.announcementModal = announcementModal;
        },
        setCatastrophe : function (state, catastrophe) {
            return state.catastrophe = catastrophe;
        },
        setShowAnnouncementModal : function(state, showAnnouncementModal) {
            return state.showAnnouncementModal = showAnnouncementModal;
        },
        setResponseReceiver : function(state, responseReceiver){
            return state.responseReceiver = responseReceiver
        },
        setMessageTab : function(state, messageTab){
            return state.messageTab = messageTab
        },
    },
    actions: {

    }
};