<template>
  <div class="browser">
    <div class="header">
      <img src="@/assets/browser/svg/bullet.svg" /><img
        src="@/assets/browser/svg/bullet.svg"
      /><img src="@/assets/browser/svg/bullet.svg" />
    </div>
    <div class="body"><img :src="img" /></div>
  </div>
</template>

<script>
import { computed } from "vue";
import useSession from "@/state/useSession";

export default {
  setup() {
    const { lastScreenshot } = useSession();
    const img = computed(() => {
      if (lastScreenshot.value) {
        return lastScreenshot.value;
      } else {
        return require("@/assets/browser/png/placeholder.png");
      }
    });
    return { img };
  },
};
</script>

<style lang="scss" scoped>
.browser {
  background-color: #dcdde1;
  border-radius: 4px;
  height: auto;

  padding: 2px;
  .header {
    height: 40px;
    text-align: left;
    padding: 10px;
    > img {
      width: 20px;
      padding: 5px;
    }
  }
  .body {
    height: auto;
    > img {
      display: block;
      margin: auto;
      max-width: 100%;
    }
  }
}
</style>