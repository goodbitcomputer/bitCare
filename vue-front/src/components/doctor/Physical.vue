<template>
  <div class="border-box">
    <span class="font-weight-bold">신체계측/바이탈</span>
    <div class="table-wrapper">
      <b-table hover :items="cbItems" :fields="fields" small>
        <template #cell(height)="data">
          <b-form-input type="number" v-model="data.item.height"></b-form-input>
        </template>
        <template #cell(weight)="data">
          <b-form-input type="number" v-model="data.item.weight"></b-form-input>
        </template>
        <template #cell(bpSystolic)="data">
          <b-form-input type="number" v-model="data.item.bpSystolic"></b-form-input>
        </template>
        <template #cell(bpDiastolic)="data">
          <b-form-input type="number" v-model="data.item.bpDiastolic"></b-form-input>
        </template>
        <template #cell(temperature)="data">
          <b-form-input type="number" v-model="data.item.temperature"></b-form-input>
        </template>
      </b-table>
    </div>
    {{cbItems}}
  </div>
</template>

<script>
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
      cbItems: [
        {height: "", weight: "", bpSystolic: "", bpDiastolic: "", temperature: ""},
      ],
    }
  },
  computed: {
    msg() {
      return this.cbItems;
    }
  }
}
</script>

<style lang="scss" scoped>

</style>