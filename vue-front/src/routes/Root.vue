<template>
  <div>
    <div class="home container">
    </div>
  </div>
</template>

<script>

export default {
  name: 'RootView',
  components: {
  },
  mounted() {
    this.redirectToMobilePage();
  },
  methods: {
    redirectToMobilePage() {
      // 모바일 기기를 감지하기 위한 정규식
      let mobilePattern = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i;

      // 현재 사용자의 User Agent 문자열 가져오기
      let userAgent = navigator.userAgent;

      // 모바일 기기로 감지되면 모바일 페이지로 리다이렉트
      if (mobilePattern.test(userAgent)) {
        this.$router.push("/mobile/home");
      }else{
        this.$router.push("/login");
      }
    }
  }
}
</script>

<style lang="scss" scoped>
body * {
  font-family: 'Pretendard-Regular';
  font-size: 13px;
}
</style>