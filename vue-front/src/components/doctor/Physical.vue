<template>
  <div class="border-box" v-if="!isSelectEmpty">
    <span class="font-weight-bold">신체계측/바이탈</span>
    <div class="table-wrapper">
      <b-table hover :items="physicalData" :fields="fields" small>
        <template #cell(height)="data">
          <b-form-input class="physical-input" type="number" v-model="data.item.height"></b-form-input>
        </template>
        <template #cell(weight)="data">
          <b-form-input class="physical-input" type="number" v-model="data.item.weight"></b-form-input>
        </template>
        <template #cell(bpSystolic)="data">
          <b-form-input class="physical-input" type="number" min="0" maxlength="4"
                        v-model="data.item.bpSystolic"></b-form-input>
        </template>
        <template #cell(bpDiastolic)="data">
          <b-form-input class="physical-input" type="number" min="0" maxlength="4"
                        v-model="data.item.bpDiastolic"></b-form-input>
        </template>
        <template #cell(temperature)="data">
          <b-form-input class="physical-input" type="number" min="0" maxlength="4"
                        v-model="data.item.temperature"></b-form-input>
        </template>
      </b-table>
    </div>
    <!--    <input type="text" :value="number">-->
    <!--    <input type="text" :value="handleOnInput(number2)">-->
  </div>
</template>

<script>

import {mapState} from "vuex";

export default {
  name: "DoctorPhysical",
  data() {
    return {
      // 신체계측/바이탈 테이블 로직
      fields: [
        {key: 'height', label: '키(cm)'},
        {key: 'weight', label: '체중(kg)'},
        {key: 'bpSystolic', label: '혈압(수축)'},
        {key: 'bpDiastolic', label: '혈압(이완)'},
        {key: 'temperature', label: '체온'},
      ],


    }
  },
  computed: {
    ...mapState('doctor',
        ['physicalData', 'waitingData']
    ),
    // 선택된 환자 데이터가 있는지 확인
    isSelectEmpty() {
      return this.waitingData === "" ? true : false;
    },
    msg() {
      return this.cbItems;
    },

  },
  watch: {

  },
  methods: {
    handleOnInput(event) {
      console.log("event");
      console.log(event);

      let regex = /[^0-9]/g;
      event = event.replace(regex, '');
      console.log(event);


      // if (event.length > 4) {
      //   event = event.substr(0, 4);
      //   console.log('length > 4');
      //   console.log(event);
      // }

      console.log("physicalData");
      console.log(this.physicalData);
      return event
    },

  },
}
</script>

<style lang="scss" scoped>
.physical-input {
  margin: 0 auto;
}

input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}
</style>