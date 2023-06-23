<template>
  <div id="search">
    <div id="search-cover">
      <div class="bg">
        <div class="bg-cover">
          <nav-bar/>
          <div class="container">
            <MobileSearchPatient class="search-patient-box" v-if="patientListByStep"/>
            <PatientHistoryList class="search-patient-box" v-if="patientHistoryListByStep"/>
            <PatientHistory class="search-patient-box" v-if="patientHistoryByStep"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MobileSearchPatient from "@/components/search/Patient.vue";
import NavBar from "@/components/NavBar.vue";
import PatientHistoryList from "@/components/search/PatientHistoryList.vue";
import PatientHistory from "@/components/search/PatientHistory.vue";
import {mapMutations, mapState} from "vuex";

export default {
  name: "WebSearch",
  components: {
    NavBar,
    MobileSearchPatient,
    PatientHistoryList,
    PatientHistory,
  },
  data() {
    return {
      isUpdate: 1,
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.setNextStep(1);
      // this.divHeightFix();
    })
  },
  updated() {
    this.$nextTick(() => {
      this.divHeightFix();
    })
  },
  computed: {
    ...mapState('search',
        ['nextStep']
    ),
    patientListByStep() {
      return this.nextStep === 1 ? true : false;
    },
    patientHistoryListByStep() {
      return this.nextStep === 2 ? true : false;
    },
    patientHistoryByStep() {
      return this.nextStep === 3 ? true : false;
    },
  },
  methods: {
    ...mapMutations('search', {
      setNextStep: 'setNextStep',
    }),
    divHeightFix() {
      let div = document.getElementById('search');
      div.style.height = 'initial';
      let cover = document.getElementById('search-cover');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;

      if (divHeight <= windowHeight) {
        div.style.height = '100vh';
        cover.style.height = '100vh';
      } else {
        div.style.height = 'initial';
        cover.style.height = 'initial';
      }
    },
  },

}
</script>

<style scoped>
.search-patient-box {
}

#search {
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
  padding: 0;
}

/*#search-cover {*/
/*  content: "";*/
/*  background: rgba(0, 0, 0, 0.6);*/
/*  bottom: 0;*/
/*  top: 0;*/
/*  left: 0;*/
/*  right: 0;*/
/*}*/

.bg {
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
  padding: 0;
}

/*.bg-cover {*/
/*  content: "";*/
/*  background: rgba(0, 0, 0, 0.6);*/
/*  bottom: 0;*/
/*  top: 0;*/
/*  left: 0;*/
/*  right: 0;*/
/*}*/
</style>