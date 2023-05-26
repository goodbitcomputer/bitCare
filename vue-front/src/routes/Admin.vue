<template>
  <div>
    <NavBar/>
    <Admin/>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import Admin from "@/components/admin/Admin.vue";
export default {
  name: "AdminRoute",
  components: {
    NavBar,
    Admin
  }
}
</script>

<style scoped>

</style>