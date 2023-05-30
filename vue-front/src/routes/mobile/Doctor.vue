<template>
  <div class="temp">
    <NavBar/>
    <Home v-if="homeByStep"/>
    <PatientList v-if="patientListByStep"/>
    <PatientHistoryList v-if="patientHistoryByStep"/>
    <PatientHistory v-if="false"/>
    <MobileMessage v-if="messageByStep"/>
    <Waiting v-if="settingByStep"/>
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
import MobileMessage from "@/components/mobile/doctor/Message.vue";
import {mapState} from "vuex";

export default {
  name: "MobileDoctor",
  components: {
    MobileMessage,
    NavBar,
    Footer,
    Home,
    PatientList,
    PatientHistoryList,
    PatientHistory,
    Waiting,
  },
  computed: {
    ...mapState('mobileDoctor',
        ['nextStep']
    ),
    homeByStep() {
      return this.nextStep===1 ? true : false;
    },
    patientListByStep() {
      return this.nextStep===2 ? true : false;
    },
    patientHistoryByStep() {
      return this.nextStep===3 ? true : false;
    },
    messageByStep() {
      return this.nextStep===4 ? true : false;
    },
    settingByStep() {
      return this.nextStep===5 ? true : false;
    },
  }
}
</script>

<style scoped>

</style>