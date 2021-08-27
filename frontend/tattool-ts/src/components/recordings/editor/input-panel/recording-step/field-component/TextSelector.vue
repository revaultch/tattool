<template>
  <div class="text-selector">
    <select-template
      v-if="!readOnly"
      :itemsProvider="() => contextData[`${field}`]"
      :itemFilter="(item, term) => applyFilter(item, term)"
      :valueRenderer="(item) => render(item)"
      :itemRenderer="(item) => render(item)"
      :value="value"
      @change="handleChange($event)"
    ></select-template>
    <div v-else>{{ value }}</div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import SelectTemplate from "@/components/recordings/editor/input-panel/recording-step/SelectTemplate.vue";
import { PayloadChangeEvent } from "@/model/common/Payload";

export default defineComponent({
  components: {
    SelectTemplate,
  },
  props: {
    field: {
      type: String,
      required: true,
    },
    contextData: {
      type: Object as PropType<any>,
      required: true,
    },
    value: {
      type: Object as PropType<any>,
      required: false,
      default: null,
    },
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const applyFilter = (item: string, term: string) =>
      item?.toLowerCase().includes(term?.toLowerCase());

    const render = (item: string) => item;

    const handleChange = (item: string) => {
      emit("change", {
        field: props.field,
        payloadItem: item,
      } as PayloadChangeEvent);
    };

    return { applyFilter, render, handleChange };
  },
});
</script>

<style>
</style>