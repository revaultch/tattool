<template>
  <div class="option-selector">
    <select-template
      v-if="!readOnly && value?.elementSuperLocator"
      :itemsProvider="
        () => contextData.selection.options[value.elementSuperLocator.id]
      "
      :itemFilter="
        (item, term) => item?.toLowerCase().includes(term?.toLowerCase())
      "
      :valueRenderer="(item) => item"
      :itemRenderer="(item) => item"
      :value="value?.option"
      @change="handleOptionChange($event)"
    ></select-template>
    <div v-else class="value">{{ value?.option }}</div>
    <div class="prepend">from</div>
    <element-super-locator-selector
      :value="value?.elementSuperLocator"
      :readOnly="readOnly"
      :contextData="contextData?.selection"
      field="elementSuperLocatorList"
      @change="handleElementSuperLocatorChange($event)"
    />
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import SelectTemplate from "@/components/recordings/editor/input-panel/recording-step/SelectTemplate.vue";
import ElementSuperLocatorSelector from "./ElementSuperLocatorSelector.vue";
import { PayloadChangeEvent } from "@/model/common/Payload";
import { SelectedItem } from "@/model/common/SelectedItem";

export default defineComponent({
  components: {
    SelectTemplate,
    ElementSuperLocatorSelector,
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
      type: Object as PropType<SelectedItem>,
      required: false,
      default: null,
    },
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["change"],
  setup(props, { emit }) {
    const handleOptionChange = (option: string) => {
      emit("change", {
        field: props.field,
        payloadItem: {
          option: option,
          elementSuperLocator: props.value.elementSuperLocator,
        },
      } as PayloadChangeEvent);
    };

    const handleElementSuperLocatorChange = (elementSuperLocator: any) => {
      emit("change", {
        field: props.field,
        payloadItem: {
          option: props.value ? props.value.option : null,
          elementSuperLocator: elementSuperLocator.payloadItem,
        },
      } as PayloadChangeEvent);
    };

    return {
      handleOptionChange,
      handleElementSuperLocatorChange,
    };
  },
});
</script>

<style lang="scss">
.option-selector {
  .value {
    text-decoration: underline;
    font-style: italic;
  }
  .prepend {
    padding-top: 1rem;
    padding-bottom: 1rem;
  }
}
</style>