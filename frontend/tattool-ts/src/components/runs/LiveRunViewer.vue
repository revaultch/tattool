<template>
  <div id="run-panel">
    <div class="title">Running ...</div>
    <run-viewer :run="run"></run-viewer>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent } from "vue";
import useRun from "@/state/useRun";
import { ServerEventHandler } from "@/services/RunService";
import {
  PayloadExecutionEvent,
  RunTerminatedExecutionEvent,
} from "@/model/transport/execution/PayloadExecutionEvent";
import RunViewer from "./RunViewer.vue";
import PayloadEventToRunConverter from "./PayloadEventToRunConverter";
import { Run, Type } from "@/model/transport/execution/Run";
import { useRouter } from "vue-router";
export default defineComponent({
  props: ["reference", "type", "debug"],
  components: { RunViewer },
  setup(props) {
    const router = useRouter();
    const { execute } = useRun();
    const payloadEventToRunConverter = new PayloadEventToRunConverter(
      props.reference,
      `Live execution for ${props.reference}`,
      props.type as Type
    );
    const run = ref<Run | null>(null);

    execute(
      props.reference,
      props.type,
      props.debug as boolean,
      {
        handlePayloadExecutionEvent: (
          payloadExecutionEvent: PayloadExecutionEvent
        ) => {
          run.value = payloadEventToRunConverter.reduce(payloadExecutionEvent);
        },
        handleError: (error: any) => {
          throw new Error(error);
        },
        handleDone: (
          runTerminatedExecutionEvent: RunTerminatedExecutionEvent
        ) => {
          router.push({ path: `/runs/${runTerminatedExecutionEvent.runId}` });
        },
      } as ServerEventHandler
    );

    return { run };
  },
});
</script>

<style lang="scss">
#run-panel {
  @extend .page;
}
</style>