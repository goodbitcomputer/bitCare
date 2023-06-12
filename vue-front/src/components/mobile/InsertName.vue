<template>
  <section id="hero" class="d-flex align-items-center">
    <div class="container position-relative text-center text-lg-start" data-aos="zoom-in" data-aos-delay="100">
      <div class="d-flex justify-content-center">
        <div>
          <h1><span>처음 등록하시네요.</span></h1>
          <h1>이름을 입력해주세요.</h1>

          <div class="btns">
            <b-input class="w-75 m-auto" v-model="patientName"></b-input>
          </div>
        </div>
      </div>
      <!--      start 버튼 박스-->
      <div class="btn-util-box d-flex justify-content-center">
        <button class="util-btn left-util-btn btn bg-warning" @click="prevBtn" style="width: 120px; ">이전</button>
        <button class="util-btn btn bg-warning" @click="nextBtn" style="width: 120px;">다음</button>
      </div>
      <!--      end 버튼 박스-->
    </div>
  </section><!-- End Hero -->
</template>

<script>
import {mapMutations} from "vuex";

const Swal = window.Swal;
export default {
  name: "InsertName",
  data() {
    return {
      patientName: '',
    }
  },
  computed: {
    // 이름 정규식
    nameMsg() {
      const regexp = new RegExp(/^[가-힣a-zA-Z]+$/);

      let strCheck = this.patientName.match(regexp);
      return strCheck;
    },
  },
  methods: {
    // vuex에 휴대폰번호 또는 주민등록번호 저장
    ...mapMutations('mobile', {
      setName: 'setName',
      setNextStep: 'setNextStep',
      initState: 'initState',
    }),
    // 이전 버튼
    prevBtn() {
      this.initState();
    },
    // 다음 버튼
    nextBtn() {
      if (this.nameMsg !== null) {
        this.setName(this.nameMsg.input);
        this.setNextStep(4);
      } else {
        Swal.fire({
          icon: 'error',
          title: 'error',
          text: '한글 또는 영어(대소문자 구분, 띄어쓰기x)로 입력해주세요.',
          showConfirmButton: false,
          timer: 1000
        })
      }
    },

  },
}
</script>

<style scoped>
#hero {
  width: 100%;
  /*height: 100vh;*/
  background: none;
  background-color: #A1C7E0;
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

#hero:before {
  content: "";
  background: rgba(0, 0, 0, 0.6);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

@media (min-width: 992px) {

  .util-btn {
    width: 250px !important;
    margin: 30px 0 0 0;
  }

  .left-util-btn {
    margin: 30px 265px 0 0;
  }

}

@media (max-width: 992px) {

  .util-btn {
    width: 180px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 95px 0 0;
  }

}

@media (max-width: 576px) {

  .util-btn {
    width: 135px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 95px 0 0;
  }

}

@media (max-width: 400px) {

  .util-btn {
    width: 100px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 40px 0 0;
  }

}

@media (max-width: 300px) {

  .util-btn {
    width: 100px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 20px 0 0;
  }

}
</style>