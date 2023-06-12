<template>
  <section id="hero" class="d-flex align-items-center">
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
        this.$router.push('/mobile/home');
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
</style>