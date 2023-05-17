<template>
  <!-- 쪽지 작성 페이지 -->
    <div>
      <h1>Message Sender</h1>
      <vue-editor v-model="messageContent"></vue-editor>
      <button @click="sendMessage">Send</button>
    </div>
</template>

<script>
import {VueEditor} from "vue2-editor";

export default {
  name: "SendMessage",
  methods: {
    sendMessage() {
      // 여기에 메시지를 전송하는 로직을 작성하세요
    }
  },
  components: {
    VueEditor
  }
}
</script>

<style scoped>

</style>