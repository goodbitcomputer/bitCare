<template>
  <div class="editor-box">
    <nav-bar/>
    <Editor/>
  </div>
</template>

<script>
import Editor from "@/components/mobile/doctor/editor/Editor.vue";
import NavBar from "@/components/mobile/NavBar.vue";

export default {
  name: "ImageEditor",
  components: {
    NavBar,
    Editor
  }
}
</script>

<style scoped>
.editor-box {
  padding-top: 35px;
}
</style>