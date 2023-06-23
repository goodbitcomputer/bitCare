<template>
  <div>
    <section class="notice">
      <div class="notice-line">
        <div class="bg-left"></div>
        <keep-alive>
          <div v-if="!this.$store.state.alarm.catastrophe" class="inner">
            <div class="inner__beforeLeft"/>
            <div class="inner__left">
              <h2>공지사항</h2>
              <swiper class="swiper" :options="swiperOption">
                <swiper-slide v-for="announcement in this.$store.state.alarm.announcementList" :key="announcement.id">
                  <span class="font-weight-bold" style="font-size: 1.0em; color: white;"
                        @click="selectAnnouncement(announcement)">
                    {{ formatDate(announcement.entryDate) }} - {{ announcement.sender }}
                  </span>
                </swiper-slide>
              </swiper>
              <button class="material-icons" style="color: white;" @click="catastropheButton">add_circle</button>
            </div>
          </div>
        </keep-alive>
        <keep-alive>
          <div v-if="this.$store.state.alarm.catastrophe" class="inner" style="height: 175px !important;">
            <div class="inner__beforeLeft"/>
            <div class="inner__left">
              <h2 style="position: relative; height: 175px !important; top: 7px;">공지사항</h2>
              <swiper class="swiper" :options="catastropheOption" style="height: 175px !important;">
                <swiper-slide v-for="announcement in this.$store.state.alarm.announcementList" :key="announcement.id">
                  <span class="font-weight-bold" style="font-size: 1.0em; color: white;"
                        @click="selectAnnouncement(announcement)">
                    {{ formatDate(announcement.entryDate) }} - {{ announcement.sender }}
                  </span>
                </swiper-slide>
              </swiper>
              <button class="material-icons" style="position: relative; color: white; top: -70px;" @click="rollbackButton">remove_circle</button>
            </div>
          </div>
        </keep-alive>
      </div>
    </section>

    <div>
      <b-modal v-model="this.$store.state.alarm.showAnnouncementModal" size="lg" title="공지 내용" hide-footer @hidden="closeAnnouncementModal">
        <div v-html="this.$store.state.alarm.selectedAnnouncement.content"></div>
      </b-modal>
    </div>


    <!--    <div class="list-box">-->

    <!--      &lt;!&ndash; 공지 목록 &ndash;&gt;-->
    <!--      <div class="empty-box border-box" v-if="this.$store.state.alarm.announcementList === null">-->
    <!--        <div class="empty-img-box">-->
    <!--          <img src="@/assets/img/empty-box.png">-->
    <!--        </div>-->
    <!--      </div>-->
    <!--      <div class="selector-box border-box" v-else>-->
    <!--        <div v-for="announcement in this.$store.state.alarm.announcementList" :key="announcement.id">-->
    <!--          <div class="border-box" @click="selectAnnouncement(announcement)">-->
    <!--            <span class="font-weight-bold" style="font-size: 1.0em;">{{ announcement.sender }}</span>-->
    <!--            <br>-->
    <!--            <span style="flex-grow: 1; font-size: 1.0em;">{{ formatDate(announcement.entryDate) }}</span>-->
    <!--          </div>-->
    <!--        </div>-->
    <!--      </div>-->

    <!--      <div class="select-box border-box">-->
    <!--        <span v-html="this.$store.state.alarm.selectedAnnouncement.content"></span>-->
    <!--      </div>-->
    <!--    </div>-->
  </div>
</template>

<script>
import axios from "axios";
import {mapMutations, mapState} from "vuex";
import {Swiper, SwiperSlide} from "vue-awesome-swiper";

export default {
  components: {Swiper, SwiperSlide},
  data() {
    return {
      announcement: [],
      showDetailsModal: false,
      swiperOption: {
        // spaceBetween: 1000,
        slidesPerView: 1,
        speed: 300,
        allowTouchMove: false, // 터치 이동  x

        direction: 'vertical',
        height: 35,
        autoplay: {
          delay: 3000,
        },
        loop: true,
      },
      catastropheOption: {
        // spaceBetween: 1000,
        slidesPerView: 1,
        speed: 300,
        allowTouchMove: true, // 터치 이동  x

        direction: 'vertical',
        height: 35,
        autoplay: false,
        loop: false,
      },
    }
  },
  name: "AnnouncementBoard",
  computed: {
    ...mapState('alarm',
        ['announcementList', 'selectedAnnouncement', 'announcementModal', 'catastrophe', 'showAnnouncementModal']
    ),
  },
  created() {
    this.settingAnnouncementList()
  },
  methods: {
    ...mapMutations('alarm', {
      setAnnouncementList: 'setAnnouncementList',
      setSelectedAnnouncement: 'setSelectedAnnouncement',
      setAnnouncementModal: 'setAnnouncementModal',
      setCatastrophe: 'setCatastrophe',
      setShowAnnouncementModal: 'setShowAnnouncementModal',
    }),
    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    settingAnnouncementList() {
      // Axios를 사용하여 RESTful API 호출
      axios.get('/api/announcementList')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let announcementList = JSON.parse(JSON.stringify(response.data.announcementList));
              this.announcement = announcementList
              this.setAnnouncementList(this.announcement)
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    selectAnnouncement(announcement) {
      this.setSelectedAnnouncement(announcement);
      this.setShowAnnouncementModal(true);
    },
    closeModal() {
      this.showDetailsModal = false;
      this.setAnnouncementModal(this.showDetailsModal);
    },
    catastropheButton() {
      this.setCatastrophe(true);
    },
    rollbackButton() {
      this.setCatastrophe(false);
      this.setSelectedAnnouncement({});
    },
    closeAnnouncementModal(){
      this.showDetailsModal = false;
      this.setShowAnnouncementModal(this.showDetailsModal);
    },
  }
}
</script>

<style scoped>
.announcement-box {
  margin: 0 auto;
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.empty-box {
  height: 140px;
  display: flex;
}

.selector-box {
  height: 550px;
  overflow-y: auto;
}

.empty-img-box {
  width: 70px;
  margin: 0 auto;
  display: flex;
  vertical-align: middle;
  justify-content: center;
}

.empty-img-box img {
  width: inherit;
  object-fit: contain;
}

.util button {
  background: rgba(12, 11, 9, 0.7);
  color: white;
}

#modal {
  z-index: 999;
  display: block;
}

#announcement {
  height: 480px;
  overflow-y: auto;
}
</style>