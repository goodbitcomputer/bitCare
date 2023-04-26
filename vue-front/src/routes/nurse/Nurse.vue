<template>
  <div>
  <div>
    <NavBar/>
  </div>
  <div>
<span>nurse</span>
  </div>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";

export default {
  name: "NurseView",
  components: {
    NavBar
  }
}
</script>

<style scoped>
body * {
  font-family: 'Pretendard-Regular';
  font-size: 13px;
}
</style>