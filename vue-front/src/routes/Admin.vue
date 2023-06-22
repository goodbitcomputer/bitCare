<template>
  <div>
    <NavBar/>
    <div>
      <AnnouncementBoard/>
    </div>
    <Admin/>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import Admin from "@/components/admin/Admin.vue";
import AnnouncementBoard from "@/components/home/Announcement.vue";

export default {
  name: "AdminRoute",
  components: {
    AnnouncementBoard,
    NavBar,
    Admin
  }
}
</script>

<style scoped>

</style>