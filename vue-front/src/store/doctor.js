import axios from 'axios'

//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        // write 상병데이터
        writeSbList: [],    // historyWrite
        sbList: [],         // sbModal
        sbDummyList: [],    // sbModal(dummy)
        
        // write 처방데이터
        writeCbList: [],    // historyWrite
    },
    getters: {},
    mutations: {
        // modal에서 historyWrite로 상병테이블 추가
        addWriteSbList: (state, items) => {
            state.writeSbList= [...items];
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
            console.log("item: "+item);
            console.log("item.id: "+item.id);

            state.sbList = state.sbList.filter(param => param.id != item.id);
        },
        // remove writeSbList   write상병테이블 한줄 삭제
        removeWriteSbList: (state, item) => {
            console.log("item: "+item);
            console.log("item.id: "+item.id);

            state.writeSbList = state.writeSbList.filter(param => param.id != item.id);
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
        }
    },
    actions: {
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
    }
};