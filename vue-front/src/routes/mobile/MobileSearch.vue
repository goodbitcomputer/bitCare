<template>
  <div>
    <nav-bar/>
    <MobileSearchPatient class="search-patient-box"/>
  </div>
</template>

<script>
import MobileSearchPatient from "@/components/mobile/search/Patient.vue";
import NavBar from "@/components/mobile/NavBar.vue";

export default {
  name: "MobileSearch",
  components: {NavBar, MobileSearchPatient},

}
</script>

<style scoped>
.search-patient-box {
  margin-top: 70px;
}
</style>