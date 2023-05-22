<template>
  <div>
    <Camera v-if="cameraByStep"/>
    <Gallery v-if="galleryByStep"/>
    <PhotoDetail v-if="photoDetailByStep"/>
  </div>
</template>

<script>
import Camera from "@/components/mobile/doctor/Camera.vue";
import Gallery from "@/components/mobile/doctor/Gallery.vue";
import PhotoDetail from "@/components/mobile/doctor/PhotoDetail.vue";
import {mapState} from "vuex";
export default {
  name: "MobileCameraRouter",
  components: {
    Camera,
    Gallery,
    PhotoDetail
  },
  computed: {
    ...mapState('mobileDoctor',
        ['nextStep']
    ),
    cameraByStep() {
      return this.nextStep===1 ? true : false;
    },
    galleryByStep() {
      return this.nextStep===2 ? true : false;
    },
    photoDetailByStep() {
      return this.nextStep===3 ? true : false;
    },
  }
}
</script>

<style scoped>

</style>