<template>
  <!-- ======= Hero Section ======= -->
  <section id="hero" class="d-flex align-items-center">
    <div class="container position-relative text-center text-lg-start" data-aos="zoom-in" data-aos-delay="100">
      <div class="row">

        <div class="col-lg-8">
          <h1>Welcome to <span>BitCare</span></h1>
          <h2>All in One EMR Cloud Platform</h2>

          <div class="btns">
            <a @click="registerBtn" class="btn-menu animated fadeInUp scrollto">접수하기</a>
            <a @click="doctorBtn" class="btn-book animated fadeInUp scrollto">관 계 자</a>
            <a @click="paymentBtn" class="btn-book animated fadeInUp scrollto">결제하기</a>
          </div>
        </div>
        <div class="col-lg-4 d-flex align-items-center justify-content-center position-relative" data-aos="zoom-in"
             data-aos-delay="200">
          <b-img class="play-btn" src="/assets/img/main/bitcare_icon.png"></b-img>
        </div>

      </div>
    </div>
  </section><!-- End Hero -->
</template>

<script>
export default {
  name: "MobileHome",

  methods: {
    registerBtn() {
      this.$store.commit('mobile/initState');
      this.$router.push('/mobile/register');
    },
    doctorBtn() {
      this.$router.push('/mobile/login')
    },
    paymentBtn() {
      this.$router.push('/mobile/payment')
    },
  },
}
</script>

<style scoped>
#hero .play-btn {
  width: 94px;
  height: 94px;
  background: none;
  border-radius: 50%;
  display: block;
  position: relative;
  overflow: hidden;
}
</style>