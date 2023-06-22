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
      <div class="info-container">
        차트 넘버 : CN.{{ this.waitingData.patientId }}, 환자 이름 : {{ this.waitingData.name }}, 성별 : {{ this.waitingData.gender }}, 연령 : {{ ageMsg }}세, 주민번호 : {{ identityNumberMsg }}
      </div>
      <button class="btn btn-primary save-button" @click="saveEditedImage(imageList)">편집 완료</button>
    </div>
  </div>
</template>

<script>
import "tui-image-editor/dist/tui-image-editor.css";
import "tui-color-picker/dist/tui-color-picker.css";
import ImageEditor from "tui-image-editor";
import axios from "axios";
import {mapMutations, mapState} from "vuex";

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
      imageData: null,
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
          menu: ["draw", "text"],
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
      'historyImageId', 'bodyCategoryId', 'waitingData'
    ]),
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
  },
  methods: {
    ...mapMutations('doctor', {
      setMemoEditor: 'setMemoEditor',
    }),
    // 2023.06.08 유동준
    // 편집한 이미지 저장하기 기능
    saveEditedImage(imageList) {
      const canvas = this.editorInstance.toDataURL();
      const blob = this.dataURLtoBlob(canvas);
      const file = new File([blob], "edited.png", {type: "image/png"});

      const formData = new FormData();

      formData.append("uploadFile", file);
      formData.append("historyId", new Blob([JSON.stringify(this.$route.query.historyId)], {type: "application/json"}));
      formData.append("bodyCategoryId", new Blob([JSON.stringify(this.bodyCategoryId)], {type: "application/json"}));
      formData.append("edited", new Blob([JSON.stringify(this.historyImageId)], {type: "application/json"}));

      let image = []
      let editedId = []
      for (let i = 0; i < imageList.length; i++) {
        image.push(imageList[i].edited)
        editedId.push(imageList[i].id)
      }

      let setId = editedId[image.indexOf(this.historyImageId + 0.1)]
      if (image.includes(this.historyImageId + 0.1)) {
        const canvas = this.editorInstance.toDataURL();
        const blob = this.dataURLtoBlob(canvas);
        const file = new File([blob], "editedUpdate.png", {type: "image/png"});

        const editedData = new FormData();

        editedData.append("id", setId);
        editedData.append("uploadFile", file);
        editedData.append("historyId", this.$route.query.historyId);
        editedData.append("bodyCategoryId", this.bodyCategoryId);
        editedData.append("edited", (this.historyImageId + 0.1).toString());

        axios
            .post("/doctor/editor/updateEditedImage", editedData, {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            })
            .then(() => {
              // 성공적으로 저장되었을 때의 처리
              // console.log("편집된 사진이 성공적으로 저장되었습니다.");
              window.Swal.fire({
                icon: 'success',
                title: '편집본 업데이트 완료',
                html: '새로운 편집본이 저장되었습니다.',
                timer: 3000
              }).then(() => {
                window.location.reload(); // 실시간으로 image_list에 안올라감 페이지 새로고침
              });
            })
            .catch(() => {
              // 저장 중 오류가 발생했을 때의 처리
              // console.error("편집된 사진 저장 중 오류가 발생했습니다.", error);
              window.Swal.fire({
                icon: 'error',
                title: '업데이트 실패',
                html: '이미지 저장을 실패했습니다.',
                timer: 3000
              })
            });
      } else {
        // 편집된 사진을 서버에 전송
        axios
            .post("/doctor/editor/saveEditedImage", formData, {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            })
            .then(() => {
              // 성공적으로 저장되었을 때의 처리
              // console.log("편집된 사진이 성공적으로 저장되었습니다.");
              window.Swal.fire({
                icon: 'success',
                title: '편집 완료',
                html: '이미지가 저장되었습니다.',
                timer: 3000
              }).then(() => {
                window.location.reload(); // 실시간으로 image_list에 안올라감 페이지 새로고침
              });
            })
            .catch(() => {
              // 저장 중 오류가 발생했을 때의 처리
              // console.error("편집된 사진 저장 중 오류가 발생했습니다.", error);
              window.Swal.fire({
                icon: 'error',
                title: '저장 실패',
                html: '이미지 저장을 실패했습니다.',
                timer: 3000
              })
            });
      }
    },
    onEditorReady() {
      if (this.selectedImage) {
        this.loadImageFromURL(this.selectedImage);
      }
    },

    // handleImageClick() {
    //   this.loadImageFromURL(this.selectedImage);
    // },

    loadImageFromURL(url) {
      this.editorInstance.loadImageFromURL(url, "Sample Image");
      this.editorInstance.ui.activeMenuEvent();
      this.imageData = url; // 이미지가 선택되도록 데이터를 업데이트합니다.
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

.info-container {
  color: white;
  position: absolute;
  top: 18px;
  right: 50%;
  transform: translateX(50%);
  text-align: center;
  border-color: white;
}
</style>
