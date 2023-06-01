<template>
  <div>
    <div class="editor-container" @drop="dropImage" @dragover.prevent>
      <div v-if="selectedImage" class="editor-cover">
        <!-- <p class="editor-text">이미지 선택</p> -->
      </div>
      <tui-image-editor
          ref="tuiImageEditor"
          :options="editorOptions"
          class="editor"
          @ready="onEditorReady"
      ></tui-image-editor>
      <button class="btn btn-primary save-button" @click="saveImage">이미지 저장</button>
      <img ref="img">
    </div>
  </div>
</template>

<script>
import "tui-image-editor/dist/tui-image-editor.css";
import "tui-color-picker/dist/tui-color-picker.css";
import ImageEditor from "tui-image-editor";

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
          initMenu: "filter",
          menu: ["crop", "flip", "rotate", "draw", "text"],
          uiSize: {
            width: "100%",
            height: "100%",
          },
          menuBarPosition: "bottom",
        },
        cssMaxWidth: 600,
        cssMaxHeight: 400,
        usageStatistics: false,
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
  methods: {
    onEditorReady() {
      if (this.selectedImage) {
        this.loadImageFromURL(this.selectedImage);
      }
    },
    loadImageFromURL(url) {
      this.editorInstance.loadImageFromURL(url, "Sample Image");
      this.editorInstance.ui.activeMenuEvent();
    },
    dropImage(event) {
      event.preventDefault();
      const url = event.dataTransfer.getData("text/plain");
      this.loadImageFromURL(url);
    },
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
    saveImage() {
      const canvas = this.editorInstance.toDataURL({format: "jpeg", quality: 0.8});
      const url = URL.createObjectURL(this.dataURLtoBlob(canvas));
      this.$emit("save", url);
    },
    dataURLtoBlob(dataURL) {
      const parts = dataURL.split(";base64,");
      const contentType = parts[0].split(":")[1];
      const raw = window.atob(parts[1]);
      const rawLength = raw.length;
      const uInt8Array = new Uint8Array(rawLength);
      for (let i = 0; i < rawLength; ++i) {
        uInt8Array[i] = raw.charCodeAt(i);
      }
      return new Blob([uInt8Array], {type: contentType});
    },
  },
};
</script>

<style scoped>
.editor-container {
  width: 100%;
  height: 100%;
  /* border: 1px solid #ebebeb; */
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
  top: 10px;
  right: 10px;
}
</style>
