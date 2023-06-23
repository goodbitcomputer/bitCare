<template>
  <div>
    <NavBar/>
    <div>
      <AnnouncementBoard/>
    </div>
    <div class="container">
      <ReceiptList/>
      <NoReceipt class="history-box flex-grow-1" />
      <RequestReceipt class="receipt-box"/>
    </div>
  </div>
</template>

<script>
import NoReceipt from "@/components/home/ReceiptList/NotReceipt.vue";
import NavBar from "@/components/NavBar.vue";
import AnnouncementBoard from "@/components/home/Announcement.vue";
import ReceiptList from "@/components/home/ReceiptList/ReceiptList.vue";
import RequestReceipt from "@/components/home/ReceiptList/RequestReceipt.vue";

export default {
  name: "ReceiptContent",
  components: {RequestReceipt, ReceiptList, AnnouncementBoard, NavBar, NoReceipt}
}
</script>

<style scoped>
@media (min-width: 1200px) {
  .container {
    display: flex;
  }
  .history-box {
    margin: 0 10px;
    max-width: 840px;
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