<template>
  <div class="container">
    <div class="waiting border-box"><waiting/></div>
    <div class="history border-box">
      <patient/>
      <physical/>
      <history/>
    </div>
    <div class="write border-box"><history-write/></div>
  </div>
</template>

<script>
import Waiting from "@/components/doctor/Waiting.vue";
import Patient from "@/components/doctor/Patient.vue";
import Physical from "@/components/doctor/Physical.vue";
import History from "@/components/doctor/History.vue";
import HistoryWrite from "@/components/doctor/HistoryWrite.vue";
export default {
  name: "DoctorView",
  components: {
    Waiting,
    Patient,
    Physical,
    History,
    HistoryWrite,

  }
}
</script>
<style lang="scss" scoped>

  .container {
    padding-top: 20px;
    display: flex;
  }
  btn {
    color: #0b2e13;
    background-color: #0b2e13;
  }
  .waiting {
    width: 20%;
  }
  .history {
    width: 45%;
  }
  .write {
    width: 55%;
  }
</style>