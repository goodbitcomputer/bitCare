<template>
  <div class="temp">
    <NavBar/>
    <Auth v-if="authByStep"/>
    <InsertPhoneIdentity v-if="insertPhoneIdentityByStep"/>
    <InsertName v-if="insertNameByStep"/>
    <InsertAddress v-if="insertAddressByStep"/>
    <SelectDept v-if="selectDeptByStep"/>
    <RegisterClear v-if="registerClearByStep"/>

  </div>
</template>

<script>
import {mapState} from "vuex";
import NavBar from "@/components/mobile/NavBar.vue";
import Auth from "@/components/mobile/Auth.vue";
import InsertPhoneIdentity from "@/components/mobile/InsertPhoneIdentity.vue";
import InsertName from "@/components/mobile/InsertName.vue";
import InsertAddress from "@/components/mobile/InsertAddress.vue";
import SelectDept from "@/components/mobile/SelectDept.vue";
import RegisterClear from "@/components/mobile/RegisterClear.vue";

export default {
  name: "MobileRegister",
  components: {
    RegisterClear,
    NavBar,
    Auth,
    InsertPhoneIdentity,
    InsertName,
    InsertAddress,
    SelectDept,
  },
  data() {
    return {
      // auth: this.setAuthByStep,
      // insertPhoneIdentity: false,
      // insertName: false,
      // insertAddress: false,
      // selectDept: false,


    }
  },
  computed: {
    ...mapState('mobile',
      ['nextStep','phoneNumber', 'identityNumber']
    ),
    authByStep() {
      return this.nextStep===1 ? true : false;
    },
    insertPhoneIdentityByStep() {
      return this.nextStep===2 ? true : false;
    },
    insertNameByStep() {
      return this.nextStep===3 ? true : false;
    },
    insertAddressByStep() {
      return this.nextStep===4 ? true : false;
    },
    selectDeptByStep() {
      return this.nextStep===5 ? true : false;
    },
    registerClearByStep() {
      return this.nextStep===6 ? true : false;
    }
  },
  methods: {
  }

}
</script>

<style lang="scss" scoped>
//@import "/public/assets/css/style.scss";

.hide {
  display: none !important;
}
</style>