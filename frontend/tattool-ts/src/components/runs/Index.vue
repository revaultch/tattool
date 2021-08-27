<template>
  <div id="run-panel" v-if="run">
    <div class="title">
      Run details for ...
      <div>{{ run.title }}</div>
    </div>
    <run-viewer v-if="run" :run="run" />
  </div>
</template>

<script lang="ts">
import { ref, defineComponent } from "vue";
import RunViewer from "./RunViewer.vue";
import { RunService } from "@/services/RunService";
import { Run } from "@/model/transport/execution/Run";
export default defineComponent({
  props: ["id"],
  components: { RunViewer },
  setup(props) {
    const run = ref<Run | null>(null);
    const runService: RunService = new RunService();
    runService.getOne(props.id).then((newRun: Run) => (run.value = newRun));
    return { run };
  },
});
</script>

<style lang="scss">
.title {
  > div {
    font-weight: 400;
    padding-top: 2rem;
  }
}
</style>