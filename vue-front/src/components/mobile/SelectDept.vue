<template>
  <section id="hero" class="d-flex align-items-center" style="background: none">
    <div class="container position-relative text-center text-lg-start" data-aos="zoom-in" data-aos-delay="100">
      <div class="row justify-content-center">
        <h1 style="font-weight: 500">진료과목 및 내원목적을</h1>
        <h1 style="font-weight: 500">입력해주세요.</h1>

        <div style="width: 500px">
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
        <div class="mr-5 mt-5">
          <button class="btn btn-dark" @click="prevBtn" style="width: 120px">이전</button>
        </div>
        <div class="ml-5 mt-5">
          <button class="btn btn-primary" @click="nextBtn" style="width: 120px">다음</button>
        </div>
      </div>
      <!--      end 버튼 박스-->
    </div>
  </section><!-- End Hero -->
</template>

<script>
import axios from "axios";

const Swal = window.Swal;
import {mapActions, mapMutations, mapState} from "vuex";

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
    ...mapActions('mobile', {
      insertRegister: 'insertRegister'
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
        console.log(this.$store.state.mobile.patientId);
        console.log(typeof this.$store.state.mobile.patientId);
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
      return axios.post('/mobile/register', {
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
            this.$router.push('m.home');
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

</style>