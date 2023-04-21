<template>
  <div class="border-box">
    <span>신체계측/바이탈</span>
    <div class="table-wrapper">
      <b-table hover :items="cbItems" :fields="fields" small>
        <template #cell(code)="data">
          <div class="">
            {{ data.value }}
          </div>
        </template>
        <template #cell(name)="data">
          <div class="ellipsis-name">
            {{ data.value }}
          </div>
        </template>
      </b-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "DoctorPhysical",
  data() {
    return {
      // 신체계측/바이탈 테이블 로직
      fields: [
        {key: 'height', label: '키'},
        {key: 'weight', label: '체중'},
        {key: 'bpSystolic', label: '햘압(수축)'},
        {key: 'bpDiastolic', label: '혈압(이완)'},
        {key: 'temperature', label: '체온'},
      ],
      cbItems: [
        {height: '178', weight: '73', bpSystolic: '128', bpDiastolic: "87", temperature: "36.5"},
      ],
    }
  }
}
</script>

<style lang="scss" scoped>

</style>