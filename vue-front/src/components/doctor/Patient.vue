<template>
  <div class="Patient-box border-box">
    <div style="border-right: 1px solid #DBDFE5;">
      <div>
        <span>박은희</span>
        <span>cn.6</span>
        <button>...</button>
      </div>
      <div class="patient-info">
        <span>50904-2******</span>
        <span>70세</span>
        <span>여</span>
        <span>010-1234-5678</span>
      </div>
      <div style="border-top: 1px solid #DBDFE5;">
        <p>접수메모</p>
        <p></p>
      </div>
    </div>
    <div>
      <p>진료메모</p>
      <textarea></textarea>
    </div>
  </div>
</template>

<script>
export default {
  name: "DoctorPatient"
}
</script>

<style lang="scss" scoped>
.Patient-box {
  display: flex;
}
.patient-info {
  flex-wrap: nowrap;
}
.patient-info span {
  font-size: 10px;
  // 글자 줄바꿈 안되게 함.
  white-space: nowrap;
}
.patient-info span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 3px 0px 3px;
}
</style>