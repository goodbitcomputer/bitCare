<template>
  <div id="gallery-box">
    <div class="gallery-util-box d-flex">
      <button @click="cameraBtn">
        <b-icon icon="chevron-left" variant=""></b-icon>
      </button>
      <div class="flex-grow-1"></div>
      <button class="list-count">{{ this.listLength }}</button>
      <button @click="gallerySave">저장</button>
    </div>
    <div class="slide-list-box" data-aos="zoom-in">
      <swiper :options="swiperOptions" ref="swiper" >
        <swiper-slide class="swiper-slide-active" v-for="(item, index) in this.selectList" :key="index" data-aos="zoom-in-left">
          <!-- 슬라이드 내용 -->
          <div class="img-box">
            <img :src="item.url" alt="Slide Image">
          </div>
        </swiper-slide>

        <!-- 네비게이션 버튼 -->
        <!--        <div class="swiper-button-prev" slot="button-prev"></div>-->
        <!--        <div class="swiper-button-next" slot="button-next"></div>-->
      </swiper>
    </div>
    <div class="photo-list-box">
      <div ref="photo" class="photo" v-for="item in this.newCameraList" :key="item.id"
           :class="{'select' : item.storeSelect}" @click="selectBtn(item)">
        <div style="overflow: hidden;width: 100%;aspect-ratio: auto 1/1; ">
          <img :src="item.url">
          <div class="expansion" @click="photoSelect(item)">
            <b-icon icon="arrows-angle-expand" variant=""
                    style="width:20px; height:20px; border: 1px solid black; padding: 2px"></b-icon>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapMutations, mapState} from "vuex";
import axios from "axios";
const Swal = window.Swal;

export default {
  name: "mobileGallery",
  data() {
    return {
      saveFileList: [],
      swiperOptions: {
        initialSlide: 99999999,
        slidesPerView: 8, // 한번에 보여줄 슬라이드 개수
        spaceBetween: 10, // 슬라이드 사이 여백
        centeredSlides: false, // 1번 슬라이드가 가운데 보이기
        navigation: false,
        // loop: true,
        // pagination: {
        //   el: '.swiper-pagination'
        // },
        // Swiper 설정
        // navigation: {
        //   nextEl: '.swiper-button-next',
        // prevEl: '.swiper-button-prev',
        // },
      },
    }
  },
  mounted() {
    this.divHeightFix();
  },
  computed: {
    ...mapState('mobileDoctor',
        ['newCameraList']
    ),
    selectList() {
      let list = [];
      this.newCameraList.forEach((item) => {
        if (item.storeSelect === true) {
          list.push(item);
        }
      })
      return list;
    },
    listLength() {
      let count = 0;
      this.newCameraList.forEach((item) => {
        if (item.storeSelect === true) {
          count++;
        }
      })
      return count;
    },
  },
  methods: {
    ...mapMutations('mobileDoctor', {
      setNewCameraList: 'setNewCameraList',
      setCameraNextStep: 'setCameraNextStep',
      setPhoto: 'setPhoto',
    }),
    divHeightFix() {
      let div = document.getElementById('gallery-box');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;

      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      } else {
        div.style.height = 'auto';
      }
    },
    cameraBtn() {
      this.setCameraNextStep(1);
    },
    photoSelect(item) {
      this.setPhoto(item);
      this.setCameraNextStep(3);
    },
    selectBtn(item) {
      if (item.storeSelect === false) {
        item.storeSelect = true;
      } else {
        item.storeSelect = false;
      }
    },
    setSaveFileList(){
      this.saveFileList = [];
      this.newCameraList.forEach((item) => {
        if (item.storeSelect === true) {
          this.saveFileList.push(item.file)
        }
      })
    },
    gallerySave() {
      this.setSaveFileList();

      let formData = new FormData();
      // formData.append("files", this.saveFileList);
      for (let i = 0; i < this.saveFileList.length; i++) {
        console.log(this.saveFileList[i]);
        formData.append("uploadFiles", this.saveFileList[i]);//키,값으로 append
      }
      return axios.post('/mobile/doctor/photoSave_proc', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((response) => {
        if (response.data === true) {
          Swal.fire({
            icon: 'success',
            title: '성공 !!!',
            text: 'file save',
            showConfirmButton: false,
            timer: 1000
          }).then(() => {
            // this.$router.push('m.home');
          })
        }
      }).catch(function (error) {
        console.log(error);
      })
    },
  }
}
</script>

<style scoped>
#gallery-box {
  background: rgba(12, 11, 9, 0.8);
}

.gallery-util-box {
  background: white;
  height: 50px;
  padding: 0 10px;
}

.photo-list-box {
  display: flex;
  flex-wrap: wrap;
  /*background: rgba(12, 11, 9, 0.8);*/
}

.select:before {
  content: "";
  border: 4px solid #FFCC00;
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
  margin: 1px;
}

.photo {
  position: relative;
  padding: 1px;
}

.expansion {
  width: 20px;
  height: 20px;
  left: 5px;
  bottom: 5px;
  position: absolute;
}

.slide-list-box .img-box {
  aspect-ratio: auto 1/1;
  overflow: hidden;
  margin: 10px 0;
  border-radius: 3px;
}

.slide-list-box .img-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.list-count {
  pointer-events: none;
  color: #FFCC00;
  margin: 5px;
}
@media (min-width: 995px ) {
  .photo {
    width: calc(100% / 5);
    aspect-ratio: auto 1/1;
    overflow: hidden;
  }

  .photo img {
    width: 500px;
    height: 500px;
    object-fit: cover;
    /*object-fit: cover;*/
  }
}

@media (max-width: 995px) {
  .photo {
    width: calc(100% / 5);
    aspect-ratio: auto 1/1;
    overflow: hidden;
  }

  .photo img {
    width: 332px;
    height: 332px;
    object-fit: cover;
  }
}

@media (max-width: 720px) {
  .photo {
    width: calc(100% / 4);
    aspect-ratio: auto 1/1;
    overflow: hidden;
  }

  .photo img {
    width: 240px;
    height: 240px;
    object-fit: cover;
  }
}

@media (max-width: 540px) {
  .photo {
    width: calc(100% / 3);
    aspect-ratio: auto 1/1;
    overflow: hidden;
  }

  .photo img {
    width: 200px;
    height: 200px;
    object-fit: cover;
  }
}

@media (max-width: 351px) {
  .photo {
    width: calc(100% / 3);
    aspect-ratio: auto 1/1;
    overflow: hidden;
  }

  .photo img {
    width: 150px;
    height: 150px;
    object-fit: cover;
  }
}
</style>