<template>
  <!-- ======= Hero Section ======= -->
  <section id="mobile-doctor">
    <div id="mobileDoctor-box" class="container" data-aos="fade-up">
      <div class="util" data-aos="fade-up">
        <div class="title">
          <span class="font-weight-bold">박은희</span>
          <span>cn.6</span>
          <span>1951.09.04</span>
          <span>70세</span>
          <span>여</span>
        </div>
      </div>
      <div class="history-detail-box border-box">
        <!--      진료기록title-->
        <div>
          <span>진료기록</span>
          <span>[2022.01.06]</span>
        </div>
        <!--      신체계측/바이탈-->
        <div class="border-box">
          <span class="font-weight-bold">신체계측/바이탈</span>
          <div class="table-wrapper">
            <b-table hover :items="pyItems" :fields="pyFields" small>
              <template #cell(code)="data">
                <div class="">
                  {{ data.value }}
                </div>
              </template>
              <template #cell(name)="data">
                <div class="ellipsis-name">
                  {{ data.value }}
                </div>
              </template>
            </b-table>
          </div>
        </div>
        <!--      의사정보-->
        <div class="doctor-info">
          <span>건강보험</span>
          <span>초진</span>
          <span>외래진료</span>
          <span>내과</span>
          <span>백지영</span>
        </div>


        <!--      진료기록 상세정보-->
        <!--      이미지 list-->
        <div class="border-box">
          <span class="font-weight-bold">이미지</span>
          <div class="img-list-box">
            <swiper :options="swiperOptions" ref="swiper">
              <swiper-slide v-for="(slide, index) in slides" :key="index">
                <!-- 슬라이드 내용 -->
                <img :src="slide.image" alt="Slide Image">
              </swiper-slide>

              <!-- 네비게이션 버튼 -->
              <div class="swiper-button-prev" slot="button-prev"></div>
              <div class="swiper-button-next" slot="button-next"></div>
            </swiper>
          </div>
          <!--          <b-button class="btn col mt-1" variant="primary">촬영 부위 선택</b-button>-->
          <div class="imageCategory-box">
            <b-dropdown
                :text="imageCategoryMsg"
                block
                variant="primary"
                class="btn-outline-dark mt-2 mb-2"
                menu-class="w-100"
            >
              <b-dropdown-item v-for="(item) in imageList" :key="item.id" @click="dropClick(item)">{{ item.dept }}
              </b-dropdown-item>
            </b-dropdown>
          </div>
        </div>
        <!--      증상-->
        <div class="border-box">
          <span class="font-weight-bold">증상</span>
          <div style="padding: 5px; border: 1px solid #DBDFE5; border-radius: 5px;">
            <p style="white-space: normal">이틀전부터 목 불편함. 목이 칼칼하다. 헛기침</p>
          </div>

        </div>
        <!--      상병-->
        <div class="border-box">
          <span class="font-weight-bold">상병</span>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="sbItems" :fields="sbFields"
                       :tbody-tr-class="rowClass" small>
                <template #cell(sb)="data">
                  <div class="ellipsis-sb td-box-sb">
                    {{ data.value }}
                  </div>
                </template>
                <template #cell(code)="data">
                  <div class="ellipsis-code td-box-code">
                    {{ data.value }}
                  </div>
                </template>
                <template #cell(name)="data">
                  <div class="ellipsis-name td-box-name">
                    {{ data.value }}
                  </div>
                </template>
              </b-table>
            </div>
          </div>
        </div>
        <!--      처방-->
        <div class="border-box">
          <span class="font-weight-bold">처방</span>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="cbItems" thead-class="hidden_header"
                       :tbody-tr-class="rowClass" small>
                <template #cell(code)="data">
                  <div class="">
                    {{ data.value }}
                  </div>
                </template>
                <template #cell(name)="data">
                  <div class="ellipsis-name">
                    {{ data.value }}
                  </div>
                </template>
              </b-table>
            </div>
          </div>
        </div>
      </div>

      <div class="d-flex">
        <button class="btn btn-primary col mr-2" @click="photography">사진촬영</button>
        <button class="btn btn-primary col">전송</button>
      </div>
    </div>
  </section><!-- End Hero -->

</template>

<script>
// import { Swiper, SwiperSlide } from "vue-awesome-swiper";
// import 'swiper/swiper.scss';


export default {
  name: "MobileHome",
  data() {
    return {
      // 신체계측/바이탈 테이블 로직
      pyFields: [
        {key: 'height', label: '키'},
        {key: 'weight', label: '체중'},
        {key: 'bpSystolic', label: '혈압(수축)'},
        {key: 'bpDiastolic', label: '혈압(이완)'},
        {key: 'temperature', label: '체온'},
      ],
      pyItems: [
        {height: '178', weight: '73', bpSystolic: '128', bpDiastolic: "87", temperature: "36.5"},
      ],

      // 이미지
      imageList: [],
      imageCategory: "",


      // 상병 테이블 로직
      sbFields: [
        {key: 'sb', label: '주/부', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
      ],
      sbItems: [
        {sb: '주상병', code: 'Dickerson', name: 'Macdonaldsdsadassssssssssssssssdadadsadsadsasadasdd'},
        {sb: '부상병', code: 'Larsen', name: 'Shaw'},
        {sb: '부상병', code: 'Geneva', name: 'Wilson'},
        {sb: '부상병', code: 'Jami', name: 'Carney'}
      ],
      specialData: "주상병", // 특정 속성 데이터

      // 처방 테이블 로직
      // cbFields: ["sb", "code", "name"],
      cbItems: [
        {code: '645700880', name: '세토펜정80밀리그램(어쩌구저쩌구ㅇㄴㅁㅇㅁㅇㄴㅁㅇㄴㅁ)', dose: '1', time: "1", days: "1"},
        {
          code: '653403810',
          name: 'DickersonDickersonDickersonDickersonDickersonDickersonDickerson',
          dose: '1',
          time: "1",
          days: "1"
        },
        {code: '653403812', name: '아르도민캡슐(에르도스도르도르말말마람람)', dose: '1', time: "1", days: "1"},
        {code: '645700883', name: '알게이트정(120밀리그람)', dose: '1', time: "1", days: "1"},
      ],

      /*
    * -----------------------------------------------------------------
    * 2023.05.18 홍사범
    * imageSwiper 생성
    * -----------------------------------------------------------------
    */
      //   swiper
      slides: [
        {image: '/assets/img/testimonials/testimonials-1.jpg'},
        {image: '/assets/img/testimonials/testimonials-1.jpg'},
        {image: '/assets/img/testimonials/testimonials-1.jpg'},
        {image: '/assets/img/testimonials/testimonials-1.jpg'},
        {image: '/assets/img/testimonials/testimonials-1.jpg'},
        {image: '/assets/img/testimonials/testimonials-1.jpg'},
        {image: '/assets/img/testimonials/testimonials-1.jpg'},
      ],
      swiperOptions: {
        slidesPerView: 3, // 한번에 보여줄 슬라이드 개수
        spaceBetween: 10, // 슬라이드 사이 여백
        centeredSlides: true, // 1번 슬라이드가 가운데 보이기
        // loop: true,
        pagination: {
          el: '.swiper-pagination'
        },
        // Swiper 설정
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev',
        },
      },
    }
  },
  mounted() {
    this.divHeightFix();

  },
  computed: {
    /*
    * -----------------------------------------------------------------
    * 2023.05.18 홍사범
    * imageCategoryButton text
    * -----------------------------------------------------------------
    */
    imageCategoryMsg() {
      if (this.imageCategory === "") return "촬영 부위 선택";
      else return this.imageCategory;
    }
  },
  methods: {
    divHeightFix() {
      let div = document.getElementById('mobileDoctor-box');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;

      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      } else {
        div.style.height = 'auto';
      }
    },
    // 상병 테이블 로직
    rowClass(item) {
      // 해당 행의 name 속성이 specialData와 일치하는지 확인
      if (item.sb === this.specialData) {
        return "font-weight-bold ellipsis"; // 굵은 글꼴 클래스 반환
      }
    },

    // imageCategory item 선택시 data 변경
    dropClick(item) {
      this.image = item.dept;
      this.imageId = item.id;
    },

    // 사진촬영버튼
    photography() {
      this.$router.push("/mobile/camera");
    }

  },
}
</script>

<style scoped>
section {
  padding: 0;
}

#mobile-doctor {
  width: 100%;
  /*height: 100vh;*/
  background: url("/public/assets/img/main/hero-bg.jpg");
  background-size: cover;
  /* 배경이미지 반복여부 */
  background-repeat: no-repeat;
  /* 배경의 위치는 그 세션의 정가운데에 위치 */
  background-position: center;
  /* 더이상 요소가 스크롤될때 같이 스크롤되지않고 이미지가 뷰포트부분에 고정, 화면이 스크롤 되더라도 같이 움직이지않는구조로 만듬 */
  background-attachment: fixed;
  position: relative;
  padding: 0;
}

#mobile-doctor:before {
  content: "";
  background: rgba(0, 0, 0, 0.2);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

#mobileDoctor-box {
  padding-top: 150px;
  padding-bottom: 65px;
}

#mobileDoctor-box * {
  /*color: #cccccc;*/
  white-space: nowrap;
}

.util {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid transparent;*/
  border-radius: 5px;
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

.util .title span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 5px 0px 5px;
}

.history-detail-box {
  background-color: white;
  /*background: rgba(12, 11, 9, 0.7);*/
}

.border-box {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid transparent;*/
  border-radius: 5px;
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 600px; /* 요소의 최대 너비를 지정합니다. */
}

@media (min-width: 995px ) {
  .img-list-box .swiper-container {
    /* calc- 계산해주는 함수 */
    width: calc(200px * 3 + 20px);
    /*height: 100px;*/
    /*position: absolute;*/
    /*top: 40px;*/
    left: 50%;
    margin-left: calc((200px * 3 + 20px) / -2);

    overflow: hidden;
  }

  .img-list-box .swiper-slide img {
    width: 200px;
    height: 200px;
    object-fit: contain;
  }
  .imageCategory-box {
    width: 500px;
    margin: 0 auto;
  }
}

@media (max-width: 995px) {
  #mobileDoctor-box {
    padding-top: 110px;
  }

  .ellipsis-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 300px; /* 요소의 최대 너비를 지정합니다. */
  }

  .img-list-box .swiper-container {
    /* calc- 계산해주는 함수 */
    width: calc(150px * 3 + 20px);
    /*height: 100px;*/
    /*position: absolute;*/
    /*top: 40px;*/
    left: 50%;
    margin-left: calc((150px * 3 + 20px) / -2);

    overflow: hidden;
  }

  .img-list-box .swiper-slide img {
    width: 150px;
    height: 150px;
    object-fit: contain;
  }
}

@media (max-width: 720px) {
  .ellipsis-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 150px; /* 요소의 최대 너비를 지정합니다. */
  }

  .img-list-box .swiper-container {
    /* calc- 계산해주는 함수 */
    width: calc(100px * 3 + 20px);
    /*height: 100px;*/
    /*position: absolute;*/
    /*top: 40px;*/
    left: 50%;
    margin-left: calc((100px * 3 + 20px) / -2);

    overflow: hidden;
  }

  .img-list-box .swiper-slide img {
    width: 100px;
    height: 100px;
    object-fit: contain;
  }
}

@media (max-width: 540px) {
  .ellipsis-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 80px; /* 요소의 최대 너비를 지정합니다. */
  }
}

@media (max-width: 351px) {
  #mobileDoctor-box {
    padding-top: 150px;
  }

  body * {
    font-size: 8px;
  }

  .ellipsis-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 50px; /* 요소의 최대 너비를 지정합니다. */
  }

  .img-list-box .swiper-button-prev {
    left: 50%;
    margin-left: -100px;
  }

  .img-list-box .swiper-button-next {
    right: 50%;
    margin-right: -100px;
  }
}

@media (max-width: 290px) {
  .ellipsis-name {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 20px; /* 요소의 최대 너비를 지정합니다. */
  }
}

/*image swiper */
.img-list-box {
  /*position: relative;*/
  /*height: 100px;*/
  width: 100%;
  overflow: hidden;

}

.img-list-box .swiper-slide {
  opacity: .5;
  transition: opacity 1s;
  /*position: relative;*/
}

.img-list-box .swiper-slide-active {
  opacity: 1;
}

/*.img-list-box .swiper-pagination {*/
/*  bottom: 40px;*/
/*  left: 0;*/
/*  right: 0;*/
/*}*/

/*.img-list-box .swiper-pagination .swiper-pagination-bullet {*/
/*  background-color: transparent;*/
/*  !*background-image: url("../images/promotion_slide_pager.png");*!*/
/*  width: 12px;*/
/*  height: 12px;*/
/*  margin-right: 6px;*/
/*  outline: none;*/
/*}*/

/*.img-list-box .swiper-pagination .swiper-pagination-bullet:last-child {*/
/*  margin-right: 0;*/
/*}*/

/*.img-list-box .swiper-pagination .swiper-pagination-bullet-active {*/
/*  !*background-image: url("../images/promotion_slide_pager_on.png");*!*/
/*}*/

/*.img-list-box .swiper-button-prev,*/
/*.img-list-box .swiper-button-next {*/
/*  width: 40px;*/
/*  height: 40px;*/
/*  border: 2px solid #333;*/
/*  border-radius: 50%;*/
/*  position: absolute;*/
/*  top: 35px;*/
/*  z-index: 1;*/
/*  cursor: pointer;*/
/*  outline: none;*/
/*  display: flex;*/
/*  justify-content: center;*/
/*  align-items: center;*/
/*  transition: .4s;*/
/*}*/

/*.img-list-box .swiper-prev {*/
/*  left: 50%;*/
/*  margin-left: -130px;*/
/*}*/

/*.img-list-box .swiper-next {*/
/*  right: 50%;*/
/*  margin-right: -130px;*/
/*}*/

/*.img-list-box .swiper-prev:hover,*/
/*.img-list-box .swiper-next:hover {*/
/*  background-color: #333;*/
/*  color: #fff;*/
/*}*/


</style>