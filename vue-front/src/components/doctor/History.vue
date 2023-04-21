<template>
  <div class="history-box border-box">
    <div>
      <!--      history 리스트-->
      <div class="border-box">
        <div>
          <span>2022.01.06</span>
        </div>
        <div>
          <span>초진</span>
        </div>
      </div>
    </div>
    <div class="history-detail-box border-box">
      <!--      진료기록title-->
      <div>
        <span>진료기록</span>
        <span>[2022.01.06]</span>
      </div>
<!--      신체계측/바이탈-->
      <div>
        <span class="font-weight-bold">신체계측/바이탈</span>
        <div class="table-wrapper">
          <b-table hover :items="pyItems" :fields="pyFields" small>
            <template #cell(code)="data">
              <div class="">
                {{ data.value }}
              </div>
            </template>
            <template #cell(name)="data">
              <div class="ellipsis-name">
                {{ data.value }}
              </div>
            </template>
          </b-table>
        </div>
      </div>
      <!--      의사정보-->
      <div class="doctor-info">
        <span>건강보험</span>
        <span>초진</span>
        <span>외래진료</span>
        <span>내과</span>
        <span>백지영</span>
      </div>
      <!--      진료기록 상세정보-->
      <div>
        <span class="font-weight-bold">증상</span>
        <div style="padding: 5px; border: 1px solid #DBDFE5; border-radius: 5px;">
          <p>이틀전부터 목 불편함. 목이 칼칼하다. 헛기침</p>
        </div>

      </div>
<!--      상병-->
      <div>
        <span class="font-weight-bold">상병</span>
        <div>
          <div class="table-wrapper">
            <b-table hover :items="sbItems" :fields="sbFields"
                     :tbody-tr-class="rowClass" small>
              <template #cell(sb)="data">
                <div class="ellipsis-sb td-box-sb">
                  {{ data.value }}
                </div>
              </template>
              <template #cell(code)="data">
                <div class="ellipsis-code td-box-code">
                  {{ data.value }}
                </div>
              </template>
              <template #cell(name)="data">
                <div class="ellipsis-name td-box-name">
                  {{ data.value }}
                </div>
              </template>
            </b-table>
          </div>
        </div>
      </div>
<!--      처방-->
      <div>
        <span class="font-weight-bold">처방</span>
        <div>
          <div class="table-wrapper">
            <b-table hover :items="cbItems" thead-class="hidden_header"
                     :tbody-tr-class="rowClass" small>
              <template #cell(code)="data">
                <div class="">
                  {{ data.value }}
                </div>
              </template>
              <template #cell(name)="data">
                <div class="ellipsis-name">
                  {{ data.value }}
                </div>
              </template>
            </b-table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DoctorHistory",

  // Note `isActive` is left out and will not appear in the rendered table
  data() {
    return {
      // 신체계측/바이탈 테이블 로직
      pyFields: [
        {key: 'height', label: '키'},
        {key: 'weight', label: '체중'},
        {key: 'bpSystolic', label: '햘압(수축)'},
        {key: 'bpDiastolic', label: '혈압(이완)'},
        {key: 'temperature', label: '체온'},
      ],
      pyItems: [
        {height: '178', weight: '73', bpSystolic: '128', bpDiastolic: "87", temperature: "36.5"},
      ],

      // 상병 테이블 로직
      sbFields: [
        {key: 'sb', label: '주/부', sortable: true},
        {key: 'code', label: '코드', sortable: true},
        {key: 'name', label: '명칭', sortable: true},
      ],
      sbItems: [
        {sb: '주상병', code: 'Dickerson', name: 'Macdonaldsdsadassssssssssssssssdadadsadsadsasadasdd'},
        {sb: '부상병', code: 'Larsen', name: 'Shaw'},
        {sb: '부상병', code: 'Geneva', name: 'Wilson'},
        {sb: '부상병', code: 'Jami', name: 'Carney'}
      ],
      specialData: "주상병", // 특정 속성 데이터

      // 처방 테이블 로직
      // cbFields: ["sb", "code", "name"],
      cbItems: [
        {code: '645700880', name: '세토펜정80밀리그램(어쩌구저쩌구ㅇㄴㅁㅇㅁㅇㄴㅁㅇㄴㅁ)', dose: '1', time: "1", days: "1"},
        {code: '653403810', name: 'DickersonDickersonDickersonDickersonDickersonDickersonDickerson', dose: '1', time: "1", days: "1"},
        {code: '653403812', name: '아르도민캡슐(에르도스도르도르말말마람람)', dose: '1', time: "1", days: "1"},
        {code: '645700883', name: '알게이트정(120밀리그람)', dose: '1', time: "1", days: "1"},
      ],
    }
  },
  methods: {
    // 상병 테이블 로직
    rowClass(item) {
      // 해당 행의 name 속성이 specialData와 일치하는지 확인
      if (item.sb === this.specialData) {
        return "font-weight-bold ellipsis"; // 굵은 글꼴 클래스 반환
      }
    },


  },

}
</script>

<style lang="scss" scoped>
b-table * * {
  padding: 0 !important;
  margin: 0 !important;
}

.history-box {
  display: flex;
}
// 의사정보 box
.doctor-info {
  text-align: center;
}
.doctor-info span {
  font-size: 12px;
  font-weight: 600;
  // 글자 줄바꿈 안되게 함.
  white-space: nowrap;
}
.doctor-info span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 3px 0px 3px;
}


.ellipsis-sb {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 50px; /* 요소의 최대 너비를 지정합니다. */
}

.ellipsis-code {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px; /* 요소의 최대 너비를 지정합니다. */
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 150px; /* 요소의 최대 너비를 지정합니다. */
}


.table-wrapper {
  display: flex;
}

.table-wrapper > b-table {
  flex: 1;
}

.td-box-sb {
  width: 50px;
}

.td-box-code {
  width: 50px;
}

.td-box-name {
  width: 150px;
}


.history-detail-box {
  flex-grow: 1;
}

</style>
<style>
.hidden_header {
  display: none;
}
.table-body-box{
  padding: 0;
}
</style>
