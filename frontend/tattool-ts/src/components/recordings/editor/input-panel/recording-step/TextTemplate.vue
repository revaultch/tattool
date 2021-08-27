<template>
  <div class="container">
    <input
      type="search"
      :value="value"
      :placeholder="placeholder"
      @change="handleChange($event)"
      @keydown.tab="handleTab($event)"
      :readonly="readOnly"
      autocomplete="off"
    />
  </div>
</template>
<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  props: {
    value: {
      type: String,
      required: false,
      default: null,
    },
    placeholder: {
      type: String,
      required: false,
      default: null,
    },

    readOnly: {
      type: Boolean,
      required: false,
    },
  },
  emits: ["change", "destroy", "next"],
  setup(props, { emit }) {
    const handleTab = (evt: Event) => {
      emit("next", evt);
    };

    const handleChange = (evt: any) => {
      emit("change", evt.target.value);
    };

    return {
      handleTab,
      handleChange,
    };
  },
});
</script>

<style lang="scss">
</style>