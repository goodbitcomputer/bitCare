<template>
  <div>
    <div class="border-box">
      <span style="font-size: 1.2em; font-weight: 700">진료 기록</span>
    </div>
    <div class="empty-box border-box" v-if="true">
      <img src="@/assets/img/empty-box.png">
    </div>
    <div class="Patient-box border-box" v-if="false">

      <div style="border-right: 1px solid #DBDFE5;">
        <div style="display: flex;align-items: center;">
          <span class="font-weight-bold">{{ this.waitingData.name }}</span>
          <span style="flex-grow: 1">cn.{{ this.waitingData.patientId }}</span>
          <button>...</button>
        </div>
        <div class="patient-info">
          <span>{{ identityNumberMsg }}</span>
          <span>{{ ageMsg }}세</span>
          <span>{{ this.waitingData.gender }}</span>
          <span>{{ phonePadMsg }}</span>
        </div>
        <div style="border-top: 1px solid #DBDFE5;">
          <p class="font-weight-bold">접수메모</p>
          <p>{{ this.waitingData.symptom }}</p>
        </div>
      </div>
      <div style="flex-grow: 1">
        <span class="font-weight-bold">진료메모</span>
        <textarea id="editor" name="memo"></textarea>
      </div>
    </div>
  </div>
</template>


<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import {mapState} from "vuex";

export default {
  name: "DoctorPatient",

  mounted() {
    ClassicEditor.create(document.querySelector('#editor'), {
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
      contentCss: ''
    };
  },
  created() {
    this.contentCss = `
        :root {
          --ck-toolbar-height: 60px;
          --ck-content-height: 300px;
        }
        .ck-editor__editable {
          height: calc(var(--ck-content-height) - var(--ck-toolbar-height) - 2px) !important;
        }
        .ck-toolbar {
          height: var(--ck-toolbar-height) !important;
        }
      `;
  },
  computed: {
    ...mapState('doctor',
        ['waitingData']
    ),
    // 주민번호
    identityNumberMsg() {
      let str1 = this.waitingData.identityNumber.slice(0, 6);
      let str2 = this.waitingData.identityNumber.slice(6, 7);
      str1 = str1 + "-" + str2 + "******";
      return str1;
    },
    // 휴대폰번호
    phonePadMsg() {
      let newStr = this.waitingData.phoneNumber.replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, "");
      return newStr;
    },
    // 나이
    ageMsg() {
      let dateTemp = new Date(this.waitingData.birth)
      let dateNow = new Date();

      let tempYear = dateTemp.getFullYear();
      let nowYear = dateNow.getFullYear();
      let age = parseInt(nowYear) - parseInt(tempYear) + 1;
      return age;
    },
  },
  methods: {}

}


</script>

<style lang="scss" scoped>
.empty-box {
  height: 150px;
}
.empty-box img{
  margin: 0 auto;
}
.Patient-box {
  display: flex;
}

.patient-info {
  flex-wrap: nowrap;
}

.patient-info span {
  font-size: 10px;
  // 글자 줄바꿈 안되게 함.
  white-space: nowrap;
}

.patient-info span:after {
  display: inline-block;
  content: '';
  width: 1px;
  height: 12px;
  background: #b9b9b9;
  margin: 0 3px 0px 3px;
}

#editor {
  height: 500px;
}

</style>
<style>
.ck-editor__editable {
  /* 부모크기 따라감 inherit */
  width: inherit;
  height: 100px;
}
</style>