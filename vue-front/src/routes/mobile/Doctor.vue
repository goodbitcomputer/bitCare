<template>
  <div class="temp">
    <NavBar/>
    <Home v-if="false"/>
    <PatientList v-if="false"/>
    <PatientOne/>
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
import PatientOne from "@/components/mobile/doctor/PatientOne.vue";
import Waiting from "@/components/mobile/doctor/Wating.vue";

export default {
  name: "MobileDoctor",
  components: {
    NavBar,
    Footer,
    Home,
    PatientList,
    PatientOne,
    Waiting,
  }
}
</script>

<style scoped>

</style>