<template>
  <div id="session-output-panel">
    <Browser></Browser>
    <console></console>
  </div>
</template>

<script>
import Browser from "@/components/recordings/editor/output-panel/Browser";
import Console from "@/components/recordings/editor/output-panel/Console";
export default {
  components: { Browser, Console },
  setup() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
#session-output-panel {
  flex: 1;
  margin: 1rem;
  padding: 1rem;
  border-radius: 4px;
  overflow: auto;
  height: 100%;
}
</style>