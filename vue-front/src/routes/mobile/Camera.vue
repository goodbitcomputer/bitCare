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
        ['cameraNextStep']
    ),
    cameraByStep() {
      return this.cameraNextStep===1 ? true : false;
    },
    galleryByStep() {
      return this.cameraNextStep===2 ? true : false;
    },
    photoDetailByStep() {
      return this.cameraNextStep===3 ? true : false;
    },
  }
}
</script>

<style scoped>

</style>