<template>
  <div class="container">
    <span>nurse</span>
    <button @click="click()">버튼</button>
    <div v-if="isClick">
      <textarea id="editor" name="memo"></textarea>
    </div>
  </div>
</template>

<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';

export default {
  name: "NurseView",
  components: {},
  data() {
    return {
      isClick: false,
      contentCss: ''
    };
  },
  mounted() {
    // ClassicEditor.create(document.querySelector('#editor'), {
    //   contentCss: this.contentCss,
    //   toolbar: [
    //     // 'heading',
    //     // '|',
    //     'bold',
    //     'italic',
    //     'link',
    //     'bulletedList',
    //     '|',
    //     'undo',
    //     'redo',
    //     // '|',
    //     // 'imageUpload',
    //     // 'alignment',
    //     // 'numberedList',
    //     // 'imageInsert',
    //     // 'blockQuote',
    //     // '|',
    //     // 'ckfinder',
    //   ],
    // }).catch((error) => {
    //   console.error(error);
    // });
  },
  created() {
    // this.contentCss = `
    //     :root {
    //       --ck-toolbar-height: 60px;
    //       --ck-content-height: 300px;
    //     }
    //     .ck-editor__editable {
    //       height: calc(var(--ck-content-height) - var(--ck-toolbar-height) - 2px) !important;
    //     }
    //     .ck-toolbar {
    //       height: var(--ck-toolbar-height) !important;
    //     }
    //   `;
  },
  updated() {
    this.$nextTick(() => {
      if (this.isClick) {
        this.ckeditorSetting();
      }
    })
  },
  methods: {
    click() {
      if (this.isClick === false) {
        this.isClick = true;
      } else {
        this.isClick = false;
      }
    },
    ckeditorSetting() {
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
    }
  }
}
</script>

<style scoped>
</style>