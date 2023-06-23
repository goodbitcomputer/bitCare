<template>
  <!-- ======= Hero Section ======= -->
  <section id="mobile-doctor">
    <div class="title-border-box d-flex">
      <span style="font-size: 1.2em; font-weight: 700; flex-grow: 1">진료 기록</span>
    </div>
    <div id="mobileDoctor-box" class="">
      <div class="util">
        <div class="title">
          <span class="font-weight-bold">{{ $store.state.receipt.patientData.name }}</span>
          <span>cn.{{ $store.state.receipt.patientData.id }}</span>
          <span>{{ dateMsg($store.state.receipt.patientData.birth) }}</span>
          <span>{{ ageMsg($store.state.receipt.patientData.birth) }}세</span>
          <span>{{ $store.state.receipt.patientData.gender }}</span>
        </div>
      </div>
      <div class="history-detail-box border-box">
        <!--      진료기록title-->
        <div>
          <span>진료기록</span>
          <span>[{{ dateMsg($store.state.receipt.historyData.entryDate) }}]</span>
        </div>
        <!--      메모-->
        <div class="border-box">
          <span class="font-weight-bold">진료메모</span>
          <div class="ellipsis"
               style="padding: 5px; border: 1px solid #DBDFE5; border-radius: 5px; margin-bottom: 5px;">
            <div class="ellipsis" v-html="$store.state.receipt.historyData.memo"></div>
          </div>

          <!--          <div class="memoInfo-box">-->
          <!--            <textarea ref="memoEditor" id="memoEditor" name="memo"></textarea>-->
          <!--          </div>-->

        </div>
        <!--      신체계측/바이탈-->
        <div class="border-box">
          <span class="font-weight-bold">신체계측/바이탈</span>
          <div class="table-wrapper">
            <b-table hover :items="$store.state.receipt.physicalData" :fields="pyFields" small>
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
          <span>{{ $store.state.receipt.historyData.visit }}</span>
          <span>외래진료</span>
          <span>{{ $store.state.receipt.historyData.dept }}</span>
          <span>{{ $store.state.receipt.historyData.name }}</span>
        </div>


        <!--      진료기록 상세정보-->
        <!--      이미지 list-->
        <div class="border-box">
          <span class="font-weight-bold">이미지</span>
          <div class="img-list-box text-center">
            <div class="swiper-box">
              <swiper :options="swiperOptions" ref="swiper">
                <swiper-slide v-for="(slide, index) in addImgListLogic" :key="index">
                  <!-- 슬라이드 내용 -->
                  <img :src="imgUrl(slide)" alt="Slide Image">
                </swiper-slide>

                <!-- 네비게이션 버튼 -->
                <div class="swiper-button-prev" slot="button-prev"></div>
                <div class="swiper-button-next" slot="button-next"></div>
              </swiper>

              <!--              <div v-if="!isImgEmpty">-->
              <!--                <span>촬영부위: </span><span>{{this.bodyCategoryName}}</span>-->
              <!--              </div>-->
              <!--              <div class="imageCategory-box" v-if="isImgEmpty">-->
              <!--                <b-dropdown-->
              <!--                    :text="imageCategoryMsg"-->
              <!--                    block-->
              <!--                    variant="primary"-->
              <!--                    class="btn-outline-dark mt-2 mb-2"-->
              <!--                    menu-class="w-100"-->
              <!--                >-->
              <!--                  <b-dropdown-item v-for="(item) in bodyCategoryList" :key="item.id" @click="dropClick(item)">-->
              <!--                    {{ item.categoryName }}-->
              <!--                  </b-dropdown-item>-->
              <!--                </b-dropdown>-->
              <!--              </div>-->
            </div>
          </div>
        </div>
        <!--      증상-->
        <div class="border-box">
          <span class="font-weight-bold">증상</span>
          <div style="padding: 5px; border: 1px solid #DBDFE5; border-radius: 5px;"
               v-html="$store.state.receipt.historyData.symptomDetail">
          </div>

        </div>
        <!--      상병-->
        <div class="border-box">
          <span class="font-weight-bold">상병</span>
          <div>
            <div class="table-wrapper">
              <b-table hover :items="$store.state.receipt.sbList" :fields="sbFields"
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
              <b-table hover :items="$store.state.receipt.cbList" thead-class="hidden_header"
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

      <!--      <div class="d-flex">-->
      <!--        <button class="btn btn-primary col mr-2" @click="photography">사진촬영</button>-->
      <!--        <button class="btn btn-primary col" @click="saveBtn">전송</button>-->
      <!--      </div>-->
    </div>
  </section><!-- End Hero -->

</template>

<script>
// import { Swiper, SwiperSlide } from "vue-awesome-swiper";
// import 'swiper/swiper.scss';


import {mapMutations, mapState} from "vuex";
import axios from "axios";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";

// const Swal = window.Swal;

export default {
  name: "noReceipt",
  data() {
    return {
      // 메모 에디터 데이터
      memoEditor: "",
      // 신체계측/바이탈 테이블 로직
      pyFields: [
        {key: 'height', label: '키'},
        {key: 'weight', label: '체중'},
        {key: 'bpSystolic', label: '혈압(수축)'},
        {key: 'bpDiastolic', label: '혈압(이완)'},
        {key: 'temperature', label: '체온'},
      ],

      // 이미지
      imageList: [],

      // 상병 테이블 로직
      sbFields: [
        {key: 'main', label: '주/부', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
      ],

      specialData: "주상병", // 특정 속성 데이터

      // 처방 테이블 로직


      /*
    * -----------------------------------------------------------------
    * 2023.05.18 홍사범
    * imageSwiper 생성
    * -----------------------------------------------------------------
    */
      //   swiper
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

      saveFileList: [],
      bodyCategoryList: [],
      bodyCategoryId: "", // 저장할때 쓸꺼
      bodyCategoryName: "", // 저장할때 쓸꺼
    }
  },
  mounted() {
    // this.divHeightFix();
    // this.getBodyCategoryData();
    // this.ckeditorSetting();
  },
  computed: {
    ...mapState('receipt',
        ['patientData', 'historyData', 'sbList', 'cbList', 'imgList', 'physicalData', 'savePhotoList']
    ),
    /*
    * -----------------------------------------------------------------
    * 2023.05.18 홍사범
    * imageCategoryButton text
    * -----------------------------------------------------------------
    */
    imageCategoryMsg() {
      if (this.bodyCategoryName === "") return "촬영 부위 선택";
      else return this.bodyCategoryName;
    },
    // image swiper 데이터 리스트
    addImgListLogic() {
      let addImgList = [];
      this.imgList.forEach((item) => {
        addImgList.push(item);
      });
      this.savePhotoList.forEach((item) => {
        addImgList.push(item);
      });
      return addImgList;
    },
    isImgEmpty() {
      if (this.imgList.length === 0) {
        return true;
      } else {
        return false;
      }
    }
  },
  methods: {
    ...mapMutations('receipt', {
      setPatientData: 'setPatientData',
      setHistoryData: 'setHistoryData',
      setSbList: 'setSbList',
      setCbList: 'setCbList',
      setImgList: 'setImgList',
      setPhysicalData: 'setPhysicalData',
    }),
    searchBodyCategory() {
      this.bodyCategoryList.forEach((item) => {
        if (item.id === this.imgList[0].categoryId) {
          this.bodyCategoryId = item.id;
          this.bodyCategoryName = item.categoryName;
        }
      })
    },
    // divHeightFix() {
    //   let div = document.getElementById('mobileDoctor-box');
    //   let divHeight = div.offsetHeight;
    //   let windowHeight = window.innerHeight;
    //
    //   if (divHeight < windowHeight) {
    //     div.style.height = '100vh';
    //   } else {
    //     div.style.height = 'auto';
    //   }
    // },
    // 상병 테이블 로직
    rowClass(item) {
      // 해당 행의 name 속성이 specialData와 일치하는지 확인
      if (item.sb === this.specialData) {
        return "font-weight-bold ellipsis"; // 굵은 글꼴 클래스 반환
      }
    },
    setMemo() {
      this.memoEditor.setData(this.historyData.memo);
    },
    // ckEditor 세팅
    ckeditorSetting() {
      ClassicEditor.create(document.querySelector('#memoEditor'), {
        contentCss: this.contentCss,
        toolbar: [
          // 'heading',
          // '|',
          'bold',
          'italic',
          'link',
          'bulletedList',
          '|',
          'undo',
          'redo',
          // '|',
          // 'imageUpload',
          // 'alignment',
          // 'numberedList',
          // 'imageInsert',
          // 'blockQuote',
          // '|',
          // 'ckfinder',
        ],
      }).then(newEditor => {
        this.memoEditor = newEditor;
        this.setMemo();
      }).catch((error) => {
        console.error(error);
      });
    },

    // // 사진촬영버튼
    // photography() {
    //   this.setPhotoListToNewCameraList(this.savePhotoList);
    //   this.setNextStep(8);
    // },

    // // 저장 버튼
    // saveBtn() {
    //   this.saveFileList = [];
    //
    //   if (this.savePhotoList.length === 0) {
    //     Swal.fire({
    //       icon: 'error',
    //       title: '실패 !!!',
    //       text: '추가된 사진이 없습니다.',
    //       showConfirmButton: false,
    //       timer: 1000
    //     })
    //   } else if (this.bodyCategoryId === "") {
    //     Swal.fire({
    //       icon: 'error',
    //       title: '실패 !!!',
    //       text: '이미지 카테고리를 선택해주세요.',
    //       showConfirmButton: false,
    //       timer: 1000
    //     })
    //   } else {
    //     this.savePhotoList.forEach((item) => {
    //       this.saveFileList.push(item.file)
    //     })
    //
    //     let formData = new FormData();
    //     formData.append("bodyCategoryId", new Blob([JSON.stringify(this.bodyCategoryId)], {type: "application/json"}));
    //     formData.append("historyId", new Blob([JSON.stringify(this.historyData.id)], {type: "application/json"}));
    //     formData.append("memo", new Blob([JSON.stringify(this.memoEditor.getData())], {type: "application/json"}));
    //     for (let i = 0; i < this.saveFileList.length; i++) {
    //       formData.append("uploadFiles", this.saveFileList[i]);// 키,값으로 append
    //     }
    //     return axios.post('/mobile/doctor/photoSave_proc', formData, {
    //       headers: {
    //         'Content-Type': 'multipart/form-data'
    //       }
    //     }).then((response) => {
    //       if (response.data === true) {
    //         Swal.fire({
    //           icon: 'success',
    //           title: '성공 !!!',
    //           text: 'file save',
    //           showConfirmButton: false,
    //           timer: 1000
    //         }).then(() => {
    //           this.setNextStep(3);
    //         })
    //       } else {
    //         Swal.fire({
    //           icon: 'error',
    //           title: '실패 !!!',
    //           text: 'fail',
    //           showConfirmButton: false,
    //           timer: 1000
    //         }).then(() => {
    //           this.setNextStep(3);
    //         })
    //       }
    //     }).catch(function (error) {
    //       console.log(error);
    //     })
    //   }
    // },

    imgUrl(item) {
      if (item.url === undefined) {
        return item.imagePath;
      } else {
        return item.url;
      }
    },
    getBodyCategoryData() {
      return axios.get('/search/getBodyCategoryData', {}).then(response => {
        let list = response.data;
        this.bodyCategoryList = list;
      }).then(()=>{
        if (!this.isImgEmpty) {
          this.searchBodyCategory();
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    // // imageCategory item 선택시 data 변경
    // dropClick(item) {
    //   this.bodyCategoryName = item.categoryName;
    //   this.bodyCategoryId = item.id;
    // },

    dateMsg(item) {
      let dateTemp = new Date(item)
      let year = dateTemp.getFullYear();
      let month = dateTemp.getMonth() + 1;
      if (month < 10) {
        month = "0" + month;
      }
      let date = dateTemp.getDate();

      return year + "." + month + "." + date;
    },
    ageMsg(item) {
      let dateTemp = new Date(item)
      let dateNow = new Date();

      let tempYear = dateTemp.getFullYear();
      let nowYear = dateNow.getFullYear();
      let age = parseInt(nowYear) - parseInt(tempYear) + 1;
      return age;
    },

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
  /*background: url("/public/assets/img/main/hero-bg.jpg");*/
  /*background-color: #A1C7E0;*/
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

/*#mobile-doctor:before {*/
/*  content: "";*/
/*  background: rgba(0, 0, 0, 0.6);*/
/*  position: absolute;*/
/*  bottom: 0;*/
/*  top: 0;*/
/*  left: 0;*/
/*  right: 0;*/
/*}*/

#mobileDoctor-box {
  /*padding-top: 70px;*/
  /*padding-bottom: 65px;*/
  margin: 0 3px;
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

.doctor-info span {
  font-size: 12px;
  font-weight: 600;
  white-space: nowrap;
}

.doctor-info span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 3px 0px 3px;
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 600px; /* 요소의 최대 너비를 지정합니다. */
}

.swiper-box {
  display: inline-block;
  padding: 0;
}

@media (min-width: 1200px ) {
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

@media (max-width: 1200px) {

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

@media (max-width: 590px) {
  #mobileDoctor-box {
    /*padding-top: 90px;*/
  }
}


</style>