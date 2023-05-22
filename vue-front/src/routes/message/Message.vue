<template>
  <div>
    <b-tabs pills>
      <b-tab active>
        <template v-slot:title>
          <b-icon-envelope-fill/>ReceiveList
        </template>
        <MessageList/>
      </b-tab>

      <b-tab>
        <template v-slot:title>
          <b-icon-cursor-fill/>SendList
        </template>
        <SendList/>
      </b-tab>

      <b-tab>
        <template v-slot:title>
          <b-icon-brush-fill/>Send Message
        </template>
        <SendMessage/>
      </b-tab>
    </b-tabs>
  </div>
</template>

<script>
import MessageList from "@/components/alarm/MessageList.vue";
import SendMessage from "@/components/alarm/SendMessage.vue";
import SendList from "@/components/alarm/SendList.vue";
export default {
  name: "MessageView",
  components: {
    SendMessage,
    MessageList,
    SendList
  }
}
</script>

<style scoped>

</style>