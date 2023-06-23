<template>
  <div class="row justify-content-center">
    <div class = "col-7" style="top: 50px">
      <table class="table table-primary table-hover">
        <thead>
        <tr>
          <th>이름</th>
          <th>아이디</th>
          <th>현재 권한</th>
          <th>권한 설정</th>
          <th>설정 저장</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(employee,index) in this.$store.state.login.list" :key="employee.id">
          <td>{{employee.name}}</td>
          <td>{{employee.username}}</td>
          <td>{{ formatRole(employee.role) }}</td>
          <td>
            <b-select v-model="role[index]">
              <option v-for="role in roles" :key="role">{{role}}</option>
            </b-select>
<!--            </b-select>-->
<!--            <b-form-input v-model="role[index]" list="roles"/>-->
<!--            <b-datalist id="roles">-->
<!--              <option v-for="role in roles" :key="role">{{role}}</option>-->
<!--            </b-datalist>-->
          </td>
          <td>
            <button type="button" class="btn btn-primary btn-sm" @click="saveRole(employee,index)">저장</button>
          </td>
        </tr>
        </tbody>
      </table>
      <button type="button" class="btn btn-primary btn-sm" @click="showDetails()">계정 추가</button>
      <button type="button" class="btn btn-primary btn-sm" @click="writeAnnouncement()" style="margin: 20px">공지 쓰기</button>
    </div>
    <div>
      <b-modal v-model="this.$store.state.login.registerModal" id="modal" size="lg" title="계정 추가" hide-footer @hidden="closeModal">
        <div id="register">
          <EmployeeRegister/>
        </div>
      </b-modal>
    </div>

    <div>
      <b-modal v-model="this.$store.state.alarm.announcementModal" id="modal" size="lg" title="공지 쓰기" hide-footer @hidden="closeModal">
        <div id="announcement">
          <WriteAnnouncement/>
        </div>
      </b-modal>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {mapMutations, mapState} from "vuex";
import EmployeeRegister from "@/components/admin/EmployeeRegister.vue";
import WriteAnnouncement from "@/components/home/WriteAnnouncement.vue";

export default {
  name: "AdminSetting",
  components: {WriteAnnouncement, EmployeeRegister},
  data(){
    return {
      showDetailsModal: false,
      role: [],
      roles: ['의사', '간호사'],
      selectRole: '',
      dummy: 0,
    }
  },
  created(){
    this.getEmployeeList()
    for(let index in this.$store.state.login.list){
      this.dummy = index
      this.role.push('')
    }
  },
  computed: {
    ...mapState('login',
        ['list', 'registerModal']
    ),
    ...mapState('alarm',
        ['announcementModal']
    )

  },
  methods: {
    ...mapMutations('login',{
      setList: 'setList',
      setRegisterModal: 'setRegisterModal',
    }),
    ...mapMutations('alarm',{
      setAnnouncementModal: 'setAnnouncementModal',
    }),
    saveRole(employee, index) {
      // API를 호출해서 해당 메시지를 삭제합니다.
      // 성공적으로 삭제되면 this.settingRecvList()를 호출합니다.
      if(this.role[index]!=='의사' && this.role[index]!=='간호사'){
        window.Swal.fire({
          icon: 'error',
          title: 'error',
          text: '권한을 선택해주세요.',
          timer: 3000
        })
      }else {
        if(this.role[index]==='의사'){
          this.selectRole = 'ROLE_DOCTOR'
        } else if(this.role[index]==='간호사'){
          this.selectRole = 'ROLE_NURSE'
        }
        axios.get('api/updateEmployee', {
          params: {
            id: employee.id,
            role: this.selectRole
          }
        }).then(response => {
          if (response.status === 200) {
            console.log('권한 변경 완료')
          } else {
            console.log('권한 변경 실패')
          }
        })

        this.role[index] = ''
        setTimeout(() => this.getEmployeeList(), 100)
      }
    },
    formatRole(role){
      if(role==='ROLE_DOCTOR'){
        return '의사'
      }else if(role==='ROLE_NURSE'){
        return '간호사'
      }
    },
    getEmployeeList(){
      axios.get('/api/selectAll')
          .then(response => {
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let employeeList = JSON.parse(JSON.stringify(response.data.employeeList));
              this.setList(employeeList)
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
    showDetails() {
      this.showDetailsModal = true;
      this.setRegisterModal(this.showDetailsModal);
    },
    closeModal() {
      this.showDetailsModal = false;
      this.setRegisterModal(this.showDetailsModal);
      this.setAnnouncementModal(this.showDetailsModal);
    },
    writeAnnouncement() {
      this.showDetailsModal = true;
      this.setAnnouncementModal(this.showDetailsModal);
    },
  }
}
</script>

<style scoped>
#register {
  height: 720px; overflow-y: auto;
}
</style>