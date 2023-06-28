import axios from 'axios'

//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        // 환자 write데이터
        waitingData: "",    // waiting에서 선택된 환자 데이터
        memoEditor: "",
        symptomEditor: "",
        physicalData: [
            {height: "", weight: "", bpSystolic: "", bpDiastolic: "", temperature: ""},
        ],
        historyId: -1,
        visit: "초진",

        // write 상병데이터
        writeSbList: [],    // historyWrite
        sbList: [],         // sbModal
        sbDummyList: [],    // sbModal(dummy)

        // write 처방데이터
        writeCbList: [],    // historyWrite
        // write 이미지데이터
        writeImgList: [],


        // 히스토리 페이지
        // 히스토리 리스트
        historyList: [],
        selectDept: 0,
        waitingList: [],
        allWaitingCount: 0,
        allHistoryList: [],
        receiptOnList: [],
        receiptCount: 0,
        historyCountList: [],
    },
    getters: {},
    mutations: {
        // 환자기록에서 편집 누를시 환자기록쓰기에 데이터 넘기기
        historyDataToWrite: (state, {history, sbList, cbList, imgList}) => {

            state.memoEditor.setData(history.memo);
            state.symptomEditor.setData(history.symptomDetail);
            state.physicalData = [{
                height: history.height,
                weight: history.weight,
                bpSystolic: history.bpSystolic,
                bpDiastolic: history.bpDiastolic,
                temperature: history.temperature
            },];
            state.historyId = history.id;
            state.visit = history.visit;
            state.writeSbList = sbList;
            state.writeCbList = cbList;
            state.writeImgList = imgList;

        },
        // writeData 초기화 기능
        initWriteHistory: (state) => {
            if (state.memoEditor !== "") {
                state.memoEditor.setData('');
                state.symptomEditor.setData('');
                state.physicalData = [{height: "", weight: "", bpSystolic: "", bpDiastolic: "", temperature: ""},];
                state.historyId = -1;
                state.writeSbList = [];
                state.sbList = [];
                state.sbDummyList = [];
                state.writeCbList = [];
                state.writeImgList = [];
            }
        },
        // 환자 + writeData 초기화 기능
        initWriteHistoryPlus: (state) => {
            state.waitingData = "";
            state.memoEditor.setData('');
            state.symptomEditor.setData('');
            state.physicalData = [{height: "", weight: "", bpSystolic: "", bpDiastolic: "", temperature: ""},];
            state.historyId = -1;
            state.writeSbList = [];
            state.sbList = [];
            state.sbDummyList = [];
            state.writeCbList = [];
            state.writeImgList = [];
        },
        // waiting에서 선택된 환자 데이터 저장
        setVisit: (state, item) => {
            state.visit = item;

        },
        // waiting에서 선택된 환자 데이터 저장
        setWaitingData: (state, item) => {
            state.waitingData = item;

        },
        // memoEditor 저장
        setSymptomEditor: (state, item) => {
            state.symptomEditor = item;
        },
        // memoEditor 저장
        setMemoEditor: (state, item) => {
            state.memoEditor = item;
        },
        // modal에서 historyWrite로 상병테이블 추가
        addWriteSbList: (state, items) => {
            state.writeSbList = [...items];
        },
        // historyWrite에서 modal테이블로 list세팅
        initSbList: (state, items) => {
            state.sbList = [...items];
        },

        // add sbList  modal상병테이블 한줄 추가
        setSbList: function (state, item) {
            state.sbList.push({
                id: item.id,
                code: item.code,
                name: item.name,
                icon: false,
                main: false,
                sub: true,
            })
        },
        // remove sbList        add 상병테이블 한줄 삭제
        removeSbList: (state, item) => {
            state.sbList = state.sbList.filter(param => param.name != item.name);
        },
        // remove writeSbList   write상병테이블 한줄 삭제
        removeWriteSbList: (state, item) => {
            state.writeSbList = state.writeSbList.filter(param => param.name != item.name);
        },

        // 모달창 상병(더미) 리스트 필터후 저장
        setSbDummyList: (state, items) => {
            state.sbDummyList = [];
            items.forEach((item) => {
                state.sbDummyList.push({
                    id: item.id,
                    code: item.code,
                    name: item.name,
                    icon: false,
                })
            })
        },


        // 처방 ----------------------------------------------
        // modal에서 historyWrite로 상병테이블 추가
        addWriteCbList: (state, items) => {
            state.writeCbList = [...items];
        },
        // remove writeSbList   write상병테이블 한줄 삭제
        removeWriteCbList: (state, item) => {
            state.writeCbList = state.writeCbList.filter(param => param.name != item.name);
        },

        // 히스토리 리스트 초기화
        initHistoryList(state,) {
            state.historyList = [];
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
            // console.log(state.historyList);
        },
        setSelectDept(state, selectDept) {
            return state.selectDept = selectDept;
        },
        setWaitingList(state, waitingList) {
            return state.waitingList = waitingList;
        },
        setAllWaitingCount(state, allWaitingCount) {
            return state.allWaitingCount = allWaitingCount;
        },
        setAllHistoryList(state, allHistoryList) {
            return state.allHistoryList = allHistoryList;
        },
        setReceiptOnList(state, receiptOnList) {
            return state.receiptOnList = receiptOnList
        },
        setReceiptCount(state, receiptCount) {
            return state.receiptCount = receiptCount
        },
        setHistoryCountList(state, historyCountList){
            return state.historyCountList = historyCountList
        },
    },

    actions: {
        // 상병 더미리스트 가져오기
        fetchSbDummyData({commit}, filterMessage) {
            return axios.post('/doctor/sbModalFilter', {
                filterMessage: filterMessage,
                sb: filterMessage,
            }).then(response => {
                let list = JSON.parse(response.data.list);
                commit('setSbDummyList', list);
            }).catch(function (error) {
                console.log(error);
            });
        },

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