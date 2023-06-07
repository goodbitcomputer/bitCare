<template>
  <div class="container d-flex">
    <NurseWait/>
    <div class="flex-grow-1">
      <NursePatient class="flex-grow-1"/>
      <NurseHistory class="flex-grow-1"/>
    </div>
    <NurseReceipt style="width: 300px"/>
  </div>
</template>

<script>
import NurseWait from "@/components/nurse/Waiting.vue"
import NursePatient from "@/components/nurse/Patient.vue";
import NurseHistory from "@/components/nurse/History.vue";
import NurseReceipt from "@/components/nurse/Receipt.vue";

export default {
  name: "NurseView",
  components: {NurseWait, NurseReceipt, NurseHistory, NursePatient},
  data() {
    return {
      isClick: false,
      contentCss: ''
    };
  },
  methods: {}
}
</script>

<style scoped>
</style>