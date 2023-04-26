<template>
  <div>
    <b-button v-b-modal.modal-center @click="initCbList(writeCbList)" class="w-100">
      <b-icon icon="plus-circle" variant="white"></b-icon>
      추가 (코드/명칭 검색)
    </b-button>
    <b-modal id="modal-center" size="lg" scrollable centered title="처방테이블 추가" button-size="sm" ref="modalRef">
      <div class="sbModal-box">
        <!--        list 박스-->
        <div class="sbModal-list-box">
          <div style="height: 40px">
            <span>list</span>
          </div>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="cbList" :fields="cbAddFields" small>
                <template #cell(code)="data">
                  <div class="ellipsis-code td-box-code">
                    {{ data.value }}
                  </div>
                </template>
                <template #cell(name)="data">
                  <div class="ellipsis-name td-box-name">
                    {{ data.value }}
                  </div>
                </template>
                <template #cell(icon)="data">
                  <b-icon
                      icon="dash-circle" variant="danger"
                      @click="removeSbButton(data.item)"
                  ></b-icon>
                </template>
              </b-table>
            </div>
          </div>
        </div>
        <!--        filter 박스-->
        <div class="sbModal-filter-box">
          <b-form-group
              label="Filter"
              label-for="filter-input"
              label-cols-sm="3"
              label-align-sm="right"
              label-size="sm"
              class="mb-0"

              style="height: 40px"
          >
            <b-input-group size="sm">
              <b-form-input
                  id="filter-input"
                  v-model="filter"
                  type="search"
                  placeholder="Type to Search"
              ></b-form-input>

              <b-input-group-append>
                <b-button :disabled="!filter" @click="cbFilterButton(filter)">search</b-button>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="cbDummyList" :fields="cbFilterFields" small>
                <template #cell(code)="data">
                  <div class="ellipsis-code td-box-code">
                    {{ data.value }}
                  </div>
                </template>
                <template #cell(name)="data">
                  <div class="ellipsis-name td-box-name">
                    {{ data.value }}
                  </div>
                </template>
                <template #cell(icon)="data">
                  <b-icon
                      v-if="addCbCheck(data.item)"
                      icon="check-circle" variant="success"
                      v-model="data.item.radio1"
                      :value="true"
                  ></b-icon>
                  <b-icon
                      v-else
                      icon="plus-circle" variant="primary"
                      @click="addCbButton(data.item)"
                  ></b-icon>
                </template>
              </b-table>
            </div>
          </div>
        </div>
      </div>
      <template #modal-footer="{ cancel }">
        <b-button size="sm" variant="secondary" @click="closeModal()" style="width: 100px">
          OK
        </b-button>
        <b-button size="sm" variant="secondary" @click="cancel()" style="width: 100px">
          Cancel
        </b-button>
      </template>
    </b-modal>
  </div>
</template>

<script>
import {mapMutations, mapState, mapActions} from 'vuex';
import axios from "axios";

export default {
  name: "writeModel",

  mounted() {
    document.addEventListener('DOMContentLoaded', () => {
      // INSERT CODE HERE
    });
  },

  data() {
    return {
      // 필터 로직
      filter: null,

      // 처방(add) 테이블 로직
      sbAddFields: [
        // {key: 'id', label: 'id', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
        {key: 'icon', label: 'remove'},
      ],
      sbAddItems: [],

      // 처방(더미) 테이블 로직
      sbFilterFields: [
        // {key: 'id', label: 'id', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
        {key: 'icon', label: 'add'},
      ],
      sbDummyItems: [],


    }
  },

  computed: {
    ...mapState('doctor',
        ['sbDummyList', 'sbList', 'writeSbList']
    ),

  },
  methods: {
    ...mapMutations('doctor', {
      addSbList: 'setSbList',
      removeSbList: 'removeSbList',
      addWriteSbList: 'addWriteSbList',
      initSbList: 'initSbList',
    }),
    ...mapActions('doctor', {
      filterButton: 'fetchSbDummyData'
    }),

    addSbButton(item) {
      this.addSbList(item);
    },
    removeSbButton(item) {
      this.removeSbList(item);
    },
    addCbCheck(item) {
      let isCheck = this.sbList.some(obj => obj.id == item.id);
      console.log(isCheck);
      return isCheck;
    },

    closeModal() {
      // b-modal ref를 사용하여 modal 참조
      this.addWriteSbList(this.sbList);
      this.$refs.modalRef.hide();
    },


  //   ------------------------------------------------------------------
    // modal에서 historyWrite로 상병테이블 추가
    addWriteCbList: (state, items) => {
      state.writeSbList= [...items];
    },
    // historyWrite에서 modal테이블로 list세팅
    initCbList: (state, items) => {
      state.sbList = [...items];
    },

    // add sbList  modal상병테이블 한줄 추가
    setCbList: function (state, item) {
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
    removeCbList: (state, item) => {
      console.log("item: "+item);
      console.log("item.id: "+item.id);

      state.sbList = state.sbList.filter(param => param.id != item.id);
    },
    // remove writeSbList   write상병테이블 한줄 삭제
    removeWriteCbList: (state, item) => {
      console.log("item: "+item);
      console.log("item.id: "+item.id);

      state.writeSbList = state.writeSbList.filter(param => param.id != item.id);
    },

    // 모달창 상병(더미) 리스트 필터후 저장
    setCbDummyList: (state, items) => {
      state.sCDummyList = [];
      items.forEach((item) => {
        state.sbCummyList.push({
          id: item.id,
          code: item.code,
          name: item.name,
          icon: false,
        })
      })
    },

    // 비동기 통신
    fetchCbDummyData({commit}, filterMessage) {
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
}
</script>

<style lang="scss" scoped>
.sbModal-box {
  display: flex;
}

.sbModal-list-box {
  width: 50%;
}

.sbModal-filter-box {
  width: 50%;
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px; /* 요소의 최대 너비를 지정합니다. */
}

.td-box-name {
  width: 100px;
}


@media (min-width: 992px) {
  .ellipsis-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 200px; /* 요소의 최대 너비를 지정합니다. */
  }
  .td-box-name {
    width: 200px;
  }
}
</style>
