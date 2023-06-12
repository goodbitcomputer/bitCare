import axios from 'axios'

//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        // 환자 write데이터
        waitingData: "",    // waiting에서 선택된 환자 데이터

        // 히스토리 리스트
        historyList: [],

        // 선택한 환자내역 데이터
        historyData: "",

        // 선택한 환자내역의 visitList
        visitList: [],
        
        // 선택한 환자내역의 수납내역
        receiptData: "",    // null 이면 ""로 반환,  default(id, historyId, patientId, payment, impUid, paidAt, cardName, cardNumber, applyNum)

    },
    getters: {},
    mutations: {
        // waiting에서 선택된 환자정보 데이터 저장
        setWaitingData: (state, item) => {
            state.waitingData = item;
        },
        // history에서 선택된 환자내역 데이터 저장
        setHistoryData: (state, item) => {
            state.historyData = item;
        },

        // 히스토리 리스트 초기화
        initHistoryList(state,) {
            state.historyList = [];
        },
        // 히스토리 데이터 초기화
        initHistoryData(state,) {
            state.historyData = "";
        },
        // 히스토리 리스트 -----------------------------
        setHistoryList(state, items) {
            state.historyList = [];
            items.forEach((item) => {
                state.historyList.push({
                    id: item.id,
                    employeeId: item.employeeId,
                    patientId: item.patientId,
                    deptId: item.deptId,
                    memo: item.memo,
                    bpSystolic: item.bpSystolic,
                    bpDiastolic: item.bpDiastolic,
                    height: item.height,
                    weight: item.weight,
                    temperature: item.temperature,
                    symptomDetail: item.symptomDetail,
                    entryDate: item.entryDate,
                    visit: item.visit,
                    dept: item.dept,
                    name: item.name,
                })
            })
        },
        
        // receiptPage의 visitList 가져오기
        setVisitList(state, items) {
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
        // receiptPage의 ReceiptData 가져오기
        setReceiptData: (state, item) => {
            state.receiptData = item;
        },


    },

    actions: {

        // 히스토리 리스트 가져오기
        getHistoryList({commit}, patientId) {
            return axios.post('/doctor/getHistoryList', {
                patientId: patientId,
            }).then(response => {
                let list = response.data;
                commit('setHistoryList', list)
            }).catch(function (error) {
                console.log(error);
            });
        },

        // 선택된 히스토리 데이터 가져오기 (imgList, sbList, cbList)


    }
};