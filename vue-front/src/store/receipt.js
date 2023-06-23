
//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        // 선택한 환자내역의 수납내역
        patientData: "",
        historyData: "",
        sbList: [],         // 상병 리스트
        cbList: [],         // 처방 리스트
        imgList: [],        // 이미지 리스트
        physicalData: [],   // 신체 데이터
        savePhotoList: [],   // 새로 저장할 사진 리스트
        visitList: [],
    },
    getters: {},
    mutations: {
        setPatientData: (state, item) => {
            state.patientData = item;
        },

        setHistoryData: (state, item) => {
            state.historyData = item;
        },

        // 상병리스트 저장
        setSbList: (state, items) => {
            state.sbList = items;
        },
        // 처방리스트 저장
        setCbList: (state, items) => {
            state.cbList = items;
        },
        // 이미지리스트 저장
        setImgList: (state, items) => {
            state.imgList = items;
        },
        // 이미지리스트 저장
        setPhysicalData: (state, items) => {
            state.physicalData = items;
        },
        setVisitList: (state, items) => {
            state.visitList = [];
            items.forEach((item) => {
                state.visitList.push({
                    id: item.id,
                    code: item.code,
                    content: item.content,
                    payment: item.payment,
                    type: item.type,
                })
            })
        },
    },

    actions: {

    }
};