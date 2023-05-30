<template>
  <div>
    <section id="mobile-doctor">
      <div id="mobileDoctor-box" class="container" data-aos="fade-up">

      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: "MobileMessage"
}
</script>

<style scoped>
section {
  padding: 0;
}

#mobile-doctor {
  width: 100%;
  height: 100vh;
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

#mobileDoctor-box {
  padding-top: 150px;
  padding-bottom: 65px;
}

#mobileDoctor-box * {
  /*color: #cccccc;*/
  white-space: nowrap;
}
</style>