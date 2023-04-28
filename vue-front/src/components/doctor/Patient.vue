<template>
  <div>
    <div class="border-box">
      <span style="font-size: 1.2em; font-weight: 700">진료 기록</span>
    </div>
    <div class="Patient-box border-box">

      <div style="border-right: 1px solid #DBDFE5;">
        <div style="display: flex;align-items: center;">
          <span class="font-weight-bold">박은희</span>
          <span style="flex-grow: 1">cn.6</span>
          <button>...</button>
        </div>
        <div class="patient-info">
          <span>50904-2******</span>
          <span>70세</span>
          <span>여</span>
          <span>010-1234-5678</span>
        </div>
        <div style="border-top: 1px solid #DBDFE5;">
          <p class="font-weight-bold">접수메모</p>
          <p></p>
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

}


</script>

<style lang="scss" scoped>
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