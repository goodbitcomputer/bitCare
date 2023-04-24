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
        <b-table :items="sbList" :fields="fields">
          <template #cell(radio1)="data">
            <b-form-radio
                v-model="data.item.radio1"
                :value="true"
                @change="handleRadioChange(data.item)"
            ></b-form-radio>
          </template>
          <template #cell(radio2)="data">
            <b-form-radio
                v-model="data.item.radio1"
                :value="false"
                @change="handleRadioChange(data.item)"
            ></b-form-radio>
          </template>
        </b-table>
        <button @click="addRow">마지막 행 추가</button>
        <!--        상병추가모달-->
        <writeSb-model/>

        <p>선택된 항목: {{ selectedItems }}</p>
      </div>
    </div>
    <!--    처방-->
    <div class="border-box">
      <p>처방</p>
      <b-table>
        <template #cell(radio1)="data">
          <b-form-radio
              v-model="data.item.radio1"
              :value="true"
              @change="handleRadioChange(data.item)"
          ></b-form-radio>
        </template>
        <template #cell(radio2)="data">
          <b-form-radio
              v-model="data.item.radio1"
              :value="false"
              @change="handleRadioChange(data.item)"
          ></b-form-radio>
        </template>
      </b-table>
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
    document.addEventListener('DOMContentLoaded', () => {
      // INSERT CODE HERE
      this.setTest(this.items)
    });
  },

  data() {
    return {
      selectedItems: [],
      fields: [
        {key: 'name', label: '이름'},
        {key: 'age', label: '나이'},
        {key: 'gender', label: '성별'},
        // {key: 'radio1', label: '선택1', thClass: 'text-center'},
        {key: 'radio1', label: '선택1'},
        {key: 'radio2', label: '선택2'},
      ],
      items: [
        {id: 1, name: 'John', age: 28, gender: 'Male', radio1: false, radio2: true},
        {id: 2, name: 'Jane', age: 35, gender: 'Female', radio1: true, radio2: false},
        {id: 3, name: 'Mike', age: 22, gender: 'Male', radio1: false, radio2: true},
        {id: 4, name: 'Emily', age: 29, gender: 'Female', radio1: true, radio2: false}
      ]
    }
  },

  // vuex
  computed: {
    ...mapState('doctor',
      ['sbList']
    )
  },
  methods: {
    ...mapMutations('doctor', {
      setTest: 'setSbList',

    }),


    handleRadioChange(item) {
      // 선택된 값을 가져와서 저장합니다.
      if (item.radio1) {
        this.selectedItems.push({id: item.id, value: 'radio1'});
      } else if (item.radio2) {
        this.selectedItems.push({id: item.id, value: 'radio2'});
      } else {
        this.selectedItems = this.selectedItems.filter((i) => i.id !== item.id);
      }
    },
    addRow() {
      // 추가할 행 객체 생성
      const newRow = {
        id: 5,
        name: 'Tom',
        age: 30,
        gender: 'Female',
        radio1: true,
        radio2: false
      };

      // 생성한 객체를 배열에 추가
      this.items.push(newRow);
    }
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


</style>