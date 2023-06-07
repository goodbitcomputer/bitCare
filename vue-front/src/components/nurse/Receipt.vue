<template>
  <div>
    <div class="border-box d-flex">
      <span style="font-size: 1.2em; font-weight: 700; flex-grow: 1">수납 내역</span>
    </div>
    <div class="border-box">
      <div>
        <b-dropdown
            :text="visitMsg"
            block
            variant="primary"
            class="btn-outline-dark mt-2 mb-2"
            menu-class="w-100"
            dropup
        >
          <b-dropdown-item v-for="(item) in deptList" :key="item.id" @click="dropClick(item)">{{ item.dept }}
          </b-dropdown-item>
        </b-dropdown>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">총 진료비</label><span>16,970</span>
      </div>
      <div>
        <button class="btn btn-primary w-100" @click="receiptBtn" style="width: 120px">수납하기</button>
      </div>
    </div>
    <div class="border-box">
      <div><span class="font-weight-bold">카드</span></div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">결제시각</label><span>2023.06.07</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">수납금액</label><span>16,970</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">카드번호</label><span>1111111</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">승인번호</label><span>1111111</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">카드종류</label><span>국민</span>
      </div>
    </div>
    <div class="border-box">
      <div><span class="font-weight-bold">카카오페이</span></div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">결제시각</label><span>2023.06.07</span>
      </div>
      <div class="d-flex">
        <label class="font-weight-bold flex-grow-1">수납금액</label><span>16,970</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "NurseReceipt"
}
</script>

<style scoped>

</style>
