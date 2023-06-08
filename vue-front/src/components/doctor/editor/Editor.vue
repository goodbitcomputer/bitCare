<template>
  <div class="editor-box d-flex">
    <div class="border-box image-list-container">
      <div class="compare-container">
        <div class="compare">
          <button
              id="button"
              ref="compareButton"
              class="button compare-button"
              @click="toggleNewDiv"
          >
            {{ showDiv ? '편집할 사진 선택 (F2)' : '비교할 사진 선택 (F2)' }}
          </button>
          <button class="btn btn-danger delete-button" @click="toggleDeleteMode">
            삭제할 이미지 선택
          </button>
        </div>
      </div>
      <div class="image-list">
        <div
            v-for="(image, index) in images"
            :key="index"
            :class="{ active: isSelected(image.imagePath) }"
            class="border-box img-select"
            draggable="true"
            style="margin-bottom: 15px;"
            @click="handleImageClick(image.imagePath)"
            @dragstart="dragImage($event, image)"
        >
          <img :src="image.imagePath" class="image-list-box" draggable="false"/>
          <div v-if="isSelected(image.imagePath)" class="img-cover"></div>
          <p class="date">{{ getCurrentDate() }}</p>
          <div v-if="isSelected(image.imagePath)" class="dropdown">
            <ul class="draggable-list">
              <li
                  v-for="(image, index) in dropdownImages"
                  :key="index"
                  class="border-box img-select"
                  draggable="true"
                  style="margin-bottom: 15px;"
                  @dragstart="dragImage($event, image.imagePath)"
              >
                <img :src="image.imagePath" class="image-list-box"/>
                <p class="date">{{ getCurrentDate() }}</p>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <br/>
    </div>
    <div class="flex-grow-1 d-flex editor-container border-box">
      <div v-if="showDiv" class="viewer d-flex align-items-center">
        <img class="BI" src="@/assets/BI/BIW2.png"/>
        <img
            v-if="selectedViewerImage"
            :src="selectedViewerImage"
            class="viewer-img"
            draggable="true"
            @dragstart="dragImage($event, selectedViewerImage)"
        />
        <div v-if="showDiv" class="viewer-bottom">
          <p style="margin-bottom: 0">뷰어에서는 수정이 불가합니다.</p>
        </div>
      </div>
      <div class="flex-grow-1 editor">
        <ImageEditor
            ref="tuiImageEditor"
            :image-list="images"
            :selectedImage="selectedEditorImage"
            class="tui-editor"
            @ready="onEditorReady"
            @update:image-list="updateImageList"
            @edit-complete="handleEditComplete"
        />
        <div v-if="showDiv" class="editor-cover" @click="removeCover">
          <p class="editor-text" style="margin-bottom: 0">
            편집할 이미지를 드래그 해주세요! <br/>
            뷰어 해제 (Click or F2)
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ImageEditor from "@/components/doctor/editor/ImageEditor.vue";
import axios from "axios";
import {mapMutations} from "vuex";
// import axios from "axios";

export default {

  name: "DoctorEditor",
  components: {
    ImageEditor,
  },

  // props: {
  //   patientId: {
  //     type: Number,
  //     default: 0,
  //   },
  // },

  data() {
    return {
      showDiv: false,
      images: [],
      selectedEditorImage: null,
      selectedViewerImage: null,
      showViewerButton: false,
      isDeletingImage: false,
    };
  },

  mounted() {
    document.addEventListener('keydown', this.handleKeyDown);
    const patientId = this.$route.query.patientId;
    const historyId = this.$route.query.historyId;
    axios
        .post('/doctor/editor/selectByPatientIdAndHistoryId', null, {
          params: {
            patientId: patientId,
            historyId: historyId
          }
        })
        .then(response => {
          console.log(response.data);
          this.images = response.data;
        })
        .catch(error => {
          console.error(error);
        });
  },

  beforeUnmount() {
    document.removeEventListener("keydown", this.handleKeyDown);
  },

  methods: {
    ...mapMutations('editor', {
      setHistoryImageId: 'setHistoryImageId',
      setBodyCategoryId: 'setBodyCategoryId',
    }),
    handleEditComplete(image) {
      this.images.push({imagePath: image});
    },
    getCurrentDate() {
      const today = new Date();
      const options = {year: "numeric", month: "long", day: "numeric"};
      return today.toLocaleDateString("ko-KR", options);
    },
    toggleImage(url) {
      if (this.showDiv) {
        if (this.selectedViewerImage === url) {
          this.selectedViewerImage = null;
        } else {
          this.selectedViewerImage = url;
        }
      } else {
        this.selectedEditorImage = this.selectedEditorImage === url ? null : url;
      }
    },
    toggleNewDiv() {
      this.showDiv = !this.showDiv;
      this.showViewerButton = this.showDiv;
    },
    toggleDeleteMode() {
      this.isDeletingImage = !this.isDeletingImage;
    },
    handleImageClick(url) {
      if (this.isDeletingImage) {
        const index = this.images.findIndex((image) => image.imagePath === url);
        if (index !== -1) {
          this.images.splice(index, 1);
        }
        this.isDeletingImage = false;
      } else {
        if (this.showDiv) {
          if (this.selectedViewerImage === url) {
            this.selectedViewerImage = null;
          } else {
            this.selectedViewerImage = url;
          }
        } else {
          this.selectedEditorImage = url;
          this.$refs.tuiImageEditor.loadImageFromURL(url, "Sample Image");
          this.$refs.tuiImageEditor.editorInstance.ui.activeMenuEvent();
        }
      }
    },
    dragImage(event, image) {
      event.dataTransfer.setData("text/plain", image.imagePath);
      this.setHistoryImageId(image.id);
      this.setBodyCategoryId(image.categoryId);
    },
    isSelected(url) {
      if (this.showDiv) {
        return this.selectedViewerImage === url;
      } else {
        return this.selectedEditorImage === url && this.selectedViewerImage !== url;
      }
    },
    onEditorReady() {
      if (this.selectedEditorImage) {
        this.$refs.tuiImageEditor.loadImageFromURL(
            this.selectedEditorImage,
            "Sample Image"
        );
      }
    },
    removeCover() {
      this.showDiv = false;
    },
    handleSelectEditorImage(url) {
      this.selectedEditorImage = url;
    },
    handleKeyDown(event) {
      if (event.keyCode === 113) {
        event.preventDefault();
        this.toggleNewDiv();
      }
    },
    updateImageList(updatedImageList) {
      this.images = updatedImageList.map((image) => ({imagePath: image.imagePath}));
    },
    // getDropdownImages(url) {
    //   return this.images.filter((image) => image.imagePath !== url).slice(0, 3);
    // },
  },
};
</script>

<style scoped>
.editor-box {
  height: 90vh;
}

.compare {
  text-align: center;
  position: sticky;
}

.editor {
  position: relative;
  width: 50%;
  background-color: #282828;
}

.viewer {
  margin-right: 5px;
  position: relative;
  width: 50%;
  display: table;
  height: 100%;
  background-color: #1e1e1e;
  justify-content: center;
}

.BI {
  position: absolute;
  top: 0;
  left: 0;
  padding: 15px 5px;
  width: 180px;
  margin-top: 5px;
  margin-left: 15px;
}

.image-list-container {
  position: relative;
  width: 15%;
}

.image-list {
  overflow-y: scroll;
  max-height: 70vh;
//margin-top: 10px; position: absolute;
}

/* 스크롤바 숨기기 */
.image-list::-webkit-scrollbar {
  width: 0;
}

.editor-container {
  width: 85vw;
  background-color: #282828;
  border-radius: 10px;
}

.editor-cover {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  width: 100%;
  height: 70px;
  top: 0;
  z-index: 5;
  background-color: #1e1e1e;
  cursor: pointer;
}

.editor-text {
  text-align: center;
  color: #ffb20d;
}

.viewer-bottom {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  width: 100%;
  height: 64px;
  bottom: 0;
  z-index: 5;
  background-color: #151515;
}

.viewer p {
  color: #ffb20d;
  text-align: center;
}

.date {
  font-size: 15px;
  font-weight: bold;
  text-align: center;
  margin-top: 5px;
  margin-bottom: 0;
}

.viewer-img {
  position: absolute;
  max-width: 80%;
  max-height: 80%;
  z-index: 10;
}

.img-select {
  position: relative;
  cursor: pointer;
}

.img-select::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: black;
  opacity: 0;
  transition: opacity 0.3s ease;
  border-radius: 5px;
}

.img-select:hover::before,
.img-select.active::before {
  opacity: 0.5;
}

.draggable-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.compare-button {
  border-radius: 10px;
  margin-top: 5px;
  width: 90%;
  font-size: 13px;
  letter-spacing: 1px;
  margin-bottom: 5px;
}

.delete-button {
  border-radius: 10px;
  margin-top: 5px;
  width: 90%;
  font-size: 13px;
  letter-spacing: 1px;
  margin-bottom: 5px;
}
</style>
