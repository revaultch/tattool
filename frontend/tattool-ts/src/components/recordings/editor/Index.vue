<template>
  <div id="recorder">
    <splitpanes class="default-theme" :horizontal="horizontal">
      <pane min-size="20">
        <recorder-input-panel :id="id"></recorder-input-panel>
      </pane>
      <pane>
        <recorder-output-panel></recorder-output-panel>
      </pane>
    </splitpanes>
  </div>
</template>

<script>
import RecorderInputPanel from "./input-panel/RecorderInputPanel";
import RecorderOutputPanel from "./output-panel/RecorderOutputPanel";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";

export default {
  props: ["id"],
  components: {
    RecorderInputPanel,
    RecorderOutputPanel,
    Splitpanes,
    Pane,
  },
  setup() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
#recorder {
  display: flex;
  flex: 1;
  align-content: stretch;
  height: 100vh;
}
</style>