<template>
  <section id="hero" class="d-flex align-items-center" style="background: none">
    <div class="container position-relative text-center text-lg-start" data-aos="zoom-in" data-aos-delay="100">
      <div class="row">
        <h1><span>처음 등록하시네요.</span></h1>
        <h1>이름을 입력해주세요.</h1>

        <div class="btns">
          <b-input class="w-50 m-auto" v-model="patientName"></b-input>
        </div>
      </div>
      <!--      start 버튼 박스-->
      <div class="btn-util-box d-flex justify-content-center">
        <div class="mr-5 mt-5">
          <button class="btn btn-dark" @click="prevBtn" style="width: 120px">이전</button>
        </div>
        <div class="ml-5 mt-5">
          <button class="btn btn-primary" @click="nextBtn" style="width: 120px">다음</button>
        </div>
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
      }else{
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

</style>