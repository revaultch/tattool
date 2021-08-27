<template>
  <div class="copy-to-clipboard container">
    <img
      alt="copy id to clipboard"
      title="copy id to clipboard"
      @click="copyToClipboard()"
      src="@/assets/common/png/copy.png"
    />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { copyText } from "vue3-clipboard";
import useUIState from "@/state/useUIState";

export default defineComponent({
  props: ["text"],
  components: {},
  setup(props) {
    const { notifyFailure, notifySuccess } = useUIState();
    const copyToClipboard = () => {
      copyText(props.text, undefined, (error: any, event: any) => {
        if (error) {
          notifyFailure("Error copying value to clipboard");
        } else {
          notifySuccess(`Value ${props.text} copied to clipboard`);
        }
      });
    };
    return { copyToClipboard };
  },
});
</script>

<style lang="scss" scoped>
img {
  width: 24px;
  margin: 0.5rem;
  cursor: pointer;
}
</style>