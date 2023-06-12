<template>
  <div>
    <nav-bar/>
    <Auth v-if="authByStep"/>
    <ReceiptList v-if="receiptListByStep"/>
    <paymentDetail v-if="paymentDetailByStep"/>
  </div>
</template>

<script>
import paymentDetail from "@/components/mobile/payment/PaymentDetail.vue";
import Auth from "@/components/mobile/payment/Auth.vue";
import ReceiptList from "@/components/mobile/payment/ReceiptList.vue";
import {mapState} from "vuex";
import NavBar from "@/components/mobile/NavBar.vue";

export default {
  name: "MobilePayment",
  components: {
    NavBar,
    ReceiptList,
    Auth,
    paymentDetail,
  },
  computed: {
    ...mapState('mobilePayment',
        ['nextStep',]
    ),
    authByStep() {
      return this.nextStep===1 ? true : false;
    },
    receiptListByStep() {
      return this.nextStep===2 ? true : false;
    },
    paymentDetailByStep() {
      return this.nextStep===3 ? true : false;
    },
  },
}
</script>

<style scoped>

</style>