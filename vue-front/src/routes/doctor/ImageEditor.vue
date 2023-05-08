<template>
  <div>
    <nav-bar/>
    <Editor/>
  </div>
</template>

<script>
import Editor from "@/components/doctor/editor/Editor.vue";
import NavBar from "@/components/NavBar.vue";

export default {
  name: "ImageEditor",
  components: {
    NavBar,
    Editor
  }
}
</script>

<style scoped>

</style>