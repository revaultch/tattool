import { ref, computed } from "vue";

const executionLog = ref([] as Array<any>);

export default function useExecution() {
  function log(content: any) {
    executionLog.value.push(content);
  }

  return { log, executionLog: computed(() => executionLog.value) };
}
