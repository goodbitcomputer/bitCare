
//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        historyImageId: "",
        bodyCategoryId: "",
        waitingData: '',
        historyData: '',

        // mobile
        isViewer: false,
        tempImage: "",
        imageEditor: "",
    },
    getters: {},
    mutations: {

        setHistoryImageId: (state, item) => {
            state.historyImageId = item;
        },
        setBodyCategoryId: (state, item) => {
            state.bodyCategoryId = item;
        },
        setWaitingData: (state, item) => {
            state.waitingData = item;
        },
        setHistoryData: (state, item) => {
            state.historyData = item;
        },
        // mobile
        setIsViewer: (state, item) => {
            state.isViewer = item;
        },
        setTempImage: (state, item) => {
            state.tempImage = item;
        },
        setImageEditor: (state, item) => {
            state.imageEditor = item;
        },


    },

    actions: {

    }
};