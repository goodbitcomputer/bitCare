import axios from 'axios'

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        nextStep: 1,    // 1: auth, 2: register휴대폰or주민번호, 3: register이름, 4: register주소, 5: 접수
        phoneNumber: "", // 휴대폰번호
        identityNumber: "", // 주민등록번호
        name: "",   // 이름
        // address
        postcode: "",   // 우편번호
        roadAddress: "",    // 도로명주소
        streetAddress: "",  // 지번주소
        extraAddress: "",   // 상세주소
        deptId: "",   // 진료과목
        symptom: "",    // 내원목적(증상)
        patientId : 0,

    },

    mutations: {
        // step1 state 초기화
        initState: (state) => {
            state.nextStep = 1;
            state.phoneNumber = "";
            state.identityNumber = "";
        },
        // step3 state 초기화
        initStep3: (state) => {
            state.nextStep = 3;
            state.name = "";
        },
        // step4 state 초기화
        initStep4: (state) => {
            state.nextStep = 4;
            state.postcode = "";
            state.roadAddress = "";
            state.streetAddress = "";
            state.extraAddress = "";
        },
        setNextStep: (state, item) => {
            state.nextStep = item;
        },
        setPhoneNumber: (state, items) => {
            state.phoneNumber = items;
        },
        setIdentityNumber: (state, items) => {
            state.identityNumber = items;
        },
        setName: (state, item) => {
            state.name = item;
        },
        setAddress: (state, {postcode, roadAddress, streetAddress, extraAddress}) => {
            state.postcode = postcode;
            state.roadAddress = roadAddress;
            state.streetAddress = streetAddress;
            state.extraAddress = extraAddress;
        },
        setDeptId: (state, item) => {
            state.deptId = item;
        },
        setSymptom: (state, item) => {
            state.symptom = item;
        },
        setPatientId: (state, item) => {
            state.patientId = item;
        },

    }, actions: {
        // fetchSbDummyData({commit}, filterMessage) {
        //     return axios.post('/doctor/sbModalFilter', {
        //         filterMessage: filterMessage,
        //         sb: filterMessage,
        //     }).then(response => {
        //         let list = JSON.parse(response.data.list);
        //         commit('setSbDummyList', list);
        //     }).catch(function (error) {
        //         console.log(error);
        //     });
        // },


        insertPatient({commit, state}) {
            return axios.post('/mobile/insertPatient', {
                phoneNumber: state.phoneNumber,
                identityNumber: state.identityNumber,
                name: state.name,
                postcode: state.postcode,
                roadAddress: state.roadAddress,
                streetAddress: state.streetAddress,
                extraAddress: state.extraAddress,
            }).then((response) => {
                if(response.data.status == "success"){
                    window.Swal.fire({
                        icon: 'success',
                        title: 'Your work has been saved',
                        showConfirmButton: false,
                        timer: 1000
                    }).then(()=>{
                        commit('setPatientId', response.data.patientId);
                    });
                }if(response.data.status == "fail"){
                    window.Swal.fire({
                        icon: 'error',
                        title: '이미 등록된 환자입니다.',
                        showConfirmButton: false,
                        timer: 1000
                    })
                }
            }).catch(function (error) {
                console.log(error);
            })
        },
        insertRegister({state}) {
            console.log("phoneNumber: "+state.phoneNumber);
            console.log("identityNumber: "+state.identityNumber);
            console.log("name: "+state.name);
            console.log("postcode: "+state.postcode);
            console.log("roadAddress: "+state.roadAddress);
            console.log("streetAddress: "+state.streetAddress);
            console.log("extraAddress: "+state.extraAddress);
            console.log("deptId: "+state.deptId);
            console.log("symptom: "+state.symptom);
            return axios.post('/mobile/insertRegister', {
                phoneNumber: state.phoneNumber,
                identityNumber: state.identityNumber,
                name: state.name,
                postcode: state.postcode,
                roadAddress: state.roadAddress,
                streetAddress: state.streetAddress,
                extraAddress: state.extraAddress,
                deptId: state.deptId.toString(),
                symptom: state.symptom,
            }).then(() => {

            }).catch(function (error) {
                console.log(error);
            })
        }
    }, getters: {}
}