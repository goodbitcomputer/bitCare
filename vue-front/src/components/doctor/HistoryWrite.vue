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
        <b-table :items="items" :fields="fields">
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

        <p>선택된 항목: {{ selectedItems }}</p>
      </div>
    </div>
<!--    처방-->
    <div class="border-box">
      <p>처방</p>
      <b-table></b-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "DoctorHistoryWrite",
  data() {
    return {
      selectedItems: [],
      fields: [
        { key: 'name', label: '이름' },
        { key: 'age', label: '나이' },
        { key: 'gender', label: '성별' },
        { key: 'radio1', label: '선택1', thClass: 'text-center' },
        { key: 'radio2', label: '선택2', thClass: 'text-center' },
      ],
      items: [
        { id: 1, name: 'John', age: 28, gender: 'Male', radio1: true, radio2: false },
        { id: 2, name: 'Jane', age: 35, gender: 'Female', radio1: false, radio2: true },
        { id: 3, name: 'Mike', age: 22, gender: 'Male', radio1: false, radio2: true },
        { id: 4, name: 'Emily', age: 29, gender: 'Female', radio1: true, radio2: false }
      ]
    }
  },
  methods: {
    handleRadioChange(item) {
      // 선택된 값을 가져와서 저장합니다.
      if (item.radio1) {
        this.selectedItems.push({ id: item.id, value: 'radio1' });
      } else if (item.radio2) {
        this.selectedItems.push({ id: item.id, value: 'radio2' });
      } else {
        this.selectedItems = this.selectedItems.filter((i) => i.id !== item.id);
      }
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