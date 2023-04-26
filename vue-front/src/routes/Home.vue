<template>
  <div>
  <div>
    <NavBar/>
  </div>
  <div class="home container">
    <Header/>
<!--    <img alt="Vue logo" src="../assets/logo.png">-->
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
  </div>
</template>

<script>
import HelloWorld from "@/components/HelloWorld.vue";
import NavBar from "@/components/NavBar.vue";

export default {
  name: 'HomeView',
  components: {
    NavBar,
    HelloWorld
  }
}
</script>

<style lang="scss" scoped>
  .home {
    text-align: center;
  }
</style>