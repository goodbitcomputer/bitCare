<template>
  <div>
    <div class="num-pad-box container">
      <div class="number-pad justify-content-center">
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="1">1
        </button>
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="2">2
        </button>
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="3">3
        </button>
      </div>
      <div class="number-pad justify-content-center">
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="4">4
        </button>
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="5">5
        </button>
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="6">6
        </button>
      </div>
      <div class="number-pad justify-content-center">
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="7">7
        </button>
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="8">8
        </button>
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="9">9
        </button>
      </div>
      <div class="number-pad justify-content-center">
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="-1">
          <div style="pointer-events: none;">지움</div>
        </button>
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="0">0
        </button>
        <button class="button button--moema button--inverted button--text-thick button--size-s"
                @click="numPadOn" value="-2">
          <div style="display: block;pointer-events: none;">전체</div>
          <div style="pointer-events: none;">지움</div>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "NumberPad",

  methods: {
    numPadOn() {
      this.$emit('numPadOn');
    }
  }
}
</script>

<style lang="scss" scoped>

/* 넘버패드 css ------------ */
/* Common button styles */
.button {
  float: left;
  min-width: 70px;
  max-width: 100px;
  display: block;
  margin: 0.1em;
  padding: 1em 1em;
  border: none;
  background: none;
  color: inherit;
  vertical-align: middle;
  position: relative;
  z-index: 1;
  -webkit-backface-visibility: hidden;
  -moz-osx-font-smoothing: grayscale;
}

.button:focus {
  outline: none;
}

.button > span {
  vertical-align: middle;
}

/* Text color adjustments (we could stick to the "inherit" but that does not work well in Safari) */
.bg-1 .button {
  color: #37474f;
  border-color: #37474f;
}

.bg-2 .button {
  color: #ECEFF1;
  border-color: #ECEFF1;
}

.bg-3 .button {
  color: #fff;
  border-color: #fff;
}

/* Sizes */
.button--size-s {
  font-size: 18px;
}

.button--size-m {
  font-size: 16px;
}

.button--size-l {
  font-size: 18px;
}

/* Moema */
.button--moema {
  padding: 1em 1em;
  border-radius: 10px;
  background: #7986cb;
  color: #fff;
  -webkit-transition: background-color 0.3s, color 0.3s;
  transition: background-color 0.3s, color 0.3s;
}

.button--moema.button--inverted {
  background: #ECEFF1;
  color: #37474f;
}

.button--moema::before {
  content: '';
  position: absolute;
  top: -20px;
  left: -20px;
  bottom: -20px;
  right: -20px;
  background: inherit;
  border-radius: 10px;
  z-index: -1;
  opacity: 0.4;
  -webkit-transform: scale3d(0.5, 0.5, 1);
  transform: scale3d(0.5, 0.5, 1);
}

.button--moema:active {
  -webkit-transition: background-color 0.1s 0.3s, color 0.1s 0.3s;
  transition: background-color 0.1s 0.3s, color 0.1s 0.3s;
  color: #ECEFF1;
  background-color: #3f51b5;
  -webkit-animation: anim-moema-1 0.1s forwards;
  animation: anim-moema-1 0.1s forwards;
}

.button--moema.button--inverted:hover {
  color: #ECEFF1;
  background-color: #7986cb;
}

.button--moema:hover::before {
  -webkit-animation: anim-moema-2 0.3s 0.3s forwards;
  animation: anim-moema-2 0.3s 0.3s forwards;
}

@-webkit-keyframes anim-moema-1 {
  60% {
    -webkit-transform: scale3d(0.8, 0.8, 1);
    transform: scale3d(0.8, 0.8, 1);
  }
  85% {
    -webkit-transform: scale3d(1.1, 1.1, 1);
    transform: scale3d(1.1, 1.1, 1);
  }
  100% {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

@keyframes anim-moema-1 {
  60% {
    -webkit-transform: scale3d(0.8, 0.8, 1);
    transform: scale3d(0.8, 0.8, 1);
  }
  85% {
    -webkit-transform: scale3d(1.1, 1.1, 1);
    transform: scale3d(1.1, 1.1, 1);
  }
  100% {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

@-webkit-keyframes anim-moema-2 {
  to {
    opacity: 0;
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

@keyframes anim-moema-2 {
  to {
    opacity: 0;
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

</style>