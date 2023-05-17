<template>
  <div>
    <!-- ======= Header ======= -->
    <header id="footer" class="fixed-bottom d-flex align-items-center">
      <div id="main-box" class="container ">

        <div class="col text-center"><b-img src="/assets/mobile/img/home_house_icon2.png"/></div>
        <div class="col text-center"><b-img src="/assets/mobile/img/persons_icon2.png"/></div>
        <div class="col text-center"><b-img src="/assets/mobile/img/person_icon2.png"/></div>
        <div class="col text-center"><b-img src="/assets/mobile/img/comment_message_icon2.png"/></div>
        <div class="col text-center"><b-img src="/assets/mobile/img/dots_icon2.png"/></div>
        <!-- Uncomment below if you prefer to use an image logo -->
        <!-- <a href="index.html" class="logo me-auto me-lg-0"><img src="assets/img/logo.png" alt="" class="img-fluid"></a>-->

        <nav id="navbar" class="navbar order-last order-lg-0">

        </nav><!-- .navbar -->

      </div>
    </header>
    <!-- End Header -->

  </div>
</template>

<script>
export default {
  name: "NavBar",
  mounted() {

  }
}
</script>

<style scoped>
#footer {
  background: rgba(12, 11, 9, 0.6);
  border-bottom: 1px solid rgba(12, 11, 9, 0.6);
  transition: all 0.5s;
  z-index: 997;
  padding: 15px 0;
}
#footer img{
  width: 25px;
  /*filter: opacity(0.5) drop-shadow(0 0 0 white);*/
}
#main-box {
  display: flex;
  justify-content: space-between;
}
</style>