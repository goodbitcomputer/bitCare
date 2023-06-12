<template>
  <div>
    <div class="border-box">
      <span style="font-size: 1.2em; font-weight: 700">환자 정보</span>
    </div>
<!--    <div class="empty-box border-box" v-if="isEmpty">-->
<!--      <div class="empty-img-box">-->
<!--        <img src="@/assets/img/empty-box.png">-->
<!--      </div>-->
<!--    </div>-->
    <div class="Patient-box border-box" v-if="true">

      <div>
        <div style="display: flex;align-items: center;">
          <span class="font-weight-bold">이지원</span>
          <span style="flex-grow: 1">cn.6</span>
          <button>...</button>
        </div>
        <div class="patient-info">
          <span>900225-1******</span>
          <span>33세</span>
          <span>남</span>
          <span>010-1111-2222</span>
        </div>
        <div style="border-top: 1px solid #DBDFE5;">
          <p class="font-weight-bold">진료메모</p>
          <p>부모 동반 내원, 세부 내역서 함께 드릴 것</p>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import {mapState} from "vuex";

export default {
  name: "NursePatient",

  mounted() {

  },

  data() {
    return {
      contentCss: '',
      ckeditor: 0,
    };
  },
  computed: {
    ...mapState('nurse',
        ['waitingData']
    ),
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
      // return true;
    },
  },
  methods: {
  }

}


</script>

<style lang="scss" scoped>
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

#memoEditor {
  height: 500px;
}

</style>
<style>
.ck-editor__editable {
  /* 부모크기 따라감 inherit */
  width: inherit;
  height: 100px;
}
</style>