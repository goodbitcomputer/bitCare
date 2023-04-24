<template>
  <div>
    <b-button v-b-modal.modal-center @click="initSbModalTable">Launch centered modal</b-button>
    <b-icon  icon = "dash-circle"  variant = "danger" ></b-icon >
    <b-icon  icon = "plus-circle"  variant = "primary" ></b-icon >
  <b-modal id="modal-center" size="lg" scrollable centered title="BootstrapVue" button-size="sm">
      <div class="sbModal-box">

        <!--        list 박스-->
        <div class="sbModal-list-box">
          <div style="height: 40px">
            <span>list</span>
          </div>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="sbItems" :fields="sbAddFields"
                       :tbody-tr-class="rowClass" small>
<!--                <template #cell(id)="data">-->
<!--                  <div v-show="false" class="ellipsis-sb td-box-sb">-->
<!--                    {{ data.value }}-->
<!--                  </div>-->
<!--                </template>-->
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
                      icon = "dash-circle"  variant = "danger"
                      v-model="data.item.radio1"
                      :value="true"
                      @change="handleRadioChange(data.item)"
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
                <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="sbItems" :fields="sbFilterFields" small>
                <template #cell(sb)="data">
                  <div class="ellipsis-sb td-box-sb">
                    {{ data.value }}
                  </div>
                </template>
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
                      icon = "plus-circle"  variant = "primary"
                      v-model="data.item.radio1"
                      :value="true"
                      @change="handleRadioChange(data.item)"
                  ></b-icon>
                </template>
              </b-table>
            </div>
          </div>
        </div>
      </div>
    </b-modal>
  </div>
</template>

<script>
import {mapState,} from 'vuex';

export default {
  name: "writeModel",

  mounted() {
    document.addEventListener('DOMContentLoaded', () => {
      // INSERT CODE HERE
    });
  },

  data() {
    return {
      // 상병(add) 테이블 로직
      sbAddFields: [
        // {key: 'id', label: 'id', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
        {key: 'icon', label: 'remove'},
      ],

      // 상병(더미) 테이블 로직
      sbFilterFields: [
        // {key: 'id', label: 'id', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
        {key: 'icon', label: 'add'},
      ],
      sbItem: [
        {sb: '주상병', code: 'Dickerson', name: 'Macdonaldsdsadassssssssssssssssdadadsadsadsasadasdd'},
        {sb: '부상병', code: 'Larsen', name: 'Shaw'},
        {sb: '부상병', code: 'Geneva', name: 'Wilson'},
        {sb: '부상병', code: 'Jami', name: 'Carney'}
      ],
      sbAddItems: [],
      sbItems: [],

    }
  },

  computed: {
    ...mapState('doctor',
        ['sbDummyList']
    ),

  },
  methods: {
    initSbModalTable() {
      this.sbItems = [];
      this.sbDummyList.forEach((item) => {
        this.sbItems.push({
          id: item.id,
          code: item.code,
          name: item.name,
          icon: true,
        })
      })
    }
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
