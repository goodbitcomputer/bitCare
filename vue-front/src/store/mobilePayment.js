
//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        nextStep: 1,    // 1: auth, 2: register휴대폰or주민번호, 3: register이름, 4: register주소, 5: 접수
        // 환자 write데이터
        patientData: "",    // 휴대폰 또는 주민번호로 인증받은후 환자데이터 받기
        receiptData: "",    // 수납내역리스트에서 선택시 선택한 수납내역 가져오기

    },
    getters: {},
    mutations: {
        // step1 state 초기화
        initState: (state) => {
            state.nextStep = 1;
            state.patientData = "";
            state.receiptData = "";
        },
        setNextStep: (state, item) => {
            state.nextStep = item;
        },
        // mobilePayment에서 인증을 통해 환자정보 데이터 저장
        setPatientData: (state, item) => {
            state.patientData = item;
        },
        // 수납내역리스트에서 선택한 수납내역 가져오기
        setReceiptData: (state, item) => {
            state.receiptData = item;
        },

    },

    actions: {



    }
};