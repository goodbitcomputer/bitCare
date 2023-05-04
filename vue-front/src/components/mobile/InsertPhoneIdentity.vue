<template>
  <section id="hero" class="d-flex align-items-center" style="background: none">
    <div class="info-box container position-relative text-center text-lg-start" data-aos="zoom-in"
         data-aos-delay="100">
      <div class="row justify-content-center">
        <div v-show="!isPhoneIdentityCheck" class="col-lg-5">
          <div class="container text-left">
            <h1 style="font-weight: 500;">처음이신가요?</h1>
            <h1 style="font-weight: 500;">휴대폰번호<span>를 입력해주세요.</span></h1>
          </div>
          <!--            휴대폰, 주민번호등록 div-->
          <div>
            <div class="m-3">
              <b-input readonly class="phone-input" :value="phonePadMsg"></b-input>
            </div>
          </div>
        </div>
        <div v-show="isPhoneIdentityCheck" class="col-lg-5">
          <div class="container text-left">
            <h1 style="font-weight: 500;">처음이신가요?</h1>
            <h1 style="font-weight: 500;">주민등록번호<span>를 입력해주세요.</span></h1>
          </div>
          <!--            휴대폰, 주민번호등록 div-->
          <div>
            <div class="d-flex m-3">
              <b-input readonly class="identity1-input" :value="identityPadMsg1"></b-input>
              <span class="" style="margin: 0 10px; font-size: 20px; color:white">-</span>
              <b-input readonly class="identity2-input" :value="identityPadMsg2"></b-input>
            </div>
          </div>
        </div>
        <div class="col-lg-4 justify-content-center position-relative">
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
        <div class="mr-5 mt-2">
          <button class="btn btn-dark" @click="prevBtn" style="width: 120px">이전</button>
        </div>
        <div class="ml-5 mt-2">
          <button class="btn btn-primary" @click="nextBtn" style="width: 120px">다음</button>
        </div>
      </div>
      <!--      end 버튼 박스-->
    </div>
  </section>
</template>

<script>
import {mapMutations, mapState} from "vuex";

export default {
  name: "PhoneIdentity",
  data() {
    return {
      phoneNumber: '',
      identityNumber: '',
    }
  },
  mounted() {

  },
  computed: {
    ...mapState('mobile', {
      statePhoneNumber : "phoneNumber",
      stateIdentityNumber : "identityNumber",
    }),
    // step1에서 입력한게 휴대폰번호, 주민등록번호인지 체크
    isPhoneIdentityCheck() {
      if(this.statePhoneNumber === "") return false;
      else return true;
    },
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
    ...mapMutations('mobile', {
      setPhoneNumber : 'setPhoneNumber',
      setIdentityNumber : 'setIdentityNumber',
      setNextStep : 'setNextStep',
      initState : 'initState',
    }),
    // 이전 버튼
    prevBtn() {
      this.initState();
    },
    // 다음 버튼
    nextBtn() {
      if (!this.isPhoneIdentityCheck && this.phoneNumber.length===11) {
        this.setPhoneNumber(this.phoneNumber);
        this.setNextStep(3);
      }else if (this.isPhoneIdentityCheck && this.identityNumber.length===13){
        this.setIdentityNumber(this.identityNumber);
        this.setNextStep(3);
      }else if(!this.isPhoneIdentityCheck){
        window.Swal.fire({
          icon: 'error',
          title: 'error',
          text: '휴대폰번호를 끝까지 입력해주세요.',
          showConfirmButton: false,
          timer: 1000
        })
      }else if(this.isPhoneIdentityCheck){
        window.Swal.fire({
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
      if (!this.isPhoneIdentityCheck) this.phonePad(event);
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
  }
}
</script>

<style lang="scss" scoped>
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
.button {
  float: left;
  min-width: 70px;
  max-width: 100px;
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
.button--size-s {
  font-size: 18px;
}

.button--size-m {
  font-size: 16px;
}

.button--size-l {
  font-size: 18px;
}

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