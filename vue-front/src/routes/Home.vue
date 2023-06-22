<template>
  <div>
    <NavBar/>
    <div>
      <AnnouncementBoard/>
    </div>
    <div class="container">
      <div class="detail border-box">
        <DeptSelect/>
      </div>
      <div class="list">
        <ClinicNow/>
        <div>
          <div class="container">
            <div class="todayHistory border-box">
              <WaitingNow/>
            </div>
            <div class="receipt border-box">
              <ReceiptNow/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "/public/assets/css/style.scss";
import NavBar from "@/components/NavBar.vue";
import ClinicNow from "@/components/home/ClinicNow.vue";
import WaitingNow from "@/components/home/WaitingNow.vue";
import DeptSelect from "@/components/home/DeptSelect.vue";
import ReceiptNow from "@/components/home/ReceiptNow.vue";
import AnnouncementBoard from "@/components/home/Announcement.vue";

export default {
  name: "DoctorView",
  components: {
    AnnouncementBoard,
    ReceiptNow,
    DeptSelect,
    WaitingNow,
    ClinicNow,
    NavBar,
  }
}
</script>
<style lang="scss" scoped>
.container {
  font-family: 'Pretendard-Regular';
  font-size: 13px;
  padding-top: 20px;
  //display: flex;
}

btn {
  color: #0b2e13;
  background-color: #0b2e13;
}

.detail {
  width: 100%;
}

.todayHistory {
  width: 100%;
}

.selectOne {
  width: 100%;
}

.announcement {
  width: 100%;
}

// sm
//@media (min-width: 576px) { ... }
// md
//@media (min-width: 768px) { ... }
// lg
@media (min-width: 1200px) {
  .container {
    padding-top: 20px;
    display: flex;
  }
  .detail {
    width: 20%;
  }
  .list{
    width: 80%;
  }
  .todayHistory {
    width: 50%;
    height: 520px;
    overflow-y: auto;
  }
  .receipt {
    width: 50%;
    height: 520px;
    overflow-y: auto;
  }
}


// xl
//@media (min-width: 1200px) { ... }
// xxl
//@media (min-width: 1400px) { ... }
</style>