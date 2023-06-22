<template>
  <div class="history-box">
    <div class="title-border-box d-flex">
      <span style="font-size: 1.2em; font-weight: 700; flex-grow: 1">진료 기록</span>
      <div class="refresh-img-box" style="cursor:pointer" @click="refreshBtn">
        <img src="@/assets/img/refresh.png">
      </div>
    </div>

    <!--    patient 리스트-->
<!--    <div class="empty-patient-box border-box" v-if="isEmpty">-->
<!--      <div class="empty-img-box">-->
<!--        <img src="@/assets/img/empty-box.png">-->
<!--      </div>-->
<!--    </div>-->
    <div class="border-box" v-if="!isEmpty">
      <div style="display: flex;align-items: center;">
        <span class="font-weight-bold">{{ this.waitingData.name }}</span>
        <span style="flex-grow: 1">cn.{{ this.waitingData.patientId }}</span>
        <button>...</button>
      </div>
      <div class="patient-info">
        <span>{{ identityNumberMsg }}</span>
        <span>{{ ageMsg }}세</span>
        <span>{{ this.waitingData.gender }}</span>
        <span>{{ phonePadMsg }}</span>
      </div>
      <div style="border-top: 1px solid #DBDFE5;">
        <p class="font-weight-bold">진료메모</p>
        <div
            v-html="$store.state.nurse.historyData.memo">
          >
        </div>
      </div>
    </div>

    <!--      history 리스트-->
    <div class="empty-list-box border-box" v-if="isListEmpty">
      <div class="empty-img-box">
        <img src="@/assets/img/empty-box.png">
      </div>
    </div>
    <div class="d-flex" v-if="!isListEmpty">
      <div class="waiting-list-box">
        <div v-for="(item, index) in historyList" :key="item.id">
          <div class="waiting-box main-border-box" :class="{'history-select' : (!isSelectEmpty && (index === selectedIndex) ? true:false)}"
               @click="selectHistoryBtn(item, index)">
            <div>
              <span>{{ dateMsg(item.entryDate) }}</span>
            </div>
            <div>
              <span>{{ item.dept }}</span>
            </div>
            <div>
              <span>{{ item.visit }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="empty-box border-box flex-grow-1" v-if="isSelectEmpty">
        <div class="empty-img-box">
          <img src="@/assets/img/empty-box.png">
        </div>
      </div>
      <div class="history-detail-box border-box" v-if="!isSelectEmpty">
        <!--      진료기록title-->
        <div class="d-flex">
          <span>진료기록</span>
          <span class="flex-grow-1">[{{ dateMsg($store.state.nurse.historyData.entryDate) }}]</span>
        </div>
        <!--      신체계측/바이탈-->
        <div>
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
          <span>{{ $store.state.nurse.historyData.visit }}</span>
          <span>외래진료</span>
          <span>{{ $store.state.nurse.historyData.dept }}</span>
          <span>{{ $store.state.nurse.historyData.name }}</span>
        </div>
        <!--      진료기록 상세정보-->
        <!--      이미지 list-->
        <div class="border-box">
          <span class="font-weight-bold">이미지</span>
          <div class="img-list-box">
            <swiper :options="swiperOptions" ref="swiper">
              <swiper-slide v-for="(slide, index) in slides" :key="index">
                <!-- 슬라이드 내용 -->
                <img :src="slide.imagePath" alt="Slide Image">
              </swiper-slide>

              <!-- 네비게이션 버튼 -->
              <div class="swiper-button-prev" slot="button-prev"></div>
              <div class="swiper-button-next" slot="button-next"></div>
            </swiper>
          </div>
          <!--          <b-button class="btn col mt-1" variant="primary">촬영 부위 선택</b-button>-->
          <div class="imageCategory-box">

          </div>
        </div>


        <div>
          <span class="font-weight-bold">증상</span>
          <div style="padding: 5px; border: 1px solid #DBDFE5; border-radius: 5px;"
               v-html="$store.state.nurse.historyData.symptomDetail">
          </div>

        </div>
        <!--      상병-->
        <div>
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
        <div>
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
        <!--        &lt;!&ndash;      총진료비&ndash;&gt;-->
        <!--        <div class="d-flex">-->
        <!--          <span class="font-weight-bold flex-grow-1">총진료비</span>-->
        <!--          <span>16,970원</span>-->
        <!--        </div>-->
        <!--        <div class="d-flex">-->
        <!--          <span class="font-weight-bold flex-grow-1">수납금</span>-->
        <!--          <span>5,000원</span>-->
        <!--        </div>-->
      </div>
    </div>
  </div>
</template>

<script>
import {mapActions, mapMutations, mapState} from "vuex";
import axios from "axios";

export default {
  name: "NurseHistory",

  // Note `isActive` is left out and will not appear in the rendered table
  data() {
    return {
      // 선택된 히스토리데이터
      selectHistoryData: "",
      selectSbList: [],
      selectCbList: [],
      selectImgList: [],

      // 신체계측/바이탈 테이블 로직
      pyFields: [
        {key: 'height', label: '키'},
        {key: 'weight', label: '체중'},
        {key: 'bpSystolic', label: '혈압(수축)'},
        {key: 'bpDiastolic', label: '혈압(이완)'},
        {key: 'temperature', label: '체온'},
      ],
      pyItems: [],

      // 상병 테이블 로직
      sbFields: [
        {key: 'main', label: '주/부', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
      ],
      sbItems: [],
      specialData: "주상병", // 특정 속성 데이터

      // 처방 테이블 로직
      // cbFields: ["sb", "code", "name"],
      cbItems: [],

      //   swiper
      slides: [],
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
      selectedIndex: -1,
    }
  },
  mounted() {
    // historyPage의 historyData 초기화
    this.$EventBus.$on('initNurseHistory', () => {
      this.$store.state.nurse.historyData = "";
    });
  },
  computed: {
    ...mapState('nurse',
        ['historyList', 'waitingData']
    ),
    // 진료기록 리스트가 있는지 확인
    isListEmpty() {
      return this.historyList.length === 0 ? true : false;
    },
    // 진료기록 데이터가 있는지 확인
    isSelectEmpty() {
      return this.$store.state.nurse.historyData === "" ? true : false;
    },
    // 대기환자 진료부서id와 진료기록 진료부서id가 같은지 비교
    isCheckUpdate() {
      return this.$store.state.nurse.historyData.deptId === this.waitingData.deptId ? true : false;
    },


    // 환자 정보
    // 주민번호
    identityNumberMsg() {
      if (this.waitingData === "") {
        return ""
      } else {
        let str1 = this.waitingData.identityNumber.slice(0, 6);
        let str2 = this.waitingData.identityNumber.slice(6, 7);
        str1 = str1 + "-" + str2 + "******";
        return str1;
      }
    },
    // 휴대폰번호
    phonePadMsg() {
      if (this.waitingData === "") {
        return ""
      } else {
        let newStr = this.waitingData.phoneNumber.replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, "");
        return newStr;
      }
    },
    // 나이
    ageMsg() {
      if (this.waitingData === "") {
        return ""
      } else {
        let dateTemp = new Date(this.waitingData.birth)
        let dateNow = new Date();

        let tempYear = dateTemp.getFullYear();
        let nowYear = dateNow.getFullYear();
        let age = parseInt(nowYear) - parseInt(tempYear) + 1;
        return age;
      }
    },
    // 환자 데이터가 있는지 확인
    isEmpty() {
      return this.waitingData === "" ? true : false;
    },
  },
  methods: {
    ...mapMutations('nurse', {
      setVuexHistoryData: 'setHistoryData',
      setVisitList: 'setVisitList',
      setReceiptData: 'setReceiptData',
      initHistoryData: 'initHistoryData',
    }),
    ...mapActions('nurse', {
      getHistoryList: 'getHistoryList',
    }),
    // 상병 테이블 로직
    rowClass(item) {
      // 해당 행의 name 속성이 specialData와 일치하는지 확인
      if (item.sb === this.specialData) {
        return "font-weight-bold ellipsis"; // 굵은 글꼴 클래스 반환
      }
    },
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
    // 진료기록리스트에서 특정 진료기록 선택
    selectHistoryBtn(item, index) {
      // 선택시 class에 select 추가
      this.selectedIndex = index;

      axios.post('/doctor/getHistoryAddData', {
        historyId: item.id,
      }).then(response => {
        let diseaseList = JSON.parse(response.data.diseaseList);
        let diagnoseList = JSON.parse(response.data.diagnoseList);
        let imgList = JSON.parse(response.data.imgList);
        this.setSelectSbList(diseaseList);
        this.setSelectCbList(diagnoseList);
        this.setSelectImgList(imgList);
      }).catch(function (error) {
        console.log(error);
      });

      this.setHistoryData(item);
      this.setVuexHistoryData(item);  // vuex에 저장 (수납내역에 사용하기위해)
      this.getVisitData(item.visit);
      this.getReceiptData(item);
      this.pyItems = [];
      this.pyItems.push({
        height: item.height,
        weight: item.weight,
        bpSystolic: item.bpSystolic,
        bpDiastolic: item.bpDiastolic,
        temperature: item.temperature
      });

    },
    // 선택된 진료기록데이터 저장
    setHistoryData(item) {
      this.selectHistoryData = item;
      this.setVuexHistoryData(item);
    },
    setSelectSbList(item) {
      this.selectSbList = [];
      this.sbItems = [];
      let degree = (degreeItem) => {
        return degreeItem === "주상병" ? true : false;
      }
      item.forEach((i) => {
        this.selectSbList.push({main: degree(i.degree), code: i.code, name: i.name, historyId: i.historyId})
        this.sbItems.push({main: i.degree, code: i.code, name: i.name});
      })
    },
    setSelectCbList(item) {
      this.selectCbList = item;
      this.cbItems = [];
      item.forEach((i) => {
        this.cbItems.push({code: i.code, name: i.name, dose: i.dose, time: i.time, days: i.days});
      })
    },
    setSelectImgList(item) {
      this.selectImgList = item;
      this.slides = [];
      item.forEach((i) => {
        this.slides.push({imagePath: i.imagePath})
      })
    },
    updateBtn() {
      let writeObj = {
        history: this.selectHistoryData,
        sbList: this.selectSbList,
        cbList: this.selectCbList,
        imgList: this.selectImgList,
      }
      this.historyDataToWrite(writeObj);
    },

    //   새로고침 버튼
    refreshBtn() {
      // 히스토리 리스트 db에서 받아오기
      this.getHistoryList(this.waitingData.patientId);
      // 선택한 진료기록 초기화
      this.selectHistoryData = "";
      this.selectSbList = [];
      this.selectCbList = [];
      this.selectImgList = [];
      this.pyItems = [];
      this.sbItems = [];
      this.cbItems = [];
      this.slides = [];
      this.initHistoryData();
    },
    getVisitData(item) {
      return axios.post('/nurse/getVisitData', {
        visitType: item,
      }).then(response => {
        let list = response.data;
        this.setVisitList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    getReceiptData(item) {
      return axios.post('/nurse/getReceiptData', {
        historyId: item.id,
      }).then(response => {
        let data = response.data;
        this.setReceiptData(data);
      }).catch(function (error) {
        console.log(error);
      });
    },

  },

}
</script>

<style lang="scss" scoped>
.refresh-img-box {
  width: 25px;
  margin: 0 auto;
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.refresh-img-box img {
  width: inherit;
  object-fit: contain;
}
.title-border-box {
  margin: 5px;
  padding: 0 5px;
  border-width: 2px;
  border-style: solid;
  border-color: #003A63;
  border-image: initial;
  border-radius: 10px;
}
.main-border-box {
  margin: 5px;
  padding: 0 5px;
  border-width: 1px;
  border-style: solid;
  border-color: #ccc;
  border-image: initial;
  border-radius: 10px;
}
.empty-patient-box {
  height: 140px;
  display: flex;
}
.empty-list-box {
  height: 600px;
  display: flex;
  flex-grow: 1;
  width: inherit;
}

.empty-box {
  height: 460px;
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

.waiting-list-box {
  overflow-y: scroll;
  max-height: 600px;
}

/* 스크롤바 숨기기 */
.waiting-list-box::-webkit-scrollbar {
  width: 0;
}
.waiting-box:hover {
  background-color: #cccccc;
  cursor: pointer;
}
.history-select {
  background-color: #cccccc;
}

b-table * * {
  padding: 0 !important;
  margin: 0 !important;
}

.history-box {
  //display: flex;
}

// 의사정보 box
.doctor-info {
  //text-align: center;
}

.doctor-info span {
  font-size: 12px;
  font-weight: 600;
  // 글자 줄바꿈 안되게 함.
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


.ellipsis-sb {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 50px; /* 요소의 최대 너비를 지정합니다. */
}

.ellipsis-code {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px; /* 요소의 최대 너비를 지정합니다. */
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 150px; /* 요소의 최대 너비를 지정합니다. */
}


.table-wrapper {
  display: flex;
}

.table-wrapper > b-table {
  flex: 1;
}

.td-box-sb {
  width: 50px;
}

.td-box-code {
  width: 50px;
}

.td-box-name {
  width: 150px;
}


.history-detail-box {
  flex-grow: 1;
}

// swiper
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


// 환자 정보
.Patient-box {
}

.patient-info {
  flex-wrap: nowrap;
}

.patient-info span {
  font-size: 10px;
  // 글자 줄바꿈 안되게 함.
  white-space: nowrap;
}

.patient-info span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 3px 0px 3px;
}


</style>
