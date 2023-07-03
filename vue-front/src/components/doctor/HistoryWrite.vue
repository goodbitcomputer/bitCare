<template>
  <div>
    <!--    <div class="border-box">-->
    <!--      <span style="font-size: 1.2em; font-weight: 700">진료 기록 작성</span>-->
    <!--    </div>-->

    <!--    <div v-if="isSelectEmpty" class="empty-list-box border-box">-->
    <!--      <div class="empty-img-box">-->
    <!--        <img src="@/assets/img/empty-box.png">-->
    <!--      </div>-->
    <!--    </div>-->
    <div v-if="!isSelectEmpty">
      <div class="border-box">
        <b-dropdown :text="visitMsg" class="m-0" size="sm">
          <b-dropdown-item-button @click="visitBtn('초진')">초진</b-dropdown-item-button>
          <b-dropdown-item-button @click="visitBtn('재진')">재진</b-dropdown-item-button>
        </b-dropdown>
      </div>
      <!--    증상-->
      <div class="border-box">
        <div class="d-flex">
          <span class="font-weight-bold flex-grow-1">증상</span>
          <div class="d-flex" style="cursor:pointer" @click="editorBtn">
            <div class="edit-img-box">
              <img src="@/assets/img/edit.png">
            </div>
            <span class="justify-content-center font-weight-bold" style="margin-left: 2px">편집</span>
          </div>
        </div>
        <div class="symptom-box">
          <div class="symptomInfo-box">
            <textarea id="symptomEditor" ref="symptomEditor" name="symptom"></textarea>
          </div>
          <div class="writeImg-box">
            <div class="img-list-box">
              <swiper ref="swiper" :options="swiperOptions">
                <swiper-slide v-for="(slide, index) in writeImgList" :key="index">
                  <!-- 슬라이드 내용 -->
                  <img :src="slide.imagePath" alt="Slide Image">
                </swiper-slide>

                <!-- 네비게이션 버튼 -->
                <div slot="button-prev" class="swiper-button-prev"></div>
                <div slot="button-next" class="swiper-button-next"></div>
              </swiper>
            </div>
          </div>
        </div>
      </div>

      <!--    상병-->
      <div class="border-box">
        <span class="font-weight-bold">상병</span>
        <div>
          <b-table :fields="sbFields" :items="writeSbList" class="text-nowrap" small>
            <template #cell(main)="data">
              <div class="text-center">
                <input
                    v-model="data.item.main"
                    :value="true"
                    class="radio-btn"
                    type="radio"
                >
              </div>
            </template>
            <template #cell(sub)="data">
              <div class="text-center">
                <input
                    v-model="data.item.main"
                    :value="false"
                    class="radio-btn"
                    type="radio"
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
          <b-table :fields="cbFields" :items="writeCbList" small>
            <template #cell(dose)="data">
              <input v-model="data.item.dose" class="cb-input" type="number" min="0" maxlength="3"/>
            </template>
            <template #cell(time)="data">
              <input v-model="data.item.time" class="cb-input" type="number" min="0" maxlength="3"/>
            </template>
            <template #cell(days)="data">
              <input v-model="data.item.days" class="cb-input" type="number" min="0" maxlength="3"/>
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
        <b-button class="w-25 m-1" variant="secondary" @click="upsertBtn">{{ isInsertOrUpdateCheck }}</b-button>
        <b-button class="w-25 m-1" variant="primary" @click="completedBtn">진료 완료</b-button>
      </div>
    </div>
  </div>
</template>

<script>

import WriteSbModel from "@/components/doctor/WriteSbModal.vue";
import WriteCbModel from "@/components/doctor/WriteCbModal.vue";
import {mapActions, mapMutations, mapState} from 'vuex';
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
      // ckEditor 다시 나타날떄 유무 체크
      ckeditor: 0,

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

  },
  created() {
    this.$nextTick(() => {
      if (!this.isSelectEmpty) {
        if (this.ckeditor == 0) {
          this.ckeditorSetting();
          this.ckeditor = this.ckeditor + 1;
        }
      } else {
        this.ckeditor = 0;
      }
    })
  },
// 환자데이터가 활성화되면 ckeditor 세팅하기
  updated() {
    this.$nextTick(() => {
      if (!this.isSelectEmpty) {
        if (this.ckeditor == 0) {
          this.ckeditorSetting();
          this.ckeditor = this.ckeditor + 1;
        }
      } else {
        this.ckeditor = 0;
      }
    })
  },
  // vuex
  computed: {
    ...mapState('doctor',
        ['sbList', 'writeSbList', 'writeCbList', 'waitingData', 'physicalData', 'memoEditor', 'writeSbList', 'writeCbList', 'writeImgList', 'historyId', 'visit']
    ),
    // 선택된 환자 데이터가 있는지 확인
    isSelectEmpty() {
      return this.waitingData === "" ? true : false;
    },
    isInsertOrUpdateCheck() {
      return this.historyId === -1 ? "신규 저장" : "수정";
    },
    // 초진, 재진 드롭박스 텍스트 변경
    visitMsg() {
      if (this.visit === "초진") return "초진";
      else return "재진";
    },
  },
  methods: {
    ...mapMutations('doctor', {
      // 상병테이블 remove icon  write상병테이블 한줄 삭제
      removeWriteSbList: 'removeWriteSbList',
      // 처방테이블 remove icon   write처방테이블 한줄 삭제
      removeWriteCbList: 'removeWriteCbList',
      setSymptomEditor: 'setSymptomEditor',
      initWriteHistory: 'initWriteHistory',
      setVisit: 'setVisit',
      setWaitingData: 'setWaitingData',
    }),
    ...mapMutations('editor', {
      setEditorWaitingData: 'setWaitingData',
    }),
    ...mapActions('doctor', {
      getHistoryList: 'getHistoryList'
    }),
    initBtn() {
      this.initWriteHistory();
    },
    ckeditorSetting() {
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
    visitBtn(item) {
      this.setVisit(item);
    },

    // editorBtn
    // 2023.06.08 유동준
    // patientId, historyId값 같이 넘겨주기
    editorBtn() {
      console.log("선택한 환자 번호: " + this.waitingData.patientId);
      console.log("선택한 진료 기록 번호: " + this.historyId);
      this.setEditorWaitingData(this.waitingData);
      this.$router.push({
        path: '/doctor/editor/selectByPatientIdAndHistoryId',
        query: {
          patientId: this.waitingData.patientId,
          historyId: this.historyId
        }
      });
    },

    upsertBtn() {
      this.historyUpsertSend();
    },

    // 접수
    historyUpsertSend() {
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
            writeImgList: this.writeImgList,
          }, {},
      ).then(response => {
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
          // historyPage 새로고침
          this.$EventBus.$emit('refresh');

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
    },
    // 진료완료 버튼
    completedBtn() {
      let waitingData = this.waitingData;
      waitingData.state = '진료완료';

      axios.post('/doctor/completed_proc', waitingData).then(() => {
        // historyPage의 historyData 초기화
        this.$EventBus.$emit('completedRefresh');
      }).catch(function (error) {
        console.log(error);
      });
    },
  }
}
</script>

<style lang="scss" scoped>
.edit-img-box {
  width: 15px;
  margin: 0 auto;
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.edit-img-box img {
  width: inherit;
  object-fit: contain;
}

.empty-box {
  height: 140px;
  display: flex;
}

.empty-img-box {
  width: 70px;
  margin: 0 auto;
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.empty-img-box img {
  width: inherit;
  object-fit: contain;
}

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
