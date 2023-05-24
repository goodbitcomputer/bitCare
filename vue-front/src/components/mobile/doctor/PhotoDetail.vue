<template>
  <div id="detail-box">
    <div class="gallery-util-box d-flex">
      <button @click="galleryBtn">
        <b-icon icon="chevron-left" variant=""></b-icon>
      </button>
      <div class="flex-grow-1"></div>
      <button @click="selectBtn">{{ this.selectBtnMsg }}</button>
    </div>
    <div id="photo" class="photo d-flex">
      <img class="align-middle" :src="photo.url">
    </div>
  </div>
</template>

<script>
import {mapMutations, mapState} from "vuex";

export default {
  name: "PhotoDetail",
  mounted() {
    this.divFix();
  },
  computed: {
    ...mapState('mobileDoctor',
        ['photo', 'newCameraList']
    ),
    selectBtnMsg() {
      if (this.photo.storeSelect === true) {
        return "선택취소";
      } else {
        return "선택";
      }
    }
  },
  methods: {
    ...mapMutations('mobileDoctor', {
      setCameraNextStep: 'setCameraNextStep',
      setPhoto: 'setPhoto',
    }),
    divFix() {
      let div = document.getElementById('detail-box');
      let divHeight = div.offsetHeight;
      let windowWidth = window.innerWidth;
      let windowHeight = window.innerHeight;
      let photoDiv = document.getElementById('photo');

      let height = windowHeight - 60 + 'px';
      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      } else {
        div.style.height = 'auto';
      }
      if (windowWidth > windowHeight) {
        photoDiv.style.width = height;
        photoDiv.style.height = height;
      }
    },
    galleryBtn() {
      this.setCameraNextStep(2);
    },
    selectBtn() {
      if(this.photo.storeSelect ===false){
        this.photo.storeSelect = true;
      }else{
        this.photo.storeSelect = false;
      }
      this.setCameraNextStep(2);
    }
  }
}
</script>

<style scoped>
#detail-box {
  background: rgba(12, 11, 9, 0.8);
}

.gallery-util-box {
  background: white;
  height: 50px;
  padding: 0 10px;
}

.photo {
  width: 100%;
  height: calc(100vh - 60px);
  aspect-ratio: auto 1/1;
  overflow: hidden;
  margin: auto;
}

.photo img {
  width: inherit;
  object-fit: contain;
  /*object-fit: cover;*/
}
</style>