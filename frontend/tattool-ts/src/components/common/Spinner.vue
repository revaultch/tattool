<template>
  <div class="spinner-container">
    <div class="ripple">
      <div></div>
      <div></div>
    </div>
    <div class="message">
      {{ loadingMessage }}
    </div>
  </div>
</template>

<script>
import useUIState from "@/state/useUIState";

export default {
  setup() {
    return { loadingMessage: useUIState().loadingMessage };
  },
};
</script>

<style lang="scss" scoped>
.spinner-container {
  position: fixed;
  .message {
    position: fixed;
    color: white;
    width: 100%;
    text-align: center;
    border-radius: 4px;
    font-weight: 100;
    font-size: 3rem;
    bottom: 4rem;
  }
}
</style>