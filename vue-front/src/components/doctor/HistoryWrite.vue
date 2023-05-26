<template>
  <div>
    <div class="border-box">
      <span style="font-size: 1.2em; font-weight: 700">진료 기록 작성</span>
    </div>
    <div class="border-box">
      <b-dropdown size="sm" :text="visit" class="m-0">
        <b-dropdown-item-button @click="visitBtn('초진')">초진</b-dropdown-item-button>
        <b-dropdown-item-button @click="visitBtn('재진')">재진</b-dropdown-item-button>
      </b-dropdown>
    </div>
    <!--    증상-->
    <div class="border-box">
      <div class="d-flex">
        <span class="font-weight-bold flex-grow-1">증상</span>
        <button @click="editorBtn">편집</button>
      </div>
      <div class="symptom-box">
        <div class="symptomInfo-box">
          <textarea ref="symptomEditor" id="symptomEditor" name="symptom"></textarea>
        </div>
        <div class="writeImg-box">
          <div class="img-list-box">
            <swiper :options="swiperOptions" ref="swiper">
              <swiper-slide v-for="(slide, index) in writeSlides" :key="index">
                <!-- 슬라이드 내용 -->
                <img :src="slide.imagePath" alt="Slide Image">
              </swiper-slide>

              <!-- 네비게이션 버튼 -->
              <div class="swiper-button-prev" slot="button-prev"></div>
              <div class="swiper-button-next" slot="button-next"></div>
            </swiper>
          </div>
        </div>
      </div>
    </div>

    <!--    상병-->
    <div class="border-box">
      <span class="font-weight-bold">상병</span>
      <div>
        <b-table class="text-nowrap" :items="writeSbList" :fields="sbFields" small>
          <template #cell(main)="data">
            <div class="text-center">
              <input
                  class="radio-btn"
                  type="radio"
                  v-model="data.item.main"
                  :value="true"
              >
            </div>
          </template>
          <template #cell(sub)="data">
            <div class="text-center">
              <input
                  class="radio-btn"
                  type="radio"
                  v-model="data.item.main"
                  :value="false"
              >
            </div>
          </template>
          <template #cell(icon)="data">
            <div class="text-center">
              <b-icon
                  icon="dash-circle" variant="danger"
                  @click="removeWriteSbList(data.item)"
              ></b-icon>
            </div>
          </template>
          <template #cell(name)="data">
            <div class="ellipsis-name td-box-name">
              {{ data.value }}
            </div>
          </template>
        </b-table>
        <!--        상병추가모달-->
        <writeSb-model/>
      </div>
    </div>
    <!--    처방-->
    <div class="border-box">
      <span class="font-weight-bold">처방</span>
      <div>
        <b-table :items="writeCbList" :fields="cbFields" small>
          <template #cell(dose)="data">
            <input class="cb-input" type="number" v-model="data.item.dose">
          </template>
          <template #cell(time)="data">
            <input class="cb-input" type="number" v-model="data.item.time">
          </template>
          <template #cell(days)="data">
            <input class="cb-input" type="number" v-model="data.item.days">
          </template>

          <template #cell(icon)="data">
            <div class="text-center">
              <b-icon
                  icon="dash-circle" variant="danger"
                  @click="removeWriteCbList(data.item)"
              ></b-icon>
            </div>
          </template>
          <template #cell(name)="data">
            <div class="ellipsis-name td-box-name">
              {{ data.value }}
            </div>
          </template>
        </b-table>
        <!--        상병추가모달-->
        <writeCb-model/>
      </div>
    </div>
    <div class="d-flex justify-content-end">
      <b-button class="w-25 m-1" variant="dark" @click="initBtn">초기화</b-button>
      <b-button class="w-25 m-1" variant="secondary">저장</b-button>
      <b-button class="w-25 m-1" variant="primary" @click="upsertBtn">진료 완료</b-button>
    </div>
  </div>
</template>

<script>

import WriteSbModel from "@/components/doctor/WriteSbModal.vue";
import WriteCbModel from "@/components/doctor/WriteCbModal.vue";
import {mapState, mapMutations} from 'vuex';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import axios from "axios";

const Swal = window.Swal;


export default {
  name: "DoctorHistoryWrite",
  components: {
    WriteSbModel,
    WriteCbModel,
  },
  data() {
    return {
      // 증상 에디터 데이터
      symptomEditor: "",
      // 초진, 재진
      visit: "초진",

      sbFields: [
        {key: 'main', label: '주상병'},
        {key: 'sub', label: '부상병'},
        {key: 'code', label: '코드'},
        {key: 'name', label: '명칭'},
        {key: 'icon', label: 'remove'},
      ],
      cbFields: [
        {key: 'code', label: '코드'},
        {key: 'name', label: '명칭'},
        {key: 'dose', label: '용량'},
        {key: 'time', label: '일투수'},
        {key: 'days', label: '일수'},
        {key: 'icon', label: 'remove'},
      ],

      //   swiper
      writeSlides: [
        {imagePath: '/assets/img/testimonials/testimonials-1.jpg'},
        {imagePath: '/assets/img/testimonials/testimonials-1.jpg'},
        {imagePath: '/assets/img/testimonials/testimonials-1.jpg'},
        {imagePath: '/assets/img/testimonials/testimonials-1.jpg'},
        {imagePath: '/assets/img/testimonials/testimonials-1.jpg'},
        {imagePath: '/assets/img/testimonials/testimonials-1.jpg'},
        {imagePath: '/assets/img/testimonials/testimonials-1.jpg'},
      ],
      swiperOptions: {
        slidesPerView: 1, // 한번에 보여줄 슬라이드 개수
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


      //login data
      login: '',
      // 진료 기록 데이터
      writeData: [],


    }
  },
  mounted() {
    // document.addEventListener('DOMContentLoaded', () => {
    //   // INSERT CODE HERE
    //   this.setTest(this.items)
    // });

    ClassicEditor.create(document.querySelector('#symptomEditor'), {
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
      this.symptomEditor = newEditor;
      this.setSymptomEditor(newEditor);
    }).catch((error) => {
      console.error(error);
    });
  },

  // vuex
  computed: {
    ...mapState('doctor',
        ['sbList', 'writeSbList', 'writeCbList', 'waitingData', 'physicalData', 'memoEditor', 'writeSbList', 'writeCbList', 'historyId']
    ),

  },
  methods: {
    ...mapMutations('doctor', {
      // 상병테이블 remove icon  write상병테이블 한줄 삭제
      removeWriteSbList: 'removeWriteSbList',
      // 처방테이블 remove icon   write처방테이블 한줄 삭제
      removeWriteCbList: 'removeWriteCbList',
      setSymptomEditor: 'setSymptomEditor',
      initWriteHistory: 'initWriteHistory',
    }),

    initBtn() {
      this.initWriteHistory();
    },

    visitBtn(item) {
      this.visit = item;
    },

    // editorBtn
    editorBtn() {
      this.$router.push('/doctor/editor');
    },
    upsertBtn() {
      // console.log("ClassicEditor: "+this.symptomEditor.getData());

      console.log(this.physicalData[0].bpSystolic);
      console.log(this.physicalData[0].bpDiastolic);
      console.log(this.physicalData[0].height);
      console.log(this.physicalData[0].weight);
      console.log(this.physicalData[0].temperature);
      console.log(this.symptomEditor.getData());
      console.log(this.memoEditor.getData());
      console.log(this.visit);

      this.historyUpsertSend();


    },

    // 접수
    historyUpsertSend() {

      let upsertData = new FormData();
      upsertData.append('id', this.historyId);
      upsertData.append('patientId', this.waitingData.patientId);
      upsertData.append('deptId', this.waitingData.deptId);
      upsertData.append('memo', this.memoEditor.getData());
      upsertData.append('bpSystolic', this.physicalData[0].bpSystolic);
      upsertData.append('bpDiastolic', this.physicalData[0].bpDiastolic);
      upsertData.append('height', this.physicalData[0].height);
      upsertData.append('weight', this.physicalData[0].weight);
      upsertData.append('temperature', this.physicalData[0].temperature);
      upsertData.append('symptomDetail', this.symptomEditor.getData());
      upsertData.append('visit', this.visit);

      return axios.post('/doctor/historyUpsert_proc', {
            id: this.historyId,
            patientId: this.waitingData.patientId,
            deptId: this.waitingData.deptId,
            memo: this.memoEditor.getData(),
            bpSystolic: this.physicalData[0].bpSystolic,
            bpDiastolic: this.physicalData[0].bpDiastolic,
            height: this.physicalData[0].height,
            weight: this.physicalData[0].weight,
            temperature: this.physicalData[0].temperature,
            symptomDetail: this.symptomEditor.getData(),
            visit: this.visit,
            writeSbList: this.writeSbList,
            writeCbList: this.writeCbList,
          }, {},
      ).then(response => {
        console.log(response.data.status);
        console.log(typeof response.data.status)
        if (response.data.status === 'fail') {
          Swal.fire({
            icon: 'error',
            title: 'error',
            text: '실패',
            showConfirmButton: false,
            timer: 1000
          })
        }
        if (response.data.status === 'success') {
          // 환자데이터 초기화
          this.initWriteHistory();
          Swal.fire({
            icon: 'success',
            title: 'success',
            text: '성공',
            showConfirmButton: false,
            timer: 1000
          })
        }
      }).catch(function (error) {
        console.log(error);
      });


    }
  }
}
</script>

<style lang="scss" scoped>
.symptom-box {
  display: flex;
}

.symptomInfo-box {
  flex-grow: 1;
}

.writeImg-box {
  width: 150px;
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px; /* 요소의 최대 너비를 지정합니다. */
}

.td-box-name {
  width: 200px;
}

.radio-btn {
  margin: 0 auto;
}

.cb-input {
  margin: 0 auto;
  width: 35px;
  height: 20px;
  font-size: 10px;
}

// swiper
.img-list-box .swiper-container {
  /* calc- 계산해주는 함수 */
  //width: calc(50% * 3 + 20px);
  /*height: 100px;*/
  /*position: absolute;*/
  /*top: 40px;*/
  //left: 50%;
  //margin-left: calc((50% * 3 + 20px) / -2);

  overflow: hidden;
}

.img-list-box .swiper-slide img {
  width: 100%;
  object-fit: contain;
}

.swiper-button-prev:after {
  font-size: 25px;
}

.swiper-button-next:after {
  font-size: 25px;
}

// input type number 증감 없애기
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}


</style>
