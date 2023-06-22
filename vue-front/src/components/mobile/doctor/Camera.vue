<template>
  <div id="container">
    <div id="vid_container">
      <video id="video" ref="video" autoplay playsinline></video>
      <div id="video_overlay"></div>
    </div>
    <div id="gui_controls">
      <button
          id="toggleFullScreenButton"
          name="toggle FullScreen"
          type="button"
          aria-pressed="false"
          @click="galleryBtn"
      ><img style="padding: 10px" src="/assets/mobile/img/icon-gallery.png"></button>
      <button id="takePhotoButton" name="take Photo" type="button" @click="takePicture();"><img
          src="/assets/mobile/img/ic_photo_camera_white_48px.svg"></button>
      <button
          id="switchCameraButton"
          name="switch Camera"
          type="button"
          aria-pressed="false"
      ><img style="padding: 10px" src="/assets/mobile/img/icon-change.png"></button>
    </div>
    <canvas ref="canvas" style="display: none;"></canvas>
  </div>

</template>

<script>
import {mapMutations} from "vuex";

export default {
  name: "MobileCamera",
  data() {
    return {
      width: '100%',
      height: '100%',
    }
  },
  mounted() {
    var takeSnapshotUI = createClickFeedbackUI();

    var video;
    var takePhotoButton;
    // var toggleFullScreenButton;
    var switchCameraButton;
    var amountOfCameras = 0;
    var currentFacingMode = 'environment';

// this function counts the amount of video inputs
// it replaces DetectRTC that was previously implemented.
    function deviceCount() {
      return new Promise(function (resolve) {
        var videoInCount = 0;

        navigator.mediaDevices
            .enumerateDevices()
            .then(function (devices) {
              devices.forEach(function (device) {
                if (device.kind === 'video') {
                  device.kind = 'videoinput';
                }

                if (device.kind === 'videoinput') {
                  videoInCount++;
                  console.log('videocam: ' + device.label);
                }
              });

              resolve(videoInCount);
            })
            .catch(function (err) {
              console.log(err.name + ': ' + err.message);
              resolve(0);
            });
      });
    }

    initCamera();

    function initCamera() {
      // check if mediaDevices is supported
      if (
          navigator.mediaDevices &&
          navigator.mediaDevices.getUserMedia &&
          navigator.mediaDevices.enumerateDevices
      ) {
        // first we call getUserMedia to trigger permissions
        // we need this before deviceCount, otherwise Safari doesn't return all the cameras
        // we need to have the number in order to display the switch front/back button
        navigator.mediaDevices
            .getUserMedia({
              audio: false,
              video: true,
            })
            .then(function (stream) {
              stream.getTracks().forEach(function (track) {
                track.stop();
              });

              deviceCount().then(function (deviceCount) {
                amountOfCameras = deviceCount;

                // init the UI and the camera stream
                initCameraUI();
                initCameraStream();
              });
            })
            .catch(function (error) {
              //https://developer.mozilla.org/en-US/docs/Web/API/MediaDevices/getUserMedia
              if (error === 'PermissionDeniedError') {
                alert('Permission denied. Please refresh and give permission.');
              }

              console.error('getUserMedia() error: ', error);
            });
      } else {
        alert(
            'Mobile camera is not supported by browser, or there is no camera detected/connected',
        );
      }
    }

    initCameraUI();

    function initCameraUI() {
      video = document.getElementById('video');

      takePhotoButton = document.getElementById('takePhotoButton');
      // toggleFullScreenButton = document.getElementById('toggleFullScreenButton');
      switchCameraButton = document.getElementById('switchCameraButton');

      // https://developer.mozilla.org/nl/docs/Web/HTML/Element/button
      // https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA/ARIA_Techniques/Using_the_button_role

      takePhotoButton.addEventListener('click', function () {
        takeSnapshotUI();
        takeSnapshot();
      });

      // -- fullscreen part

      // function fullScreenChange() {
      //   if (screenfull.isFullscreen) {
      //     toggleFullScreenButton.setAttribute('aria-pressed', true);
      //   } else {
      //     toggleFullScreenButton.setAttribute('aria-pressed', false);
      //   }
      // }

      // if (screenfull.isEnabled) {
      //   screenfull.on('change', fullScreenChange);
      //
      //   toggleFullScreenButton.style.display = 'block';
      //
      //   // set init values
      //   fullScreenChange();
      //
      //   toggleFullScreenButton.addEventListener('click', function () {
      //     screenfull.toggle(document.getElementById('container')).then(function () {
      //       console.log(
      //           'Fullscreen mode: ' +
      //           (screenfull.isFullscreen ? 'enabled' : 'disabled'),
      //       );
      //     });
      //   });
      // } else {
      //   console.log("iOS doesn't support fullscreen (yet)");
      // }

      // -- switch camera part
      if (amountOfCameras > 1) {
        switchCameraButton.style.display = 'block';

        switchCameraButton.addEventListener('click', function () {
          if (currentFacingMode === 'environment') currentFacingMode = 'user';
          else currentFacingMode = 'environment';

          initCameraStream();
        });
      }

      // Listen for orientation changes to make sure buttons stay at the side of the
      // physical (and virtual) buttons (opposite of camera) most of the layout change is done by CSS media queries
      // https://www.sitepoint.com/introducing-screen-orientation-api/
      // https://developer.mozilla.org/en-US/docs/Web/API/Screen/orientation
      window.addEventListener(
          'orientationchange',
          function () {
            // iOS doesn't have screen.orientation, so fallback to window.orientation.
            // screen.orientation will
            let angle;
            if (screen.orientation) angle = screen.orientation.angle;
            else angle = window.orientation;

            var guiControls = document.getElementById('gui_controls').classList;
            var vidContainer = document.getElementById('vid_container').classList;

            if (angle == 270 || angle == -90) {
              guiControls.add('left');
              vidContainer.add('left');
            } else {
              if (guiControls.contains('left')) guiControls.remove('left');
              if (vidContainer.contains('left')) vidContainer.remove('left');
            }

            //0   portrait-primary
            //180 portrait-secondary device is down under
            //90  landscape-primary  buttons at the right
            //270 landscape-secondary buttons at the left
          },
          false,
      );
    }

// https://github.com/webrtc/samples/blob/gh-pages/src/content/devices/input-output/js/main.js
    function initCameraStream() {
      // stop any active streams in the window
      if (window.stream) {
        window.stream.getTracks().forEach(function (track) {
          console.log(track);
          track.stop();
        });
      }

      // we ask for a square resolution, it will cropped on top (landscape)
      // or cropped at the sides (landscape)
      var size = 1280;

      var constraints = {
        audio: false,
        video: {
          width: {ideal: size},
          height: {ideal: size},
          //width: { min: 1024, ideal: window.innerWidth, max: 1920 },
          //height: { min: 776, ideal: window.innerHeight, max: 1080 },
          facingMode: currentFacingMode,
        },
      };

      navigator.mediaDevices
          .getUserMedia(constraints)
          .then(handleSuccess)
          .catch(handleError);

      function handleSuccess(stream) {
        window.stream = stream; // make stream available to browser console
        video.srcObject = stream;

        if (constraints.video.facingMode) {
          if (constraints.video.facingMode === 'environment') {
            switchCameraButton.setAttribute('aria-pressed', true);
          } else {
            switchCameraButton.setAttribute('aria-pressed', false);
          }
        }

        const track = window.stream.getVideoTracks()[0];
        const settings = track.getSettings();
        let str = JSON.stringify(settings, null, 4);
        console.log('settings ' + str);
      }

      function handleError(error) {
        console.error('getUserMedia() error: ', error);
      }
    }

    function takeSnapshot() {
      // if you'd like to show the canvas add it to the DOM
      var canvas = document.createElement('canvas');

      var width = video.videoWidth;
      var height = video.videoHeight;

      canvas.width = width;
      canvas.height = height;

      let context = canvas.getContext('2d');
      context.drawImage(video, 0, 0, width, height);

      // polyfil if needed https://github.com/blueimp/JavaScript-Canvas-to-Blob

      // https://developers.google.com/web/fundamentals/primers/promises
      // https://stackoverflow.com/questions/42458849/access-blob-value-outside-of-canvas-toblob-async-function
      function getCanvasBlob(canvas) {
        return new Promise(function (resolve) {
          canvas.toBlob(function (blob) {
            resolve(blob);
          }, 'image/jpeg');
        });
      }

      // some API's (like Azure Custom Vision) need a blob with image data
      getCanvasBlob(canvas).then(function () {
        // do something with the image blob
      });

    }

// https://hackernoon.com/how-to-use-javascript-closures-with-confidence-85cd1f841a6b
// closure; store this in a variable and call the variable as function
// eg. var takeSnapshotUI = createClickFeedbackUI();
// takeSnapshotUI();


    function createClickFeedbackUI() {
      // in order to give feedback that we actually pressed a button.
      // we trigger a almost black overlay
      var overlay = document.getElementById('video_overlay'); //.style.display;

      // sound feedback
      // var sndClick = new Howl({ src: ['snd/click.mp3'] });

      var overlayVisibility = false;
      var timeOut = 80;

      function setFalseAgain() {
        overlayVisibility = false;
        overlay.style.display = 'none';
      }

      return function () {
        if (overlayVisibility == false) {
          // sndClick.play();
          overlayVisibility = true;
          overlay.style.display = 'block';
          setTimeout(setFalseAgain, timeOut);
        }
      };
    }


    // let constraints = {
    //   audio: false,
    //   video: {
    //     facingMode: 'user',
    //   }
    // }
    // navigator.mediaDevices.getUserMedia(constraints)
    //     .then((stream) => {
    //       this.$refs.video.srcObject = stream;
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //     });
  },

  methods: {
    ...mapMutations('mobileDoctor', {
      setNewCameraList: 'setNewCameraList',
      setNextStep: 'setNextStep',
    }),
    initStream() {
      if (window.stream) {
        window.stream.getTracks().forEach(function (track) {
          // console.log(track);
          track.stop();
        });
      }
    },
    galleryBtn() {
      // let video = document.getElementById('video');
      // video.pause();
      // let vidTrack = this.localStream.getVideoTracks();
      // this.localStream.removeTrack(vidTrack);
      this.initStream();

      this.setNextStep(9);
    },
    takePicture() {
      const video = this.$refs.video;
      const canvas = this.$refs.canvas;
      const context = canvas.getContext('2d');

      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      context.drawImage(video, 0, 0, canvas.width, canvas.height);

      const dataURL = canvas.toDataURL('image/png');
      // dataURL을 사용하여 이미지를 업로드하거나 저장할 수 있습니다.

      // console.log(dataURL);
      let base64Content = dataURL.split(",")[1];
      // base64 문자열 디코딩
      let binary = atob(base64Content);

// ArrayBuffer 생성
      let arrayBuffer = new ArrayBuffer(binary.length);
      let uint8Array = new Uint8Array(arrayBuffer);

// uint8Array에 binary 데이터 복사
      for (let i = 0; i < binary.length; i++) {
        uint8Array[i] = binary.charCodeAt(i);
      }

// Blob 객체 생성
      let blob = new Blob([arrayBuffer], {type: "image/png"});

// Blob URL 생성
      let url = URL.createObjectURL(blob);

// File 객체 생성
      let file = new File([blob], "image.png", {type: "image/png"});

      // newCameraList on vuex 저장
      this.setNewCameraList({blob: blob, url: url, file: file, storeSelect: false, photoSelect: false});

      // document.getElementById('cameraImg').src = url;


    }
  }
}
</script>

<style lang="scss" scoped>


body {
  margin: 0px;
  padding: 0px;
  background: #000;
}

#vid_container {
  position: fixed;
  //position: relative;
  top: 0;
  left: 0;
}

#video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
}

#gui_controls {
  position: fixed;
  background-color: #111; /*rgba(255, 0, 0, 0.5);*/
  z-index: 2;
  bottom: 0;
}

#video_overlay {
  position: fixed;
  display: none;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;

  z-index: 10;
  background-color: #111;
}

/*  Button style from:
    https://googlesamples.github.io/web-fundamentals/fundamentals/media/mobile-web-video-playback.html */

button {
  outline: none;
  position: absolute;
  color: white;
  display: block;
  opacity: 1;
  background: transparent;
  border: solid 2px #fff;
  padding: 0;
  text-shadow: 0px 0px 4px black;
  background-position: center center;
  background-repeat: no-repeat;
  pointer-events: auto;
  z-index: 2;
}

#takePhotoButton {
  left: calc(50% - 40px);
  top: calc(50% - 40px);
  width: 80px;
  height: 80px;
  //background-image: url('/public/assets/mobile/img/ic_photo_camera_white_48px.svg');
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.5);
}

#takePhotoButton:active {
  background-color: #fff;
}

#toggleFullScreenButton {
  //display: none;
  width: 64px;
  height: 64px;
  //background-image: url('/public/assets/mobile/img/ic_fullscreen_white_48px.svg');
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.5);
}

//#toggleFullScreenButton[aria-pressed='true'] {
//  background-image: url('/public/assets/mobile/img/ic_fullscreen_exit_white_48px.svg');
//}

#switchCameraButton {
  display: none;
  width: 64px;
  height: 64px;
  //background-image: url('/assets/mobile/img/ic_camera_rear_white_36px.svg');
  //background-image: url('/public/assets/mobile/img/home_house_icon.png');
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.5);
}

#switchCameraButton[aria-pressed='true'] {
  //background-image: url('/public/assets/mobile/img/ic_camera_front_white_36px.svg');
}

@media screen and (orientation: portrait) {
  /* portrait-specific styles */

  /* video_container (video) doesn't respect height...
       so we will fill it in completely in portrait mode
    */
  #vid_container {
    width: 100%;
    height: 80%;
  }

  #gui_controls {
    width: 100%;
    height: 20%;
    left: 0;
  }
  #toggleFullScreenButton {
    left: calc(20% - 32px);
    top: calc(50% - 32px);
  }

  #switchCameraButton {
    left: calc(80% - 32px);
    top: calc(50% - 32px);
  }
}

@media screen and (orientation: landscape) {
  #vid_container {
    width: 80%;
    height: 100%;
  }

  #vid_container.left {
    left: 20%;
  }

  /* we default to right */
  #gui_controls {
    width: 20%;
    height: 100%;
    right: 0;
  }

  /* for the lefties */
  #gui_controls.left {
    left: 0;
  }

  #toggleFullScreenButton {
    left: calc(50% - 32px);
    top: calc(82% - 32px);
  }

  #switchCameraButton {
    left: calc(50% - 32px);
    top: calc(18% - 32px);
  }

}
</style>