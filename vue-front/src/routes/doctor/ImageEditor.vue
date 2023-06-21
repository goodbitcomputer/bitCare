<template>
  <div>
    <nav-bar/>
    <div>
      <AnnouncementBoard/>
    </div>
    <Editor/>
  </div>
</template>

<script>
import Editor from "@/components/doctor/editor/Editor.vue";
import NavBar from "@/components/NavBar.vue";
import AnnouncementBoard from "@/components/home/Announcement.vue";

export default {
  name: "ImageEditor",
  components: {
    AnnouncementBoard,
    NavBar,
    Editor
  }
}
</script>

<style scoped>

</style>