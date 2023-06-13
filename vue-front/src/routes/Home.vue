<template>
  <div>
    <NavBar/>
    <AnnouncementBoard/>
    <div class="container">
      <!-- 오늘의 접수/수납 건수 -->
      <div class="detail border-box">
        <!-- 의사 목록 -->
      </div>
      <div class="todayHistory border-box">
        <!-- 오늘의 의사가 본 진료목록 -->
      </div>
      <div class="selectOne border-box">
        <!-- 진료 기록 -->
      </div>
    </div>
  </div>
</template>

<script>
import "/public/assets/css/style.scss";
import NavBar from "@/components/NavBar.vue";
import AnnouncementBoard from "@/components/home/Announcement.vue";

export default {
  name: "DoctorView",
  components: {
    AnnouncementBoard,
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

.announcement{
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
  .announcement{
    width: 100%;
  }
  .detail {
    width: 60%;
  }
  .history {
    width: 30%;
  }
  .write {
    width: 30%;
  }
}

// xl
//@media (min-width: 1200px) { ... }
// xxl
//@media (min-width: 1400px) { ... }
</style>