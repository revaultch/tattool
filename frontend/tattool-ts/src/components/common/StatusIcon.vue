<template>
  <div
    class="status-icon"
    :class="{
      ok: status == 'SUCCESS',
      warn: status == 'WARNING',
      error: status == 'FAILURE',
      running: status == 'RUNNING',
    }"
  ></div>
</template>

<script>
export default {
  props: ["status"],
  setup(props) {
    return {};
  },
};
</script>

<style lang="scss" >
.status-icon {
  background-size: 3rem;
  height: 3rem;
  width: 3rem;
  &.ok {
    background-image: url("~@/assets/state/svg/ok.svg");
  }
  &.warn {
    background-image: url("~@/assets/state/svg/warn.svg");
  }
  &.running {
    background-image: url("~@/assets/state/svg/running.svg");
  }
  &.error {
    background-image: url("~@/assets/state/svg/error.svg");
  }
}
</style>