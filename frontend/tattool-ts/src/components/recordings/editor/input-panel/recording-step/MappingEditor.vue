<template>
  <div class="mapping-editor">
    <button v-if="isUnset" class="map" @click="handleEdit()"></button>
    <div v-if="isSet" class="value">{{ value }}</div>
    <div v-if="isEditing" class="editing">
      <select-template
        class="column-selector"
        :itemsProvider="() => mappableColumns"
        :itemRenderer="(item) => item"
        :itemFilter="(item, term) => item.includes(term)"
        :valueRenderer="(item) => item"
        :value="value"
        :readOnly="readOnly"
        @change="handleSet($event)"
      />
    </div>
    <button v-if="isSet" class="unset" @click="handleUnset()"></button>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, ref, computed, watch } from "vue";
import SelectTemplate from "@/components/recordings/editor/input-panel/recording-step/SelectTemplate.vue";

export default defineComponent({
  props: {
    mappableColumns: {
      type: Array as PropType<Array<string>>,
      required: true,
    },
    value: {
      type: String as PropType<string | null>,
      default: null,
    },
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  components: { SelectTemplate },
  emits: ["change"],
  setup(props, { emit }) {
    enum states {
      UNSET,
      EDITING,
      SET,
    }

    const evalState = () => (props.value ? states.SET : states.UNSET);
    const state = ref<states>(evalState());

    const isUnset = computed(() => state.value === states.UNSET);
    const isEditing = computed(() => state.value === states.EDITING);
    const isSet = computed(() => state.value === states.SET);

    const handleEdit = () => {
      state.value = states.EDITING;
    };

    const handleSet = (value: string) => {
      state.value = states.SET;
      emit("change", value);
    };

    const handleUnset = () => {
      state.value = states.UNSET;
      emit("change", undefined);
    };

    watch(
      () => props.value,
      (value: any, oldValue: any) => {
        state.value = evalState();
      }
    );

    return { isUnset, isEditing, isSet, handleEdit, handleSet, handleUnset };
  },
});
</script>

<style lang="scss" scoped>
.mapping-editor {
  background-image: url("~@/assets/common/svg/map.svg");
  background-repeat: no-repeat;
  background-size: 1.5rem 1.5rem;
  margin-left: 0.5rem;
  display: flex;
  flex-direction: row;
  min-height: 1.5rem;
  > .value {
    margin-left: 2rem;
    font-weight: 600;
  }
  > .map {
    background-image: url("~@/assets/common/svg/map-plus.svg");
    background-repeat: no-repeat;
    background-color: transparent;
    background-position: center;
  }
  > .unset {
    background-color: transparent;
    background-image: url("~@/assets/common/svg/map-delete.svg");
    background-repeat: no-repeat;
  }
}
</style>