import axios from 'axios'

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        nextStep: 1,    // 1: auth, 2: register휴대폰or주민번호, 3: register이름, 4: register주소, 5: 접수


    },

    mutations: {
        setNextStep: (state, item) => {
            state.nextStep = item;
        },
        setAddress: (state, {postcode, roadAddress, streetAddress, extraAddress}) => {
            state.postcode = postcode;
            state.roadAddress = roadAddress;
            state.streetAddress = streetAddress;
            state.extraAddress = extraAddress;
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
                if (response.data.status == "success") {
                    window.Swal.fire({
                        icon: 'success',
                        title: 'Your work has been saved',
                        showConfirmButton: false,
                        timer: 1000
                    }).then(() => {
                        commit('setPatientId', response.data.patientId);
                    });
                }
                if (response.data.status == "fail") {
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

    }, getters: {}
}