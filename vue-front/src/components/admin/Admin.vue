<template>
  <div class="row justify-content-center">
    <div class = "col-12">
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
          <td>{{employee.role}}</td>
          <td>
            <b-form-input v-model="role[index]" list="roles"/>
            <b-datalist id="roles">
              <option v-for="role in roles" :key="role">{{role}}</option>
            </b-datalist>
          </td>
          <td>
            <button type="button" class="btn btn-primary btn-sm" @click="saveRole(employee,index)">저장</button>
          </td>
        </tr>
        </tbody>
      </table>
      <button type="button" class="btn btn-primary btn-sm" >계정 추가</button>
    </div>
  </div>

</template>

<script>
import axios from "axios";
import {mapMutations, mapState} from "vuex";

export default {
  name: "AdminSetting",
  data(){
    return {
      role: [],
      roles: ['ROLE_DOCTOR', 'ROLE_NURSE']
    }
  },
  created(){
    this.getEmployeeList()
    for(let index in this.$store.state.login.list){
      console.log(index)
      this.role.push('')
    }
  },
  computed: {
    ...mapState('login',
        ['list']
    )
  },
  methods: {
    ...mapMutations('login',{
      setList: 'setList'
    }),
    saveRole(employee, index) {
      // API를 호출해서 해당 메시지를 삭제합니다.
      // 성공적으로 삭제되면 this.settingRecvList()를 호출합니다.
      axios.get('api/updateEmployee', {
        params: {
          id: employee.id,
          role: this.role[index]
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
    },
    getEmployeeList(){
      axios.get('/api/selectAll')
          .then(response => {
            console.log(response.data);
            // 세션 데이터 사용 예시
            if (response.data) {
              this.isLogin = true
              let employeeList = JSON.parse(JSON.stringify(response.data.employeeList));
              this.setList(employeeList)
              console.log(employeeList)
            } else {
              console.log('로그인되어 있지 않습니다.');
            }
          })
          .catch(error => {
            console.error('세션 데이터를 가져오는 중 에러 발생: ', error);
          });
    },
  }
}
</script>

<style scoped>

</style>