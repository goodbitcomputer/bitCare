import axios from 'axios'

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        // 진료환자 데이터

        nextStep: 0,        // 1: patientList, 2: patientHistoryList, 3: patientHistory,
        patientData: "",    // waiting에서 선택된 환자 정보 데이터
        historyList: [],    // waiting에서 선택된 환자 진료기록 데이터
        historyData: "",    // 진료기록 데이터
        sbList: [],         // 상병 리스트
        cbList: [],         // 처방 리스트
        imgList: [],        // 이미지 리스트
        physicalData: [],   // 신체 데이터
        savePhotoList: [],   // 새로 저장할 사진 리스트
    },

    mutations: {
        // 진료환자 메소드
        // 다음 페이지 넘어가기
        setNextStep: (state, item) => {
            state.nextStep = item;
        },
        // 선택한 대기환자 데이터 저장
        setPatientData: (state, items) => {
            state.patientData = items;
        },
        // 히스토리 리스트 초기화
        initHistoryList(state, ) {
            state.historyList = [];
        },
        // 히스토리 리스트 -----------------------------
        setHistoryList(state, items) {
            console.log("setHistoryList");
            console.log(items);
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
                    diseaseName: item.diseaseName,
                })
            })
        },
        // 진료기록 데이터 저장
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

    }, actions: {
        // 히스토리 리스트 가져오기
        getHistoryList({commit}, patientId) {
            return axios.post('/search/getHistoryList', {
                patientId: patientId,
            }).then(response => {
                let list = response.data;
                commit('setHistoryList', list)
            }).catch(function (error) {
                console.log(error);
            });
        },

    }, getters: {}
}