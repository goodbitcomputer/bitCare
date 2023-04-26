<template>
  <div>
  <div>
    <NavBar/>
  </div>
  <div>
    <MessageList/>
  </div>
  </div>
</template>

<script>
import MessageList from "@/components/alarm/MessageList.vue";
import NavBar from "@/components/NavBar.vue";
export default {
  name: "MessageAlarm",
  components: {
    NavBar,
    MessageList
  }
}
</script>

<style scoped>

</style>