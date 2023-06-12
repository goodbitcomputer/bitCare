<template>
  <section id="hero" class="d-flex align-items-center">
    <div class="container position-relative text-center text-lg-start" data-aos="zoom-in" data-aos-delay="100">
      <div class="row justify-content-center">
        <h1><span>처음 등록하시네요.</span></h1>
        <h1>주소를 입력해주세요.</h1>

        <div class="input-box">
          <div class="d-flex mt-4">
            <b-input class="form__input" id="zip-code" name="zipCode" placeholder="우편번호" :value="postcode" readonly/>
            <input type="button" class="btn btn-light ml-2 mb-3" @click="execDaumPostcode()"
                   value="우편번호 찾기">
          </div>

          <b-input id="address-1" placeholder="도로명주소" :value="roadAddress" readonly/>

          <b-input id="address-1-1" placeholder="지번주소" :value="streetAddress" readonly/>

          <b-input id="address-2" placeholder="상세주소" v-model="extraAddress"/>
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
// const daum = window.daum;
import {mapActions, mapMutations} from "vuex";

const Swal = window.Swal;

export default {
  name: "InsertAddress",
  data() {
    return {
      postcode: "",
      roadAddress: "",
      streetAddress: "",
      extraAddress: "",
    }
  },
  mounted() {

  },
  computed: {
    addressObg() {
      let addressObg = {
        postcode: this.postcode,
        roadAddress: this.roadAddress,
        streetAddress: this.streetAddress,
        extraAddress: this.extraAddress,
      }
      return addressObg;
    }
  },
  methods: {
    ...mapMutations('mobile', {
      setAddress: 'setAddress',
      setNextStep: 'setNextStep',
      initStep3: 'initStep3',
    }),
    ...mapActions('mobile', {
      insertPatient: 'insertPatient'
    }),
    execDaumPostcode() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          this.postcode = data.zonecode;
          this.roadAddress = data.address;
          this.streetAddress = data.jibunAddress;
          document.getElementById("address-2").focus();
        },
      }).open();
    },
    // 이전 버튼
    prevBtn() {
      this.initStep3();
    },
    // 다음 버튼
    nextBtn() {
      if (this.postcode !== "" && this.extraAddress !== "") {
        this.setAddress(this.addressObg);
        this.insertPatient();
        this.setNextStep(5);
      } else if (this.postcode === "") {
        Swal.fire({
          icon: 'error',
          title: 'error',
          text: '우편번호를 입력해주십시오.',
          showConfirmButton: false,
          timer: 1000
        })
      } else if (this.extraAddress === "") {
        Swal.fire({
          icon: 'error',
          title: 'error',
          text: '상세주소를 입력해주십시오.',
          showConfirmButton: false,
          timer: 1000
        })
      }
    },
  }
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
  .input-box{
    width: 600px;
  }
  .util-btn {
    width: 250px !important;
    margin: 30px 0 0 0;
  }

  .left-util-btn {
    margin: 30px 265px 0 0;
  }

}

@media (max-width: 992px) {
  .input-box{
    width: 600px;
  }
  .util-btn {
    width: 180px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 95px 0 0;
  }

}

@media (max-width: 576px) {
  .input-box{
    width: 500px;
  }
  .util-btn {
    width: 135px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 95px 0 0;
  }

}

@media (max-width: 400px) {
  .input-box{
    width: 300px;
  }
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