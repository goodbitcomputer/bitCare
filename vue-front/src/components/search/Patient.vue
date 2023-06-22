<template>
  <section id="patient-page-box">
    <div id="page_box">
      <div id="patient_list_box" class="d-flex flex-wrap">
        <div class="border-box patient-box" v-for="item in patientList" :key="item.id">
          <div @click="patientBtn(item)">
            <div>
              <span class="font-weight-bold">{{ item.name }} </span>
              <span> cn.{{ item.id }}</span>
            </div>
            <div class="patient-info">
              <span v-if="item.identityNumber">{{ identityNumberMsg(item.identityNumber) }}</span>
              <span v-if="item.birth">{{ ageMsg(item.birth) }}세</span>
              <span>{{ item.gender }}</span>
              <span v-if="item.phoneNumber">{{ phoneNumMsg(item.phoneNumber) }}</span>
            </div>
            <div>
              <span class="font-weight-bold">최근진료</span>
              <div v-if="item.symptomDetail===null" class="ellipsis"
                   style="padding: 5px; border: 1px solid #DBDFE5; border-radius: 5px; margin-bottom: 5px; height: 35px">
                진료기록 없음
              </div>
              <div v-if="!(item.symptomDetail===null)" class="ellipsis"
                   style="padding: 5px; border: 1px solid #DBDFE5; border-radius: 5px; margin-bottom: 5px; height: 35px">
                <div class="ellipsis" v-html="item.symptomDetail"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--    페이지네이션-->
    <div class="pagination-box d-flex justify-content-center">
      <tr>
        <td colspan="5" class="">
          <ul class="pagination">
            <li class="page-item">
              <div class="page-link page-util" :class="{'page-util-select' : isExistPrevPage}"
                   @click="movePage(Number(1))">&lt;&lt;
              </div>
            </li>
            <li class="page-item">
              <div class="page-link page-util" :class="{'page-util-select' : isExistPrevPage}"
                   @click="movePage('prev')">&lt;
              </div>
            </li>

            <li class="page-item" v-for="item in pageRange(paginationData.startPage, paginationData.endPage)"
                :key="item">
              <div class="page-link" style="cursor: pointer;" :class="{'page-select' : item.isSelect}"
                   @click="movePage(Number(item.page))">{{ item.page }}
              </div>
            </li>

            <li class="page-item">
              <div class="page-link page-util" :class="{'page-util' : isExistNextPage}" @click="movePage('next')">
                &gt;
              </div>
            </li>
            <li class="page-item">
              <div class="page-link page-util" :class="{'page-util' : isExistNextPage}" @click="movePage('lastNext')">
                &gt;&gt;
              </div>
            </li>
          </ul>
        </td>
      </tr>
    </div>
  </section>
</template>

<script>

import axios from "axios";
import {mapActions, mapMutations} from "vuex";

export default {
  name: "MobileSearchPatient",
  data() {
    return {
      isSearchCheck: false,
      keyword: "",
      page: 1,

      // axios
      patientList: [],
      paginationData: "",
    }
  },
  mounted() {
    console.log("mounted");
    this.$nextTick(() => {
      this.searchProc();
      // this.divHeightFix();
    })
  },
  update() {
    console.log("updated");
    this.$nextTick(() => {
      // this.divHeightFix();
    })
  },
  watch: {},
  computed: {

    isExistPrevPage() {
      if (this.paginationData === "") return false;
      else return this.paginationData.existPrevPage;
    },
    isExistNextPage() {
      if (this.paginationData === "") return false;
      else return this.paginationData.existNextPage;
    },
  },
  methods: {
    ...mapMutations('search', {
      setNextStep: 'setNextStep',
      setPatientData: 'setPatientData',
    }),
    ...mapActions('search', {
      getHistoryList: 'getHistoryList'
    }),
    divHeightFix() {
      let div = document.getElementById('page_box');
      // div.style.height = 'initial';
      // let listDiv = document.getElementById('patient_list_box');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;

      console.log(divHeight);
      console.log(windowHeight);

      if (divHeight <= windowHeight - 121) {
        div.style.height = 'calc( 100vh - 121px )';
      } else {
        div.style.height = 'initial';
      }
    },

    // 주민번호
    identityNumberMsg(item) {
      if (typeof item === "object") {
        return ""
      } else {
        let str1 = item.slice(0, 6);
        let str2 = item.slice(6, 7);
        str1 = str1 + "-" + str2 + "******";
        return str1;
      }
    },
    // 휴대폰번호
    phoneNumMsg(item) {
      if (item === "") {
        return ""
      } else {
        let newStr = item.replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, "");
        return newStr;
      }
    },
    // 나이
    ageMsg(item) {
      if (item === "") {
        return ""
      } else {
        let dateTemp = new Date(item);
        let dateNow = new Date();

        let tempYear = dateTemp.getFullYear();
        let nowYear = dateNow.getFullYear();
        let age = parseInt(nowYear) - parseInt(tempYear) + 1;
        return age;
      }
    },

    pageRange: function (start, end) {
      let list = [];
      for (let i = start; i <= end; i++) {
        list.push({"page": i, "isSelect": this.page === i ? true : false});
      }
      return list;
    },
    // 페이지이동 로직
    movePage(page) {
      let resultPage = 1;
      if (page === 'prev') resultPage = this.paginationData.startPage - 1;             // 이전 페이지
      else if (page === 'next') resultPage = this.paginationData.endPage + 1;          // 다음 페이지
      else if (page === 'lastNext') resultPage = this.paginationData.totalPageCount; // 마지막 페이지
      else resultPage = page;

      this.$router.push({path: "/search", query: {keyword: this.keyword, page: resultPage}});
      this.$router.go();
    },

    setSearchData() {
      this.keyword = this.$route.query.keyword;
      if (this.$route.query.page !== undefined) {
        this.page = Number(this.$route.query.page);
      }
    },
    setPatientList(item) {
      this.patientList = [];
      item.forEach((i) => {
        let year = i.birth.substring(i.birth.length - 5, i.birth.length);
        let birthIndex = i.birth.indexOf('00:00:00') - 1;
        let tempBirth = i.birth.substring(0, 3) + "," + i.birth.substring(3, birthIndex) + year + i.birth.substring(birthIndex, i.birth.length - 8);

        this.patientList.push({
          id: i.id,
          phoneNumber: i.phoneNumber,
          identityNumber: i.identityNumber,
          birth: tempBirth,
          gender: i.gender,
          name: i.name,
          addressId: i.addressId,
          symptomDetail: i.symptomDetail,
        })
      })
    },
    setPaginationData(item) {
      this.paginationData = {
        totalRecordCount: Number(item.totalRecordCount),
        totalPageCount: Number(item.totalPageCount),
        startPage: Number(item.startPage),
        endPage: Number(item.endPage),
        current: Number(item.current),
        limitStart: Number(item.limitStart),
        existPrevPage: Boolean(item.existPrevPage),
        existNextPage: Boolean(item.existNextPage)
      }
    },
    searchProc() {
      this.setSearchData();
      let keyword = this.keyword;
      let page = this.page;

      axios.post('/search/search_proc', {
        keyword: keyword,
        page: page,
      }).then((response) => {
        console.log(response.data);
        let patientList = JSON.parse(response.data.patientList);
        let paginationDTO = JSON.parse(response.data.paginationDTO);

        console.log("patientList");
        console.log(patientList);
        console.log("paginationDTO");
        console.log(paginationDTO);

        this.setPatientList(patientList);
        this.setPaginationData(paginationDTO);

        console.log(this.patientList);

      }).catch(function (error) {
        console.log(error);
      })

    },

    patientBtn(item) {
      console.log(item);
      this.setPatientData(item);
      this.getHistoryList(item.id);
      this.setNextStep(2);
    }
  },
}
</script>

<style scoped>
#patient-page-box{
}


.patient-box {
  background-color: white;
  align-content: flex-start
}

.patient-box:hover {
  background-color: #cccccc;
}

.patient-info {
  display: flex;
  flex-wrap: nowrap;
}

.patient-info span {
  font-size: 14px;
  /*글자 줄바꿈 안되게 함.*/
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


/*@media (min-width: 1500px) {*/
/*  .patient-box {*/
/*    width: calc(20% - 10px);*/
/*  }*/
/*}*/

@media (min-width: 1250px) {
  .patient-box {
    width: calc(25% - 10px);
  }
}

@media (max-width: 1250px) {
  .patient-box {
    width: calc(33% - 10px);
  }
}

@media (max-width: 991px) {
  .patient-box {
    width: calc(50% - 10px);
  }
}

@media (max-width: 770px) {
  .patient-box {
    width: 100%;
  }
}



.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%; /* 요소의 최대 너비를 지정합니다. */
}


.pagination-box {
  bottom: 20px;
}

.page-item {
}

.page-util {
  color: #CCCCCC;
  pointer-events: none;
}

.page-util-select {
  background-color: inherit;
  color: inherit;
  pointer-events: initial;
  cursor: pointer;
}

.page-select {
  background-color: #007aff;
  color: white;
  cursor: pointer;
}


</style>