<template>
  <div class="text-editor">
    <text-template
      v-if="!readOnly"
      :placeholder="field"
      :value="value"
      @change="handleChange($event)"
    ></text-template>
    <div v-else>{{ value }}</div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import TextTemplate from "@/components/recordings/editor/input-panel/recording-step/TextTemplate.vue";
import { PayloadChangeEvent } from "@/model/common/Payload";

export default defineComponent({
  components: {
    TextTemplate,
  },
  emits: ["change"],
  props: {
    field: {
      type: String,
      required: true,
    },
    // not required here
    contextData: {
      type: Object as PropType<any>,
      required: false,
      default: null,
    },
    value: {
      type: String,
      required: false,
    },
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const handleChange = (item: string) => {
      emit("change", {
        field: props.field,
        payloadItem: item,
      } as PayloadChangeEvent);
    };
    return { handleChange };
  },
});
</script>

<style>
</style>