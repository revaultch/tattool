<template>
  <div class="select-template" :class="{ hover: hover }">
    <input
      type="search"
      name="text"
      class="text"
      v-model="filter"
      @keyup="handleFilterKeyup($event)"
      @keydown.tab="handleFilterTab($event)"
      @keydown.esc="handleEsc($event)"
      v-if="filterOn"
      autocomplete="off"
    />
    <div
      @mouseover="hover = true"
      @mouseout="hover = false"
      class="value"
      v-if="!filterOn"
      @click="select(null)"
      v-html="valueRenderer && selected ? valueRenderer(selected) : 'nothing'"
    ></div>
    <div class="selector" v-if="selectorOpen">
      <div
        v-for="item in filteredItems"
        :key="item"
        @click="select(item)"
        v-html="itemRenderer ? itemRenderer(item) : ''"
      ></div>
    </div>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, ref, PropType } from "vue";

export default defineComponent({
  props: {
    itemsProvider: {
      type: Function as PropType<() => Array<any>>,
      required: true,
    },
    itemRenderer: {
      type: Function as PropType<(item: any) => string>,
      required: true,
    },
    itemFilter: {
      type: Function as PropType<(items: Array<any>, term: string) => string>,
      required: true,
    },
    valueRenderer: {
      type: Function as PropType<(item: any) => string>,
      required: true,
    },
    value: {
      type: Object as PropType<any>,
      default: null,
    },
  },
  emits: ["change"],
  setup(props, { emit }) {
    const hover = ref(false);
    const filter = ref("");
    const selected = ref(props.value);
    const filterOn = computed(() => selected.value == null);
    const filteredItems = computed(() =>
      props.itemsProvider
        ? props
            .itemsProvider()
            .filter((item) =>
              filter.value != ""
                ? props.itemFilter(item, filter.value)
                : props.itemsProvider()
            )
        : []
    );
    const selectorOpen = computed(
      () => filteredItems.value.length > 0 && selected.value === null
    );

    const select = (item: any) => {
      selected.value = item;
      emit("change", selected.value);
    };

    const handleFilterKeyup = (evt: Event) => {
      selected.value = null;
    };

    const handleFilterTab = (evt: Event) => {
      if (filteredItems.value && filteredItems.value.length == 1) {
        select(filteredItems.value[0]);
      }
    };

    const handleEsc = (item: any) => {
      select(props.value);
    };

    return {
      hover,
      filterOn,
      filter,
      filteredItems,
      selectorOpen,
      selected,
      select,
      handleFilterKeyup,
      handleFilterTab,
      handleEsc,
    };
  },
});
</script>

<style lang="scss" scoped>
.select-template {
  width: 100%;
  border: 1px solid transparent;
  &.hover {
    border: 1px solid #dddddd;
    cursor: pointer;
    background-image: url("~@/assets/common/svg/arrow-down.svg");
    background-repeat: no-repeat;
    background-position: 97%;
    background-size: 20px;
  }
  > .text {
    font-size: 1rem;
    width: 100%;
    padding: 1rem;
  }
  > .selector {
    border: 1px solid;
    margin: 0;
    padding: 0;
    > div {
      font-size: 1rem;
      cursor: pointer;
      padding: 1.5rem;
    }
    > div:nth-child(2n + 1) {
      background-color: #eeeeee;
    }
  }
}
</style>