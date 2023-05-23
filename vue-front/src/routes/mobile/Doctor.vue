<template>
  <div class="temp">
    <NavBar/>
    <Home v-if="true"/>
    <PatientList v-if="true"/>
    <PatientHistoryList v-if="true"/>
    <PatientHistory v-if="true"/>
    <Waiting v-if="false"/>

    <Footer/>
  </div>
</template>

<script>
import "/public/assets/css/style.scss";
import NavBar from "@/components/mobile/NavBar.vue";
import Footer from "@/components/mobile/doctor/Footer.vue";
import Home from "@/components/mobile/doctor/Home.vue";
import PatientList from "@/components/mobile/doctor/PatientList.vue";
import PatientHistoryList from "@/components/mobile/doctor/PatientHistoryList.vue";
import PatientHistory from "@/components/mobile/doctor/PatientHistory.vue";
import Waiting from "@/components/mobile/doctor/Wating.vue";

export default {
  name: "MobileDoctor",
  components: {
    NavBar,
    Footer,
    Home,
    PatientList,
    PatientHistoryList,
    PatientHistory,
    Waiting,
  }
}
</script>

<style scoped>

</style>