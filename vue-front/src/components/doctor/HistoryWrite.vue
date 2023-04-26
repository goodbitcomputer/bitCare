<template>
  <div>
    <div class="border-box">
      <div>
        <span>진료 기록 작성</span>
      </div>
    </div>
    <!--    증상-->
    <div class="border-box">
      <div>
        <span>증상</span>
      </div>
      <div class="symptom-box">
        <div class="symptomInfo-box">
          <textarea></textarea>
        </div>
        <div class="writeImg-box">
          <img src="@/assets/history/s2.jpg">
        </div>
      </div>
    </div>

    <!--    상병-->
    <div class="border-box">
      <p>상병</p>
      <div>
        <b-table :items="writeSbList" :fields="sbFields">
          <template #cell(main)="data">
            <b-form-radio
                v-model="data.item.main"
                :value="true"
                @change="handleRadioChange(data.item)"
            ></b-form-radio>
          </template>
          <template #cell(sub)="data">
            <b-form-radio
                v-model="data.item.main"
                :value="false"
                @change="handleRadioChange(data.item)"
            ></b-form-radio>
          </template>
          <template #cell(icon)="data">
            <b-icon
                icon="dash-circle" variant="danger"
                v-model="data.item.radio1"
                :value="true"
                @click="removeWriteSbList(data.item)"
            ></b-icon>
          </template>
          <template #cell(name)="data">
            <div class="ellipsis-name td-box-name">
              {{ data.value }}
            </div>
          </template>
        </b-table>
        <!--        상병추가모달-->
        <writeSb-model/>
      </div>
    </div>
    <!--    처방-->
    <div class="border-box">
      <p>처방</p>
      <div>
        <b-table :items="writeCbList" :fields="cbFields">

          <template #cell(icon)="data">
            <b-icon
                icon="dash-circle" variant="danger"
                v-model="data.item.radio1"
                :value="true"
                @click="removeWriteSbList(data.item)"
            ></b-icon>
          </template>
          <template #cell(name)="data">
            <div class="ellipsis-name td-box-name">
              {{ data.value }}
            </div>
          </template>
        </b-table>
        <!--        상병추가모달-->
        <writeSb-model/>
      </div>
    </div>
  </div>
</template>

<script>

import WriteSbModel from "@/components/doctor/WriteSbModal.vue";
import {mapState, mapMutations} from 'vuex';


export default {
  name: "DoctorHistoryWrite",
  components: {WriteSbModel},

  mounted() {
    // document.addEventListener('DOMContentLoaded', () => {
    //   // INSERT CODE HERE
    //   this.setTest(this.items)
    // });
  },

  data() {
    return {
      sbFields: [
        {key: 'main', label: '주상병'},
        {key: 'sub', label: '부상병'},
        {key: 'code', label: '코드'},
        // {key: 'radio1', label: '선택1', thClass: 'text-center'},
        {key: 'name', label: '명칭'},
        {key: 'icon', label: 'remove'},
      ],
      cbFields: [
        {key: 'code', label: '코드'},
        {key: 'name', label: '명칭'},
        {key: 'dose', label: '용량'},
        {key: 'time', label: '일투수'},
        {key: 'days', label: '일수'},
        {key: 'icon', label: 'remove'},
      ],
    }
  },

  // vuex
  computed: {
    ...mapState('doctor',
        ['sbList', 'writeSbList']
    )
  },
  methods: {
    ...mapMutations('doctor', {
      removeWriteSbList: 'removeWriteSbList',
    }),
    // 상병테이블 remove icon
    removeSbButton(item) {
      this.removeSbList(item);
    },

    // 라디오버튼 누를때 값 가져오는거
    handleRadioChange(item) {
      // 선택된 값을 가져와서 저장합니다.
      if (item.main) {
        this.selectedItems.push({id: item.id, value: 'main'});
      } else if (item.sub) {
        this.selectedItems.push({id: item.id, value: 'sub'});
      } else {
        this.selectedItems = this.selectedItems.filter((i) => i.id !== item.id);
      }
    },

  }
}
</script>

<style lang="scss" scoped>
.symptom-box {
  display: flex;
}

.symptomInfo-box {
  flex-grow: 1;
}

.writeImg-box {
  width: 150px;
}

.writeImg-box img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px; /* 요소의 최대 너비를 지정합니다. */
}

.td-box-name {
  width: 200px;
}

</style>
