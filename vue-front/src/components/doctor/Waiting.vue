<template>
  <div>
    <div class="border-box">
      <span style="font-size: 1.2em; font-weight: 700">대기 환자</span>
    </div>
    <div class="waiting-info-box border-box">


      <div>
        <span class="font-weight-bold">박은희 </span>
        <span>외래진료</span>
      </div>
      <div class="patient-info">
        <span>cn.6 </span>
        <span>1951.09.04 </span>
        <span>70세 </span>
        <span>여</span>
        <button>진료중</button>
      </div>
      <div>
        <span>감기증상, 약처방위해 내원</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DoctorWait"
}
</script>

<style lang="scss" scoped>
#sidebar-1 {
  position: static !important;
  display: block !important;
}
.waiting-info-box span, p{
  white-space: nowrap;
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