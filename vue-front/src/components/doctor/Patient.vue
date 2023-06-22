<template>
  <div>
    <div class="title-border-box">
      <span style="font-size: 1.2em; font-weight: 700">진료 기록 작성</span>
    </div>
    <div class="empty-box border-box" v-if="isEmpty">
      <div class="empty-img-box">
        <img src="@/assets/img/empty-box.png">
      </div>
    </div>
    <div class="Patient-box border-box" v-if="!isEmpty">

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
        <textarea id="memoEditor" name="memo"></textarea>
      </div>
    </div>
  </div>
</template>


<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import {mapMutations, mapState} from "vuex";

export default {
  name: "DoctorPatient",

  mounted() {
    // console.log(this.waitingData);

  },

  data() {
    return {
      contentCss: '',
      ckeditor: 0,
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
          line-height: 0.3em;
        }
        .ck-toolbar {
          height: var(--ck-toolbar-height) !important;
        }
      `;

    this.$nextTick(()=>{
      if(!this.isEmpty) {
        if(this.ckeditor==0) {
          this.ckeditorSetting();
          this.ckeditor= this.ckeditor+1;
        }
      } else{
        this.ckeditor= 0;
      }
    })
  },
  // 환자데이터가 활성화되면 ckeditor 세팅하기
  updated() {
    this.$nextTick(()=>{
      if(!this.isEmpty) {
        if(this.ckeditor==0) {
          this.ckeditorSetting();
          this.ckeditor= this.ckeditor+1;
        }
      } else{
        this.ckeditor= 0;
      }
    })
  },
  computed: {
    ...mapState('doctor',
        ['waitingData']
    ),
    // 주민번호
    identityNumberMsg() {
      if (this.waitingData === "") {
        return ""
      } else {
        let str1 = this.waitingData.identityNumber.slice(0, 6);
        let str2 = this.waitingData.identityNumber.slice(6, 7);
        str1 = str1 + "-" + str2 + "******";
        return str1;
      }
    },
    // 휴대폰번호
    phonePadMsg() {
      if (this.waitingData === "") {
        return ""
      } else {
        let newStr = this.waitingData.phoneNumber.replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, "");
        return newStr;
      }
    },
    // 나이
    ageMsg() {
      if (this.waitingData === "") {
        return ""
      } else {
        let dateTemp = new Date(this.waitingData.birth)
        let dateNow = new Date();

        let tempYear = dateTemp.getFullYear();
        let nowYear = dateNow.getFullYear();
        let age = parseInt(nowYear) - parseInt(tempYear) + 1;
        return age;
      }
    },
    // 환자 데이터가 있는지 확인
    isEmpty() {
      return this.waitingData === "" ? true : false;
      // return true;
    },
  },
  methods: {
    ...mapMutations('doctor', {
      setMemoEditor:'setMemoEditor',
    }),
    ckeditorSetting() {
      ClassicEditor.create(document.querySelector('#memoEditor'), {
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
      }).then( newEditor => {
        this.setMemoEditor(newEditor);
      }).catch((error) => {
        console.error(error);
      });
    }
  }

}


</script>

<style lang="scss" scoped>
.title-border-box {
  margin: 5px;
  padding: 0 5px;
  border-width: 2px;
  border-style: solid;
  border-color: #003A63;
  border-image: initial;
  border-radius: 10px;
}
.empty-box {
  height: 700px;
  display: flex;
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

#memoEditor {
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