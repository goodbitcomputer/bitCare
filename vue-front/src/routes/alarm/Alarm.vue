<template>
  <div>
    <SendMessage/>
  </div>
</template>

<script>
//import MessageList from "@/components/alarm/MessageList.vue";
import SendMessage from "@/components/alarm/SendMessage.vue";
export default {
  name: "MessageAlarm",
  components: {
    SendMessage,
    //MessageList
  }
}
</script>

<style scoped>

</style>