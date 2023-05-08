<template>
  <div>
    <div class="border-box">
      <span style="font-size: 1.2em; font-weight: 700">진료 기록 작성</span>
    </div>
    <!--    증상-->
    <div class="border-box">
      <div class="d-flex">
        <span class="font-weight-bold flex-grow-1">증상</span>
        <button @click="editorBtn">편집</button>
      </div>
      <div class="symptom-box">
        <div class="symptomInfo-box">
          <textarea id="symptom-editor" name="symptom"></textarea>
        </div>
        <div class="writeImg-box">
          <img src="@/assets/history/s2.jpg">
        </div>
      </div>
    </div>

    <!--    상병-->
    <div class="border-box">
      <span class="font-weight-bold">상병</span>
      <div>
        <b-table :items="writeSbList" :fields="sbFields" small>
          <template #cell(main)="data">
            <b-form-radio
                v-model="data.item.main"
                :value="true"
                @change="handleRadioChange(data.item)"
            ></b-form-radio>
          </template>
          <template #cell(sub)="data">
            <b-form-radio
                v-model="data.item.main"
                :value="false"
                @change="handleRadioChange(data.item)"
            ></b-form-radio>
          </template>
          <template #cell(icon)="data">
            <b-icon
                icon="dash-circle" variant="danger"
                v-model="data.item.radio1"
                :value="true"
                @click="removeWriteSbList(data.item)"
            ></b-icon>
          </template>
          <template #cell(name)="data">
            <div class="ellipsis-name td-box-name">
              {{ data.value }}
            </div>
          </template>
        </b-table>
        <!--        상병추가모달-->
        <writeSb-model/>
      </div>
    </div>
    <!--    처방-->
    <div class="border-box">
      <span class="font-weight-bold">처방</span>
      <div>
        <b-table :items="writeCbList" :fields="cbFields" small>
          <template #cell(icon)="data">
            <b-icon
                icon="dash-circle" variant="danger"
                v-model="data.item.radio1"
                :value="true"
                @click="removeWriteCbList(data.item)"
            ></b-icon>
          </template>
          <template #cell(name)="data">
            <div class="ellipsis-name td-box-name">
              {{ data.value }}
            </div>
          </template>
        </b-table>
        <!--        상병추가모달-->
        <writeCb-model/>
      </div>
    </div>
    <div class="d-flex justify-content-end">
      <b-button class="w-25 m-1" variant="dark">초기화</b-button>
      <b-button class="w-25 m-1" variant="primary">진료 완료</b-button>
    </div>
  </div>
</template>

<script>

import WriteSbModel from "@/components/doctor/WriteSbModal.vue";
import WriteCbModel from "@/components/doctor/WriteCbModal.vue";
import {mapState, mapMutations} from 'vuex';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';


export default {
  name: "DoctorHistoryWrite",
  components: {
    WriteSbModel,
    WriteCbModel,
  },

  mounted() {
    // document.addEventListener('DOMContentLoaded', () => {
    //   // INSERT CODE HERE
    //   this.setTest(this.items)
    // });

    ClassicEditor.create(document.querySelector('#symptom-editor'), {
      contentCss: this.contentCss,
      toolbar: [
        // 'heading',
        // '|',
        'bold',
        'italic',
        'link',
        'bulletedList',
        '|',
        'undo',
        'redo',
        // '|',
        // 'imageUpload',
        // 'alignment',
        // 'numberedList',
        // 'imageInsert',
        // 'blockQuote',
        // '|',
        // 'ckfinder',
      ],
    }).catch((error) => {
      console.error(error);
    });
  },

  data() {
    return {
      sbFields: [
        {key: 'main', label: '주상병'},
        {key: 'sub', label: '부상병'},
        {key: 'code', label: '코드'},
        // {key: 'radio1', label: '선택1', thClass: 'text-center'},
        {key: 'name', label: '명칭'},
        {key: 'icon', label: 'remove'},
      ],
      cbFields: [
        {key: 'code', label: '코드'},
        {key: 'name', label: '명칭'},
        {key: 'dose', label: '용량'},
        {key: 'time', label: '일투수'},
        {key: 'days', label: '일수'},
        {key: 'icon', label: 'remove'},
      ],
    }
  },

  // vuex
  computed: {
    ...mapState('doctor',
        ['sbList', 'writeSbList', 'writeCbList']
    )
  },
  methods: {
    ...mapMutations('doctor', {
      // 상병테이블 remove icon  write상병테이블 한줄 삭제
      removeWriteSbList: 'removeWriteSbList',
      // 처방테이블 remove icon   write처방테이블 한줄 삭제
      removeWriteCbList: 'removeWriteCbList',
    }),
    // // 상병테이블 remove icon  write상병테이블 한줄 삭제
    // removeSbButton(item) {
    //   this.removeWriteSbList(item);
    // },
    // // 처방테이블 remove icon   write처방테이블 한줄 삭제
    // removeCbButton(item) {
    //   this.removeWriteCbList(item);
    // },

    // 라디오버튼 누를때 값 가져오는거
    handleRadioChange(item) {
      // 선택된 값을 가져와서 저장합니다.
      if (item.main) {
        this.selectedItems.push({id: item.id, value: 'main'});
      } else if (item.sub) {
        this.selectedItems.push({id: item.id, value: 'sub'});
      } else {
        this.selectedItems = this.selectedItems.filter((i) => i.id !== item.id);
      }
    },

    // editorBtn
    editorBtn() {
      this.$router.push('/doctor/editor');
    }

  }
}
</script>

<style lang="scss" scoped>
.symptom-box {
  display: flex;
}

.symptomInfo-box {
  flex-grow: 1;
}

.writeImg-box {
  width: 150px;
}

.writeImg-box img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.ellipsis-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px; /* 요소의 최대 너비를 지정합니다. */
}

.td-box-name {
  width: 200px;
}


</style>
