<template>
  <div class="element-super-locator-selector">
    <select-template
      v-if="!readOnly"
      :itemsProvider="() => contextData[`${field}`]"
      :itemFilter="(item, term) => applyFilter(item, term)"
      :valueRenderer="(item) => render(item)"
      :itemRenderer="(item) => render(item)"
      :value="value"
      @change="handleChange($event)"
    ></select-template>
    <div v-else v-html="render(value)"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import SelectTemplate from "@/components/recordings/editor/input-panel/recording-step/SelectTemplate.vue";
import { ElementSuperLocator } from "@/model/transport/ElementSuperLocator";
import { PayloadChangeEvent } from "@/model/common/Payload";
import useSession from "@/state/useSession";
import usePlatform from "@/state/usePlatform";

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
      required: false,
    },
    value: {
      type: Object as PropType<any>,
      required: false,
    },
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["change"],
  setup(props, { emit }) {
    const { sessionId, takeScreenshot } = useSession();
    const { currentPlatform } = usePlatform();

    const applyFilter = (item: ElementSuperLocator, term: string) => {
      return item.asString?.toLowerCase().includes(term?.toLowerCase());
    };

    const render = (item: ElementSuperLocator) =>
      `<img src='${item.screenshot}'/>`;

    const handleChange = (item: ElementSuperLocator) => {
      takeScreenshot(currentPlatform.value!.id, sessionId.value!, item.id);
      emit("change", {
        field: props.field,
        payloadItem: item,
      } as PayloadChangeEvent);
    };

    return { applyFilter, render, handleChange };
  },
});
</script>

<style lang="scss">
.element-super-locator-selector {
  img {
    max-width: 100%;
  }
}
</style>