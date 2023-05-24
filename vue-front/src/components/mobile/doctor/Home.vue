<template>
  <!-- ======= Hero Section ======= -->
  <section id="hero" class="d-flex align-items-center">
    <div id="mobileDoctor-box"  class="container pt-0" data-aos="zoom-in" data-aos-delay="100">
      <div class="page-box justify-content-center">
        <div class="logo-box d-flex align-items-center" data-aos="zoom-in"
             data-aos-delay="200">
          <div>
            <b-img class="" src="/assets/mobile/img/bitcare_logo2.png"></b-img>
            <h2 class="text-center">All in One EMR Cloud Platform</h2>
          </div>
        </div>
        <div class="login-box d-flex justify-content-center">
          <!--            <h2>All in One EMR Cloud Platform</h2>-->

          <div class="login-info-box align-items-center d-flex justify-content-center">
            <div class="text-center">
              <div class="d-flex text-nowrap">
                <h1>백지영님 <span>환영합니다.</span></h1>
              </div>
              <div class="btns">
                <a class="btn-menu animated fadeInUp scrollto" @click="NextBtn(2)">대기환자</a>
              </div>
            </div>
          </div>
        </div>


      </div>
    </div>
  </section><!-- End Hero -->

</template>

<script>
import {mapMutations} from "vuex";

export default {
  name: "MobileHome",

  methods: {
    ...mapMutations('mobileDoctor', {
      setNextStep: 'setNextStep',
    }),
    NextBtn(item) {
      this.setNextStep(item);
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
.login-box {
  margin-top: 50px;
}

.logo-box {
  margin: 0 0 20px 0;
}

.login-box {
  margin: 0px 50px;
}

@media (min-width: 992px) {
  .page-box {
    display: flex;
  }

  .logo-box {
    width: 500px;
    margin: 0 40px 0 0;
  }

  .login-box {
    width: 500px;
  }

  .login-info-box h1, span {

  }
}
</style>