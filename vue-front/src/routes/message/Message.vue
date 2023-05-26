<template>
  <div>
    <b-tabs pills card vertical id="tabs">
      <b-tab active>
        <template v-slot:title>
          <b-icon-envelope-fill/>ReceiveList
        </template>
        <MessageList id="receive"/>
      </b-tab>

      <b-tab>
        <template v-slot:title>
          <b-icon-cursor-fill/>SendList
        </template>
        <SendList id="send"/>
      </b-tab>

      <b-tab>
        <template v-slot:title>
          <b-icon-brush-fill/>Send Message
        </template>
        <SendMessage id="message"/>
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
#tabs{
  height: 450px;
}

#receive{
  height: 400px;
  overflow-y: auto;
}

#message{
  height: 400px;
  overflow-y: auto;
}

#send{
  height: 400px;
  overflow-y: auto;
}
</style>