<template>
  <div class="action-icon" :class="size ? size : 'normal'">
    <div class="icon" :class="iconClass"></div>
  </div>
</template>

<script>
import { computed } from "vue";
export default {
  props: ["icon", "size"],
  setup(props) {
    const iconClass = computed(() => `action-button-${props.icon}`);
    return { iconClass };
  },
};
</script>

<style lang="scss" scoped>
.action-icon {
  position: relative;
  &.normal {
    width: 50px;
    height: 50px;
    padding: 0px;
    margin: 0px;
    > .icon {
      background-size: 24px 24px;
      min-width: 24px;
      min-height: 24px;
      left: 13px;
      top: 14px;
    }
  }

  &.small {
    width: 20px;
    height: 20px;
    padding: 0px;
    margin: 0px;
    > .icon {
      background-size: 8px 8px;
      min-width: 8px;
      min-height: 8px;
      left: 6px;
      top: 6px;
    }
  }

  cursor: pointer;
  &:hover {
    background-image: url("../../assets/common/action-button/svg/border-hover.svg");
  }
  &:not(hover) {
    background-image: url("../../assets/common/action-button/svg/border-unselected.svg");
  }
  > .icon {
    position: absolute;
    background-repeat: no-repeat;
    background-position: center;
  }
}
</style>