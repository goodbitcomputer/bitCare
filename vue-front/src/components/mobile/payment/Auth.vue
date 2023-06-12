<template>
  <section id="payment-auth" class="d-flex align-items-center">
    <div id="info-box" class="info-box container position-relative text-center text-lg-start" data-aos="zoom-in"
         data-aos-delay="100">
      <div class="row justify-content-center">
        <div class="input-box col">
          <div class="input-text-box text-left" style="margin: 0 auto">
            <h1 style="font-weight: 500; color:white">휴대폰번호 <span>또는</span></h1>
            <h1 style="font-weight: 500; color:white">주민등록번호<span>를 입력해주세요.</span></h1>
          </div>
          <!--            휴대폰, 주민번호등록 div-->
          <div>
            <ul class="input-text-box menu align-center expanded text-center SMN_effect-21 flex-nowrap mx-auto mb-2">
              <li>
                <button ref="phoneBtn" value="true" class="focused" @click="inputChange">휴대폰번호</button>
              </li>
              <li>
                <button ref="identityBtn" value="false" @click="inputChange">주민등록번호</button>
              </li>
            </ul>
            <div v-if="isPhoneCheck" class="input-box d-flex justify-content-center">
              <b-input readonly class="phone-input" :value="phonePadMsg"></b-input>
            </div>
            <div v-else class="input-box d-flex justify-content-center">
              <b-input readonly class="identity1-input" :value="identityPadMsg1"></b-input>
              <span class="tot m-1" style="color: white">-</span>
              <b-input readonly class="identity2-input" :value="identityPadMsg2"></b-input>
            </div>
          </div>
        </div>
        <div class="col justify-content-center position-relative">
          <div>
            <div class="num-pad-box container">
              <div class="number-pad justify-content-center">
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="1">1
                </button>
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="2">2
                </button>
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="3">3
                </button>
              </div>
              <div class="number-pad justify-content-center">
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="4">4
                </button>
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="5">5
                </button>
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="6">6
                </button>
              </div>
              <div class="number-pad justify-content-center">
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="7">7
                </button>
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="8">8
                </button>
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="9">9
                </button>
              </div>
              <div class="number-pad justify-content-center">
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="-1">
                  <div style="pointer-events: none;">지움</div>
                </button>
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="0">0
                </button>
                <button class="button button--moema button--inverted button--text-thick button--size-s"
                        @click="numPadOn" value="-2">
                  <div style="display: block;pointer-events: none;">전체</div>
                  <div style="pointer-events: none;">지움</div>
                </button>
              </div>
            </div>
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
  </section>
</template>

<script>
import {mapMutations,} from 'vuex';
import axios from "axios";

const Swal = window.Swal;
export default {
  name: "mobileAuth",

  data() {
    return {
      isPhoneCheck: true,
      phoneNumber: '',
      identityNumber: '',
    }
  },
  mounted() {
    this.divHeightFix();
  },
  computed: {
    // 휴대폰번호
    phonePadMsg() {
      let newStr = this.phoneNumber.replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, "");
      return newStr;
    },
    // 주민등록번호
    identityPadMsg1() {
      let str = this.identityNumber.slice(0, 6);
      return str
    },
    identityPadMsg2() {
      if (this.identityNumber.length < 7) return null;
      let str = this.identityNumber.slice(6, 13);
      return str;
    },
  },
  methods: {
    // vuex에 휴대폰번호 또는 주민등록번호 저장
    ...mapMutations('mobilePayment', {
      setPatientData: 'setPatientData',
      setNextStep: 'setNextStep',
    }),
    divHeightFix() {
      let div = document.getElementById('payment-auth');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;

      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      } else {
        div.style.height = 'auto';
      }
    },
    prevBtn() {
      this.$router.back();
    },
    nextBtn() {
      if (this.isPhoneCheck && this.phoneNumber.length === 11) {
        this.auth();
      } else if (!this.isPhoneCheck && this.identityNumber.length === 13) {
        this.auth();
      } else if (this.isPhoneCheck) {
        Swal.fire({
          icon: 'error',
          title: 'error',
          text: '휴대폰번호를 끝까지 입력해주세요.',
          showConfirmButton: false,
          timer: 1000
        })
      } else if (!this.isPhoneCheck) {
        Swal.fire({
          icon: 'error',
          title: 'error',
          text: '주민등록번호를 끝까지 입력해주세요.',
          showConfirmButton: false,
          timer: 1000
        })
      }
    },
    // 휴대폰번호, 주민등록번호 focus 유지
    inputChange(event) {
      this.isPhoneCheck = event.target.value === 'true' ? true : false;
      if (this.isPhoneCheck) {
        this.$refs.phoneBtn.classList.add('focused');
        this.$refs.identityBtn.classList.remove('focused');
      } else {
        this.$refs.identityBtn.classList.add('focused');
        this.$refs.phoneBtn.classList.remove('focused');
      }
    },
    // numpad 클릭시 휴대폰번호인지 주민번호인지 확인
    numPadOn(event) {
      if (this.isPhoneCheck) this.phonePad(event);
      else this.identityPad(event);
    },
    // 휴대폰번호 입력 로직
    phonePad(event) {
      if (event.target.value === '-1') this.phoneNumber = this.phoneNumber.slice(0, -1);
      else if (event.target.value === '-2') this.phoneNumber = "";
      else {
        if (this.phoneNumber.length === 11) ;
        else this.phoneNumber += event.target.value;
      }
    },
    // 주민등록번호 입력 로직
    identityPad(event) {
      if (event.target.value === '-1') this.identityNumber = this.identityNumber.slice(0, -1);
      else if (event.target.value === '-2') this.identityNumber = "";
      else {
        if (this.identityNumber.length === 13) ;
        else this.identityNumber += event.target.value;
      }
    },
    auth() {
      return axios.post('/mobile/auth', {
        isPhoneCheck: this.isPhoneCheck,
        phoneNumber: this.phoneNumber,
        identityNumber: this.identityNumber,
      }).then((response) => {
        if(response.data !== "") {
          this.setPatientData(response.data);
          this.setNextStep(2);
        }else{
          Swal.fire({
            icon: 'error',
            title: 'error',
            text: '등록된 환자정보가 없습니다.',
            showConfirmButton: false,
            timer: 1000
          })
        }
      }).catch(function (error) {
        console.log(error);
      })
    },
  }
}
</script>

<style lang="scss" scoped>
#payment-auth {
  width: 100%;
  /*height: 100vh;*/
  //background: url("/public/assets/img/main/payment-bg.jpg");
  //background: url("@/assets/img/payment-bg.jpg");
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

#payment-auth:before {
  content: "";
  background: rgba(0, 0, 0, 0.6);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

h1 {
  font-size: 27px !important;
  // 글자 줄바꿈 안되게 함.
  white-space: nowrap;
}

h1 > span {
  font-size: 25px;
}

.number-pad {
  display: flex;
  flex-wrap: nowrap;
}

@media (max-width: 992px) {
  #hero .info-box {
    padding-top: 0px;
  }
  #hero .num-pad-box {
    padding-top: 0px;
  }
  //.btn-util-box {
  //  padding: 0 50px;
  //}
}


/* 휴대폰번호, 주민번호 버튼 */
/*



/*
-----------------
common style
-----------------
*/
.hero {
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.menu button {
  color: rgba(255, 255, 255, 0.8);
  font-family: Lato;
  font-size: 17pt;
  font-weight: 400;
  padding: 5px 25px;
  /**/
  position: relative;
  display: block;
  text-decoration: none;
  text-transform: uppercase;
  width: 200px;
}

/*
---------------------------------------
hover effect 21
---------------------------------------
*/

.SMN_effect-21 button:before, .SMN_effect-21 button:after {
  content: "";
  width: 0;
  height: 2px;
  background: rgba(255, 255, 255, 0.4);
  position: absolute;
  top: 0;
  left: 50%;
  transition: all 0.3s ease 0s;
}

.SMN_effect-21 button:after {
  top: auto;
  bottom: 0;
}

//.SMN_effect-21 button:focus:before, .SMN_effect-21 button:focus:after {
//  width: 100%;
//  left: 0;
//}
.SMN_effect-21 .focused:before, .SMN_effect-21 .focused:after {
  width: 100%;
  left: 0;
}


/* 넘버패드 css ------------ */
/* Common button styles */
@media (min-width: 992px) {
  .input-text-box {
    width: 300px;
  }
  .input-box {
    //margin-right: 95px;
    //margin:0 auto;
  }
  .phone-input {
    width: 300px;
    font-size: 20px;
  }
  .identity1-input {
    width: 140px;
    font-size: 20px;
  }
  .identity2-input {
    width: 140px;
    font-size: 20px;
  }
  .util-btn {
    width: 250px !important;
    margin: 30px 0 0 0;
  }
  .left-util-btn {
    margin: 30px 265px 0 0;
  }
  .tot {
    font-size: 20px;
  }
  .button {
    float: left;
    min-width: 100px;
    height: 70px;
    display: block;
    margin: 0.1em;
    padding: 1em 1em;
    border: none;
    background: none;
    color: inherit;
    vertical-align: middle;
    position: relative;
    z-index: 1;
    -webkit-backface-visibility: hidden;
    -moz-osx-font-smoothing: grayscale;

    font-size: 22px;
  }
}

@media (max-width: 992px) {
  .input-text-box {
    width: 458px;
  }
  .phone-input {
    width: 458px;
    font-size: 40px;
  }
  .identity1-input {
    width: 215px;
    font-size: 40px;
  }
  .identity2-input {
    width: 215px;
    font-size: 40px;
  }
  .tot {
    font-size: 40px;
  }
  .util-btn {
    width: 180px !important;
    margin: 20px 0 0 0;
  }
  .left-util-btn {
    margin: 20px 95px 0 0;
  }
  .button {
    float: left;
    width: 150px;
    height: 120px;
    display: block;
    margin: 0.1em;
    padding: 1em 1em;
    border: none;
    background: none;
    color: inherit;
    vertical-align: middle;
    position: relative;
    z-index: 1;
    -webkit-backface-visibility: hidden;
    -moz-osx-font-smoothing: grayscale;

    font-size: 35px;
  }
}

@media (max-width: 576px) {
  .input-text-box {
    width: 370px;
  }
  .phone-input {
    width: 370px;
    font-size: 25px;
  }
  .identity1-input {
    width: 170px;
    font-size: 25px;
  }
  .identity2-input {
    width: 170px;
    font-size: 25px;
  }
  .tot {
    font-size: 25px;
  }
  .util-btn {
    width: 135px !important;
    margin: 20px 0 0 0;
  }
  .left-util-btn {
    margin: 20px 95px 0 0;
  }
  .button {
    float: left;
    width: 120px;
    height: 90px;
    display: block;
    margin: 0.1em;
    padding: 1em 1em;
    border: none;
    background: none;
    color: inherit;
    vertical-align: middle;
    position: relative;
    z-index: 1;
    -webkit-backface-visibility: hidden;
    -moz-osx-font-smoothing: grayscale;

    font-size: 22px;
  }
}

@media (max-width: 400px) {
  .input-text-box {
    width: 310px;
  }
  .phone-input {
    width: 310px;
    font-size: 20px;
  }
  .identity1-input {
    width: 140px;
    font-size: 20px;
  }
  .identity2-input {
    width: 140px;
    font-size: 20px;
  }
  .tot {
    font-size: 20px;
  }
  .util-btn {
    width: 100px !important;
    margin: 20px 0 0 0;
  }
  .left-util-btn {
    margin: 20px 40px 0 0;
  }
  .button {
    float: left;
    width: 100px;
    height: 70px;
    display: block;
    margin: 0.1em;
    padding: 1em 1em;
    border: none;
    background: none;
    color: inherit;
    vertical-align: middle;
    position: relative;
    z-index: 1;
    -webkit-backface-visibility: hidden;
    -moz-osx-font-smoothing: grayscale;

    font-size: 20px;
  }
}

@media (max-width: 300px) {
  .input-text-box {
    width: 200px;
  }
  .phone-input {
    width: 200px;
    font-size: 18px;
  }
  .identity1-input {
    width: 88px;
    font-size: 20px;
  }
  .identity2-input {
    width: 88px;
    font-size: 20px;
  }
  .tot {
    font-size: 20px;
  }
  .util-btn {
    width: 100px !important;
    margin: 20px 0 0 0;
  }
  .left-util-btn {
    margin: 20px 20px 0 0;
  }
  .button {
    float: left;
    width: 80px;
    height: 50px;
    display: block;
    margin: 0.1em;
    padding: 1em 1em;
    border: none;
    background: none;
    color: inherit;
    vertical-align: middle;
    position: relative;
    z-index: 1;
    -webkit-backface-visibility: hidden;
    -moz-osx-font-smoothing: grayscale;

    font-size: 18px;
  }
}

.button:focus {
  outline: none;
}

.button > span {
  vertical-align: middle;
}

/* Text color adjustments (we could stick to the "inherit" but that does not work well in Safari) */
.bg-1 .button {
  color: #37474f;
  border-color: #37474f;
}

.bg-2 .button {
  color: #ECEFF1;
  border-color: #ECEFF1;
}

.bg-3 .button {
  color: #fff;
  border-color: #fff;
}

/* Sizes */
//.button--size-s {
//  font-size: 18px;
//}
//
//.button--size-m {
//  font-size: 16px;
//}
//
//.button--size-l {
//  font-size: 18px;
//}

/* Moema */
.button--moema {
  padding: 1em 1em;
  border-radius: 10px;
  background: #7986cb;
  color: #fff;
  -webkit-transition: background-color 0.3s, color 0.3s;
  transition: background-color 0.3s, color 0.3s;
}

.button--moema.button--inverted {
  background: #ECEFF1;
  color: #37474f;
}

.button--moema::before {
  content: '';
  position: absolute;
  top: -20px;
  left: -20px;
  bottom: -20px;
  right: -20px;
  background: inherit;
  border-radius: 10px;
  z-index: -1;
  opacity: 0.4;
  -webkit-transform: scale3d(0.5, 0.5, 1);
  transform: scale3d(0.5, 0.5, 1);
}

.button--moema:active {
  -webkit-transition: background-color 0.1s 0.3s, color 0.1s 0.3s;
  transition: background-color 0.1s 0.3s, color 0.1s 0.3s;
  color: #ECEFF1;
  background-color: #3f51b5;
  -webkit-animation: anim-moema-1 0.1s forwards;
  animation: anim-moema-1 0.1s forwards;
}

.button--moema.button--inverted:hover {
  color: #ECEFF1;
  background-color: #7986cb;
}

.button--moema:hover::before {
  -webkit-animation: anim-moema-2 0.3s 0.3s forwards;
  animation: anim-moema-2 0.3s 0.3s forwards;
}

@-webkit-keyframes anim-moema-1 {
  60% {
    -webkit-transform: scale3d(0.8, 0.8, 1);
    transform: scale3d(0.8, 0.8, 1);
  }
  85% {
    -webkit-transform: scale3d(1.1, 1.1, 1);
    transform: scale3d(1.1, 1.1, 1);
  }
  100% {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

@keyframes anim-moema-1 {
  60% {
    -webkit-transform: scale3d(0.8, 0.8, 1);
    transform: scale3d(0.8, 0.8, 1);
  }
  85% {
    -webkit-transform: scale3d(1.1, 1.1, 1);
    transform: scale3d(1.1, 1.1, 1);
  }
  100% {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

@-webkit-keyframes anim-moema-2 {
  to {
    opacity: 0;
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

@keyframes anim-moema-2 {
  to {
    opacity: 0;
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

</style>