<template>
  <div>
    <div class="editor-container" @drop="dropImage" @dragover.prevent>
      <div v-if="selectedImage && !isImageOnCanvas(selectedImage)" class="editor-cover"/>
      <tui-image-editor
          ref="tuiImageEditor"
          :options="editorOptions"
          class="editor"
          @ready="onEditorReady"
      ></tui-image-editor>
      <button class="btn btn-primary save-button" @click="saveEditedImage"> 편집 완료</button>
      <img v-if="selectedImage" ref="img" :src="selectedImage"/>
    </div>
  </div>
</template>

<script>
import "tui-image-editor/dist/tui-image-editor.css";
import "tui-color-picker/dist/tui-color-picker.css";
import ImageEditor from "tui-image-editor";
import axios from "axios";
import {mapState} from "vuex";

export default {
  name: "ImageEditor",
  props: {
    selectedImage: {
      type: String,
      default: null,
    },
    imageList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      editorInstance: null,
      editorOptions: {
        includeUI: {
          loadImage: {
            path: "",
            name: "",
          },
          // initMenu : 시작할 때 켜질 기능
          // menu : 사용할 기능
          initMenu: "filter",
          menu: ["draw"],
          // menu: ["crop", "flip", "rotate", "draw", "text"],
          uiSize: {
            width: "100%",
            height: "100%",
          },
          menuBarPosition: "bottom",
        },
        // canvas 최대 사이즈 지정
        cssMaxWidth: 600,
        cssMaxHeight: 400,
        usageStatistics: false,
        crossOrigin: 'Anonymous',
      },
    };
  },
  mounted() {
    this.editorInstance = new ImageEditor(this.$refs.tuiImageEditor, this.editorOptions);
    window.addEventListener("resize", this.adjustCanvasSize);
  },
  beforeUnmount() {
    window.removeEventListener("resize", this.adjustCanvasSize);
  },
  watch: {
    selectedImage(newValue) {
      if (this.editorInstance && newValue) {
        this.loadImageFromURL(newValue);
      }
    },
  },
  computed: {
    ...mapState('editor', [
      'historyImageId', 'bodyCategoryId',
    ])
  },
  methods: {
    // 2023.06.08 유동준
    // 편집한 이미지 저장하기 기능
    saveEditedImage() {
      const canvas = this.editorInstance.toDataURL();
      const blob = this.dataURLtoBlob(canvas);
      const file = new File([blob], "edited.png", {type: "image/png"});

      const formData = new FormData();

      formData.append("uploadFile", file);
      formData.append("historyId", new Blob([JSON.stringify(this.$route.query.historyId)], {type: "application/json"}));
      formData.append("bodyCategoryId", new Blob([JSON.stringify(this.bodyCategoryId)], {type: "application/json"}));
      formData.append("edited", new Blob([JSON.stringify(this.historyImageId)], {type: "application/json"}));

      // 편집된 사진을 서버에 전송
      axios
          .post("/doctor/editor/saveEditedImage", formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then(() => {
            // 성공적으로 저장되었을 때의 처리
            console.log("편집된 사진이 성공적으로 저장되었습니다.");
          })
          .catch((error) => {
            // 저장 중 오류가 발생했을 때의 처리
            console.error("편집된 사진 저장 중 오류가 발생했습니다.", error);
          });
    },
    onEditorReady() {
      if (this.selectedImage) {
        this.loadImageFromURL(this.selectedImage);
      }
    },

    loadImageFromURL(url) {
      console.log("asdfasdfasdfasdfdasf")
      this.editorInstance.loadImageFromURL(url, "Sample Image");
      this.editorInstance.ui.activeMenuEvent();
    },

    // image-list에서 사진 드래그앤드랍했을 때
    dropImage(event) {
      event.preventDefault();
      const url = event.dataTransfer.getData("text/plain");

      // S3 서버 CORS 관리
      const tempImage = new Image()
      tempImage.crossOrigin = "Anonymous"
      tempImage.src = url;


      this.loadImageFromURL(tempImage.src + '?t=' + new Date().getTime());
    },

    // canvas 사이즈 조절
    adjustCanvasSize() {
      if (!this.editorInstance) {
        return;
      }
      const canvasElement = this.editorInstance.toDataURL(true).canvas;
      if (!canvasElement) {
        return;
      }
      const pixelRatio = window.devicePixelRatio || 1;
      const canvasWidth = canvasElement.width / pixelRatio;
      const canvasHeight = canvasElement.height / pixelRatio;
      canvasElement.style.width = `${canvasWidth}px`;
      canvasElement.style.height = `${canvasHeight}px`;
      canvasElement.width = canvasWidth * pixelRatio;
      canvasElement.height = canvasHeight * pixelRatio;
      this.loadImageFromURL(this.selectedImage);
    },


    // data 타입 변환
    dataURLtoBlob(dataURL) {
      const base64Split = dataURL.split(",");
      const contentType = base64Split[0].match(/:(.*?);/)[1];
      const byteCharacters = atob(base64Split[1]);
      const byteArrays = [];

      for (let offset = 0; offset < byteCharacters.length; offset += 1024) {
        const slice = byteCharacters.slice(offset, offset + 1024);
        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
          byteNumbers[i] = slice.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
      }

      return new Blob(byteArrays, {type: contentType});
    },
    isImageOnCanvas(url) {
      const objects = this.editorInstance._graphics._canvasImage.objects;
      return objects.some((obj) => obj._element.src === url);
    },
  },
};
</script>


<style scoped>
.editor-container {
  width: 100%;
  height: 100%;
}

@media screen and (max-width: 600px) {
  .editor-container {
    /* 작은 화면에 대한 이미지 크기 조정 */
    width: 80%;
    height: auto;
  }
}

@media screen and (min-width: 601px) and (max-width: 1024px) {
  .editor-container {
    /* 중간 크기 화면에 대한 이미지 크기 조정 */
    width: 60%;
    height: auto;
  }
}

@media screen and (min-width: 1025px) {
  .editor-container {
    /* 큰 화면에 대한 이미지 크기 조정 */
    width: 40%;
    height: auto;
  }
}

.save-button {
  position: absolute;
  top: 20px; /* 수정: 조정된 버튼 간격 */
  right: 10px;
}

</style>
