<template>
  <section id="hero" class="d-flex align-items-center" style="background: none">
    <div class="container position-relative text-center text-lg-start" data-aos="zoom-in" data-aos-delay="100">
      <div class="row justify-content-center">

        <h1 style="font-weight: 500"><span>접수 완료</span></h1>
        <hr>
        <h1 style="font-weight: 500">{{nameMsg}}님, 접수가 완료되었습니다.</h1>

        <div style="width: 500px">
          <h2>{{timeMsg}}초 후 자동으로 첫 화면으로 이동합니다.</h2>
        </div>

      </div>

    </div>
  </section><!-- End Hero -->
</template>

<script>
export default {
  name: "RegisterClear",
  data(){
    return {
      time: 5,
    }
  },
  mounted() {
    let x = setInterval(()=>{
      this.time--;
      if(this.time<0) {
        clearInterval(x);
        this.$router.push('m.home');
      }
    }, 1000);
  },
  computed: {
    nameMsg() {
      return this.$store.state.mobile.name;
    },
    timeMsg() {
      return this.time;
    }
  },
}
</script>

<style scoped>

</style>