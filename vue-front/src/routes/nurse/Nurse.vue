<template>
  <div>
    <NavBar/>
    <div>
      <AnnouncementBoard/>
    </div>
    <div class="container">
      <NurseWait/>
      <NurseHistory class="history-box flex-grow-1" />
      <NurseReceipt class="receipt-box"/>
    </div>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar.vue";
import NurseWait from "@/components/nurse/Waiting.vue"
import NurseHistory from "@/components/nurse/History.vue";
import NurseReceipt from "@/components/nurse/Receipt.vue";
import AnnouncementBoard from "@/components/home/Announcement.vue";

export default {
  name: "NurseView",
  components: {AnnouncementBoard, NavBar, NurseWait, NurseReceipt, NurseHistory},
  data() {
    return {};
  },
  methods: {}
}
</script>

<style scoped>
@media (min-width: 1200px) {
  .container {
    display: flex;
  }
  .history-box {
    margin: 0 10px;
  }
  .receipt-box {
    width: 300px;
  }
}
@media (max-width: 1200px) {
  .history-box {
    margin: 20px 0px;
  }
}
</style>