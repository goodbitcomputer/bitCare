<template>
  <div class="border-box">
    <div class="border-box">
      <span>환자</span>
      <tr>
        <td colspan="5" class="">
          <ul class="pagination">
            <li class="page-item">
              <div class="page-link" @click="prev()">이전 페이지</div>
            </li>

            <li class="page-item">
              <div class="page-link" href="">${i}</div>
            </li>

            <li class="page-item">
              <div class="page-link" @click="next()">다음 페이지</div>
            </li>
          </ul>
        </td>
      </tr>
    </div>
  </div>
</template>

<script>

import axios from "axios";

export default {
  name: "MobileSearchPatient",
  data() {
    return {
      isSearchCheck: false,
      keyword: "",
      page: 1,

      // axios
      patientList: [],
      paginationData: "",
    }
  },
  mounted() {
    this.searchProc();
  },
  methods: {
    prev() {
      this.$router.push({path: "/mobile/search", query: {keyword: this.keyword}});
    },
    next() {
      this.$router.push({path: "/mobile/search", query: {keyword: this.keyword}});
    },
    setSearchData() {
      this.keyword = this.$route.query.keyword;
      if (this.$route.query.page !== undefined) {
        this.page = this.$route.query.page;
      }
    },
    setPatientList(item) {
      this.patientList = [];
      item.forEach((i) => {
        this.patientList.push({
          id: i.id,
          phoneNumber: i.phoneNumber,
          identityNumber: i.identityNumber,
          birth: i.birth,
          gender: i.gender,
          name: i.name,
          addressId: i.addressId
        })
      })
    },
    searchProc() {
      this.setSearchData();
      let keyword = this.keyword;
      let page = this.page;

      console.log("keyword");
      console.log(keyword);
      console.log("page");
      console.log(page);

      axios.post('/search/search_proc', {
        keyword: keyword,
        page: page,
      }).then((response) => {
        console.log(response.data);
        let patientList = JSON.parse(response.data.patientList);
        let paginationDTO = JSON.parse(response.data.paginationDTO);

        console.log("patientList");
        console.log(patientList);
        console.log("paginationDTO");
        console.log(paginationDTO);

        this.setPatientList(patientList);
        this.paginationData = paginationDTO;

      }).catch(function (error) {
        console.log(error);
      })
    }
  },
}
</script>

<style scoped>

</style>