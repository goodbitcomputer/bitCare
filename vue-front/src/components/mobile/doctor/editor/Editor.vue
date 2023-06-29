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
            {{ showDiv ? '편집할 사진 선택' : '비교할 사진 선택' }}
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
            class="border-box img-select"
            draggable="true"
            style="margin-bottom: 15px;"
            @click="selectImage(image)"
        >
          <img :src="image.imagePath" class="image-list-box"/>
          <div class="img-cover"></div>
          <p class="image-date">{{ formatDate(image) }}</p>
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
      <div class="flex-grow-1 tuiEditor">
        <ImageEditor
            ref="tuiImageEditor"
            :disabled="showDiv"
            :image-list="images"
            :selectedImage="selectedEditorImage"
            class="tui-editor"
            @ready="onEditorReady"
            @update:image-list="updateImageList"
            @edit-complete="handleEditComplete"
        />
        <div v-if="showDiv" class="editor-cover" @click="removeCover">
          <p class="editor-text" style="margin-bottom: 0">
            편집할 이미지 변경 시 뷰어 해제
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ImageEditor from "@/components/mobile/doctor/editor/ImageEditor.vue";
import axios from "axios";
import {mapMutations, mapState} from "vuex";

export default {

  name: "DoctorEditor",
  components: {
    ImageEditor,
  },
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
    let patientId = this.waitingData.patientId;
    let historyId = this.historyData.id;
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

    axios
        .post('/doctor/editor/loadBodyCategoryImage', null, {
          params: {
            patientId: patientId,
            historyId: historyId
          }
        })
        .then(response => {
          console.log(response.data);
          this.images = [response.data, ...this.images];
        })
        .catch(error => {
          console.error(error);
        });
  },
  beforeUnmount() {
    document.removeEventListener("keydown", this.handleKeyDown);
  },
  computed: {
    ...mapState('editor',
        ['waitingData', 'historyData', 'isViewer', 'tempImage', 'imageEditor']
    )
  },
  methods: {
    ...mapMutations('editor', {
      setHistoryImageId: 'setHistoryImageId',
      setBodyCategoryId: 'setBodyCategoryId',
      setIsViewer: 'setIsViewer',
      setTempImage: 'setTempImage',
    }),
    formatDate(image) {
      console.log(image.entryDate);

      if (image.entryDate !== undefined) {
        const formattedDate = new Date(image.entryDate);
        const year = formattedDate.getFullYear();
        const month = String(formattedDate.getMonth() + 1).padStart(2, '0');
        const day = String(formattedDate.getDate()).padStart(2, '0');
        return `${year}년 ${month}월 ${day}일`;
      } else {
        return image.categoryName;
      }

    },
    handleEditComplete(image) {
      this.images.push({imagePath: image});
    },
    toggleImage(url) {
      if (this.isViewer) {
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
      if (this.showDiv === false) {
        this.setIsViewer(false);
      } else {
        this.setIsViewer(true);
      }
      // this.showViewerButton = this.showDiv;
    },
    toggleDeleteMode() {
      if (this.isDeletingImage) {
        const url = this.selectedEditorImage || this.selectedViewerImage;
        if (url) {
          if (confirm("정말로 이미지를 삭제하시겠습니까?")) {
            axios
                .post('/doctor/editor/deleteImage', null, {
                  params: {
                    imagePath: url
                  }
                })
          }
        }
      } else {
        this.isDeletingImage = true;
      }
    },
    // select img
    selectImage(image) {
      if (this.isDeletingImage) {
        window.Swal.fire({
          icon: 'question',
          title: '이미지 삭제',
          text: '정말로 이미지를 삭제하시겠습니까?',
          showCancelButton: true,
          confirmButtonText: '삭제',
          cancelButtonText: '취소',
        }).then((result) => {
          if (result.isConfirmed) {
            axios
                .post('/doctor/editor/deleteImage', null, {
                  params: {
                    imagePath: image.imagePath,
                  },
                })
                .then(() => {
                  const index = this.images.findIndex((item) => item.imagePath === image.imagePath);
                  if (index !== -1) {
                    this.images.splice(index, 1);
                  }
                  this.isDeletingImage = false;
                  window.Swal.fire({
                    icon: 'success',
                    title: '삭제 성공',
                    html: '이미지가 삭제되었습니다.',
                    timer: 3000,
                  });
                }).then(() => {
              window.location.reload(); // 실시간으로 image_list에 안올라감 페이지 새로고침
            })
                .catch((error) => {
                  console.error(error);
                  window.Swal.fire({
                    icon: 'error',
                    title: '삭제 실패',
                    html: '이미지 삭제 중 오류가 발생했습니다.',
                    timer: 3000,
                  });
                });
          }
        });
      } else {
        if (this.isViewer === false) {
          const tempImage = new Image()
          tempImage.crossOrigin = "Anonymous"
          tempImage.src = image.imagePath + '?t=' + new Date().getTime();
          // this.imageEditor.loadImageFromURL(tempImage.src + '?t=' + new Date().getTime(), "Sample Image");
          this.imageEditor.loadImageFromURL(tempImage.src, "Sample Image");
          this.imageEditor.ui.activeMenuEvent();
          this.imageData = tempImage.src; // 이미지가 선택되도록 데이터를 업데이트합니다.
        } else {
          if (this.selectedViewerImage === image.imagePath) {
            this.selectedViewerImage = null;
          } else {
            this.selectedViewerImage = image.imagePath;
          }
        }
      }
    },


    // 2023. 06. 09 유동준
    // 삭제 버튼 클릭 후 이미지 클릭하면 알람창 뜨고 이미지 삭제, db에서도 삭제 완료
    handleImageClick(url) {
      // 이미지 편집기를 호출하여 편집 가능한 이미지를 로드합니다.
      if (this.isDeletingImage) {
        // 이미지 삭제 모드에서는 삭제 확인 알림창을 표시
        window.Swal.fire({
          icon: 'question',
          title: '이미지 삭제',
          text: '정말로 이미지를 삭제하시겠습니까?',
          showCancelButton: true,
          confirmButtonText: '삭제',
          cancelButtonText: '취소',
        }).then((result) => {
          if (result.isConfirmed) {
            if (result.isConfirmed) {
              axios
                  .post('/doctor/editor/deleteImage', null, {
                    params: {
                      imagePath: url,
                    },
                  })
                  .then(() => {
                    const index = this.images.findIndex((image) => image.imagePath === url);
                    if (index !== -1) {
                      this.images.splice(index, 1);
                    }
                    this.isDeletingImage = false;
                    window.Swal.fire({
                      icon: 'success',
                      title: '삭제 성공',
                      html: '이미지가 삭제되었습니다.',
                      timer: 3000,
                    });
                  })
                  .catch((error) => {
                    console.error(error);
                    window.Swal.fire({
                      icon: 'error',
                      title: '삭제 실패',
                      html: '이미지 삭제 중 오류가 발생했습니다.',
                      timer: 3000,
                    });
                  });
            }
          }
        });
      } else {
        if (this.showDiv) {
          if (this.selectedViewerImage === url) {
            this.selectedViewerImage = null;
          } else {
            this.selectedViewerImage = url;
          }
        } else {
          // 이미지 편집 모드에서는 선택한 이미지를 편집기에 로드
          // let url = this.image.imagePath;
          // console.log(url);
          // console.log(this.image.imagePath);
          // console.log(this.images.imagePath);
          // console.log(this.image.images.imagePath);
          this.selectedEditorImage = url;
          this.$refs.tuiImageEditor.loadImageFromURL(url);
          this.$refs.tuiImageEditor.adjustCanvasSize(url);
          this.showDiv = false; // 캔버스 보이도록 showDiv를 false로 설정
        }
      }
    },
    dragImage(event, image) {
      event.dataTransfer.setData("text/plain", image.imagePath);
      this.setHistoryImageId(image.id);
      this.setBodyCategoryId(image.categoryId);
    },
    // dragFail() {
    //   window.Swal.fire({
    //     icon: 'error',
    //     title: '사진 편집 불가',
    //     html: '편집본은 수정이 불가합니다.',
    //     timer: 3000,
    //   });
    // },
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
    updateImageList(updatedImageList) {
      this.images = updatedImageList.map((image) => ({imagePath: image.imagePath}));
    },
    getDropdownImages(url) {
      const integerPart = Math.floor(url); // 이미지의 정수 부분 추출
      return this.images.filter(
          (image) => Math.floor(image.imagePath) === integerPart && image.imagePath !== url
      );
    },
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

.tuiEditor {
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

.image-date {
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
