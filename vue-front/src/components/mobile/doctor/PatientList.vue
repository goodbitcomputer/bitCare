<template>
  <!-- ======= Hero Section ======= -->
  <section id="mobile-doctor">
    <div id="patientList-box" class="patientList-box container text-center text-lg-start" data-aos="fade-up">
      <div class="util d-flex" data-aos="fade-up">
        <button class="border-box col">진료대기</button>
        <button class="border-box col">진료완료</button>
      </div>
      <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200">
        <div class="title">
          <span class="font-weight-bold">박은희 </span>
          <span>외래진료</span>
        </div>
        <div class="patient-info">
          <span>cn.6 </span>
          <span>1951.09.04 </span>
          <span>70세 </span>
          <span>여 </span>
          <div style="display: inline-block; color: white">진료중</div>
        </div>
        <div>
          <span>감기증상, 약처방위해 내원</span>
        </div>
      </div>
      <div class="patient-box border-box" data-aos="fade-up" data-aos-delay="200">
        <div class="title">
          <span class="font-weight-bold">박은희 </span>
          <span>외래진료</span>
        </div>
        <div class="patient-info">
          <span>cn.6 </span>
          <span>1951.09.04 </span>
          <span>70세 </span>
          <span>여</span>
          <div style="display: inline-block; color: white">진료중</div>
        </div>
        <div>
          <span>감기증상, 약처방위해 내원</span>
        </div>
      </div>

    </div>
  </section><!-- End Hero -->

</template>

<script>
export default {
  name: "MobileHome",

  mounted() {
    this.divHeightFix();
  },
  methods: {
    divHeightFix() {
      let div = document.getElementById('patientList-box');
      let divHeight = div.offsetHeight;
      let windowHeight = window.innerHeight;
      if (divHeight < windowHeight) {
        div.style.height = '100vh';
      } else {
        div.style.height = 'auto';
      }
    },
    registerBtn() {
      this.$store.commit('mobile/initState');
      this.$router.push('/mobile/register');
    },
    doctorBtn() {
      this.$router.push('/mobile/doctor')
    }
  },
}
</script>

<style scoped>
#mobile-doctor {
  width: 100%;
  /*height: 100vh;*/
  background: url("/public/assets/img/main/hero-bg.jpg");
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

#mobile-doctor:before {
  content: "";
  background: rgba(0, 0, 0, 0.2);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

.patientList-box {
  padding-top: 150px;
}

.patientList-box .util button{
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

.patient-box {
  text-align: left;
  background-color: white;
  /*background: rgba(12, 11, 9, 0.7);*/
  height: 80px;
}
.patient-box *{
  /*color: #cccccc;*/
  white-space: nowrap;
}
.patient-box .title *{
  /*color: white;*/
  white-space: nowrap;
}

/*.patient-info span, p {*/
/*  color: #999999;*/
/*  white-space: nowrap;*/
/*}*/

.patient-info {
  flex-wrap: nowrap;
}

.patient-info span {
  font-size: 10px;
  /*// 글자 줄바꿈 안되게 함.*/
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

.border-box {
  margin: 0 0 5px 0;
  padding: 5px;
  border: 1px solid #DBDFE5;
  /*border: 1px solid ;*/
  border-radius: 5px;
}


@media (max-width: 992px) {
  .patientList-box {
    padding-top: 110px;
  }
}
</style>