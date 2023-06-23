<template>
  <div class="temp">
    <NavBar v-if="isNavFooter"/>
    <Home v-if="homeByStep"/>
    <PatientList v-if="patientListByStep"/>
    <PatientHistoryList v-if="patientHistoryListByStep"/>
    <PatientHistory v-if="patientHistoryByStep"/>
    <MobileMessage v-if="messageByStep"/>
    <Waiting v-if="settingByStep"/>
<!--    <Admin v-if="adminByStep"/>-->

    <Camera v-if="cameraByStep"/>
    <Gallery v-if="galleryByStep"/>
    <PhotoDetail v-if="photoDetailByStep"/>
    <Footer v-if="isNavFooter"/>
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

// 카메라
import Camera from "@/components/mobile/doctor/Camera.vue";
import Gallery from "@/components/mobile/doctor/Gallery.vue";
import PhotoDetail from "@/components/mobile/doctor/PhotoDetail.vue";
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

    Camera,
    Gallery,
    PhotoDetail
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
    patientHistoryListByStep() {
      return this.nextStep===3 ? true : false;
    },
    patientHistoryByStep() {
      return this.nextStep===4 ? true : false;
    },
    messageByStep() {
      return this.nextStep===5 ? true : false;
    },
    settingByStep() {
      return this.nextStep===6 ? true : false;
    },
    cameraByStep() {
      return this.nextStep===8 ? true : false;
    },
    galleryByStep() {
      return this.nextStep===9 ? true : false;
    },
    photoDetailByStep() {
      return this.nextStep===10 ? true : false;
    },

    isNavFooter(){
      return this.nextStep<8 ? true : false;
    },
  }
}
</script>

<style scoped>

</style>