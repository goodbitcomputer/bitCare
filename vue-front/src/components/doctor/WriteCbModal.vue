<template>
  <div>
    <b-button v-b-modal.cbModal-center @click="initCbList(writeCbList)" class="w-100">
      <b-icon icon="plus-circle" variant="white"></b-icon>
      추가 (코드/명칭 검색)
    </b-button>
    <b-modal id="cbModal-center" size="lg" scrollable centered title="처방테이블 추가" button-size="sm" ref="modalCbRef">
      <div class="sbModal-box">
        <!--        list 박스-->
        <div class="sbModal-list-box">
          <div style="height: 40px">
            <span>list</span>
          </div>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="this.cbAddItems" :fields="cbAddFields" small>
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
                      @click="removeCbList(data.item)"
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
                <b-button :disabled="!filter" @click="fetchCbDummyData(filter)">search</b-button>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="cbDummyItems" :fields="cbFilterFields" small>
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
import {mapMutations, mapState} from 'vuex';
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
      filter: null, // 검색란이 null일시 비활성화

      // 처방(add) 테이블 로직
      cbAddFields: [
        // {key: 'id', label: 'id', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
        {key: 'icon', label: 'remove'},
      ],
      cbAddItems: [],

      // 처방(더미) 테이블 로직
      cbFilterFields: [
        // {key: 'id', label: 'id', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
        {key: 'icon', label: 'add'},
      ],
      cbDummyItems: [],

    }
  },

  computed: {
    ...mapState('doctor',
        ['writeCbList']
    ),




  },
  methods: {
    ...mapMutations('doctor', {
      addWriteCbList: 'addWriteCbList',
    }),


    addCbCheck(item) {
      let isCheck = this.cbAddItems.some(obj => obj.name === item.name);
      return isCheck;
    },

    closeModal() {
      // b-modal ref를 사용하여 modal 참조
      this.addWriteCbList(this.cbAddItems);
      this.$refs.modalCbRef.hide();
    },

    // 처방 --------------------------------

    // historyWrite에서 modalCb테이블로 list세팅
    initCbList(items) {
      this.cbAddItems = [...items];
    },

    // add cbAddItems  modal상병테이블 한줄 추가
    addCbButton(item) {
      this.cbAddItems.push({
        id: item.id,
        code: item.code,
        name: item.name,
        dose: item.dose,
        time: item.time,
        days: item.days,
        icon: false,
      })
    },
    // remove cbList        add 처방테이블 한줄 삭제
    removeCbList(item) {
      this.cbAddItems = this.cbAddItems.filter(param => param.name != item.name);
    },
    // 모달창 상병(더미) 리스트 필터후 저장
    setCbDummyList(items) {
      this.cbDummyItems = [];
      items.forEach((item) => {
        this.cbDummyItems.push({
          id: item.id,
          code: item.code,
          name: item.name,
          dose: item.dose,
          time: item.time,
          days: item.days,
          icon: false,
        })
      })
    },

    // 비동기 통신
    fetchCbDummyData(filterMessage) {
      return axios.post('/doctor/cbModalFilter', {
        filterMessage: filterMessage,
      }).then(response => {
        let list = response.data;
        this.setCbDummyList(list);
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
