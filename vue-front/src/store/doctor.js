// import axios from 'axios'

//store.js
// import Vuex from 'vuex';

export default {
// 현재 파일(movie.js)을 Store 모듈로 활용하려면 다음 옵션이 필요합니다.
    namespaced: true,

    state: {
        sbList: [],
        sbDummyList: [
            {id:1, code: 'ss6412880', name: 'Macdonaldsdsadassssssssssssssssdadadsadsadsasadasdd'},
            {id:2, code: 'dd6347880', name: 'Shaw'},
            {id:3, code: 'ff6450880', name: 'Wilson'},
            {id:4, code: 'qq6400540', name: 'Carney'}
        ],
    },
    getters: {

    },
    mutations: {
        // setSbList : (state, items) => {
        //     // return state.sbList= [...items]
        //     state.sbList= items
        // }
        setSbList : function (state, items) {
            console.log('items')
            return state.sbList = items;
        }
    },
    actions: {

    }
};