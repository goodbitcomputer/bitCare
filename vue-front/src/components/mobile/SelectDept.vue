<template>
  <section id="hero" class="d-flex align-items-center">
    <div class="container position-relative text-center text-lg-start" data-aos="zoom-in" data-aos-delay="100">
      <div class="row justify-content-center">
        <h1 style="font-weight: 500">진료과목 및 내원목적을</h1>
        <h1 style="font-weight: 500">입력해주세요.</h1>

        <div class="input-box">
          <b-dropdown
              :text="deptMsg"
              block
              variant="primary"
              class="btn-outline-dark mt-2 mb-2"
              menu-class="w-100"
              dropup
          >
            <b-dropdown-item v-for="(item) in deptList" :key="item.id" @click="dropClick(item)">{{ item.dept }}
            </b-dropdown-item>
          </b-dropdown>
          <b-input placeholder="내원목적(증상)을 입력해주세요." v-model="symptom"/>
        </div>

      </div>
      <!--      start 버튼 박스-->
      <div class="btn-util-box d-flex justify-content-center">
        <button class="util-btn left-util-btn btn bg-warning" @click="prevBtn" style="width: 120px; ">이전</button>
        <button class="util-btn btn bg-warning" @click="nextBtn" style="width: 120px;">다음</button>
      </div>
      <!--      end 버튼 박스-->
    </div>
  </section><!-- End Hero -->
</template>

<script>
import axios from "axios";
const Swal = window.Swal;
import {mapMutations, mapState} from "vuex";

export default {
  name: "SelectDept",
  data() {
    return {
      deptList: [],
      dept: "",
      deptId: "",
      symptom: "",
    }
  },
  mounted() {
    this.getDeptData()
  },
  computed: {
    ...mapState('mobile', {
      patientId: "patientId",
    }),
    deptMsg() {
      if (this.dept === "") return "진료과목을 선택해주세요.";
      else return this.dept;
    }
  },
  methods: {
    ...mapMutations('mobile', {
      setDeptId: 'setDeptId',
      setSymptom: 'setSymptom',
      setNextStep: 'setNextStep',
      initState: 'initState',
    }),
    dropClick(item) {
      this.dept = item.dept;
      this.deptId = item.id;
    },

    // 이전 버튼
    prevBtn() {
      this.initState();
    },
    // 다음 버튼
    nextBtn() {
      if (this.deptId !== "" && this.symptom !== "") {
        this.setDeptId(this.deptId);
        this.setSymptom(this.symptom);
        this.register();
        this.setNextStep(6);
      } else if (this.deptId === "") {
        Swal.fire({
          icon: 'error',
          title: 'error',
          text: '진료유형을 선택해주세요.',
          showConfirmButton: false,
          timer: 1000
        })
      } else if (this.symptom === "") {
        Swal.fire({
          icon: 'error',
          title: 'error',
          text: '내원목적을 입력해주세요.',
          showConfirmButton: false,
          timer: 1000
        })
      }
    },
    setDeptList(items) {
      this.deptList = [];
      items.forEach((item) => {
        this.deptList.push({
          id: item.id,
          dept: item.dept,
        })
      })
    },
    getDeptData() {
      return axios.get('/mobile/getDeptData', {}).then(response => {
        let list = response.data;
        this.setDeptList(list);
      }).catch(function (error) {
        console.log(error);
      });
    },
    // 접수
    register() {
      return axios.post('/mobile/register_proc', {
        patientId: this.patientId,
        deptId: this.deptId,
        symptom: this.symptom,
      }).then((response) => {
        if (response.data === false) {
          Swal.fire({
            icon: 'error',
            title: 'error',
            text: '이미 등록하셨습니다.',
            showConfirmButton: false,
            timer: 1000
          }).then(() => {
            this.$router.push('/mobile/home');
          })
        }
      }).catch(function (error) {
        console.log(error);
      })
    },
  }
}
</script>

<style scoped>
#hero {
  width: 100%;
  /*height: 100vh;*/
  background: none;
  background-color: #A1C7E0;
  background-size: cover;
  /* 배경이미지 반복여부 */
  background-repeat: no-repeat;
  /* 배경의 위치는 그 세션의 정가운데에 위치 */
  background-position: center;
  /* 더이상 요소가 스크롤될때 같이 스크롤되지않고 이미지가 뷰포트부분에 고정, 화면이 스크롤 되더라도 같이 움직이지않는구조로 만듬 */
  background-attachment: fixed;
  position: relative;
  padding: 0;
}

#hero:before {
  content: "";
  background: rgba(0, 0, 0, 0.6);
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  right: 0;
}

@media (min-width: 992px) {
  .input-box{
    width: 600px;
  }
  .util-btn {
    width: 250px !important;
    margin: 30px 0 0 0;
  }

  .left-util-btn {
    margin: 30px 265px 0 0;
  }

}

@media (max-width: 992px) {
  .input-box{
    width: 600px;
  }
  .util-btn {
    width: 180px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 95px 0 0;
  }

}

@media (max-width: 576px) {
  .input-box{
    width: 500px;
  }
  .util-btn {
    width: 135px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 95px 0 0;
  }

}

@media (max-width: 400px) {
  .input-box{
    width: 300px;
  }
  .util-btn {
    width: 100px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 40px 0 0;
  }

}

@media (max-width: 300px) {

  .util-btn {
    width: 100px !important;
    margin: 20px 0 0 0;
  }

  .left-util-btn {
    margin: 20px 20px 0 0;
  }

}
</style>