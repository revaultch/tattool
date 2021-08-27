<template>
  <div class="selector">
    <div class="selected" @click="showSelectableItems($event)">
      <div v-if="selected == null">{{ placeholder }}</div>
      <span v-if="body && selected != null" v-html="body(selected)"></span>
      <img
        v-if="bodyImage && selected != null"
        :src="bodyImageFull(selected)"
      />
      <div class="arrow"></div>
    </div>
    <div
      class="selectable-container"
      @mouseover="overSelectable = true"
      @mouseout="overSelectable = false"
      :class="{
        visible: selectableItemsVisible,
        openUp: clickHeightPct > 50,
        openDown: clickHeightPct <= 50,
      }"
    >
      <div
        class="selectable"
        v-for="item in selectable"
        :key="id(item)"
        @click="select(item)"
      >
        <span v-if="body" v-html="body(item)"></span>
        <img
          v-if="bodyImage"
          :src="bodyImageFull(item)"
          :alt="JSON.stringify(item)"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
export default {
  name: "Selector",
  props: ["placeholder", "selectable", "id", "body", "bodyImage"],
  emits: ["changed"],
  setup(props, { emit }) {
    const selectableItemsVisible = ref(false);
    const overSelectable = ref(false);
    const clickHeightPct = ref(0);

    const bodyImageFull = (item) => {
      if (item.screenshot) {
        return props.bodyImage(item);
      } else {
        return "";
      }
    };
    const selected = ref(null);

    const checkSelectableVisibility = () => {
      if (!overSelectable.value) {
        hideSelectableItems();
      }
      setTimeout(checkSelectableVisibility, 2000);
    };

    const hideSelectableItems = () => {
      selectableItemsVisible.value = false;
    };

    const showSelectableItems = ($event) => {
      clickHeightPct.value = ($event.pageY * 100) / window.innerHeight;
      selectableItemsVisible.value = true;
      setTimeout(checkSelectableVisibility, 2000);
    };

    const select = (item) => {
      selected.value = item;
      emit("changed", selected.value);
      hideSelectableItems();
      overSelectable.value = false;
    };

    return {
      selectableItemsVisible,
      bodyImageFull,
      hideSelectableItems,
      showSelectableItems,
      select,
      selected,
      overSelectable,
      clickHeightPct,
    };
  },
};
</script>

<style lang="scss" scoped>
.selector {
  cursor: pointer;
  position: relative;
  height: auto;
  width: auto;
  padding: 0px;
  margin: 0px;
  .selected {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: auto;
    border-radius: 2px;
    border: 2px solid #d9dde4;
    padding-left: 0.5rem;
    padding-top: 1rem;
    padding-bottom: 1rem;
    border-radius: 3px;
    text-align: left;
    > .arrow {
      background-image: url("~@/assets/common/svg/arrow-down.svg");
      background-repeat: no-repeat;
      background-position: center;
      padding-left: 1rem;
      padding-right: 0.5rem;
      width: 20px;
      height: 20px;
      background-size: 20px 20px;
    }
  }
  .selectable-container {
    width: 100%;
    background-color: #f0f0f0;
    position: absolute;
    display: none;
    left: 0;
    max-height: 60vh;
    overflow-y: auto;
    overflow-x: hidden;
    border-radius: 4px;
    border: 2px solid #aaa;
    &.openUp {
      bottom: 0px;
    }
    &.openDown {
      top: 100%;
    }
    &.visible {
      z-index: 1000;
      display: flex;
      flex-direction: column;
    }

    > .selectable {
      padding: 20px;
      display: flex;
      position: relative;
      border-top: 1px solid white;
      width: 100%;
      height: auto;
      margin: auto;
      justify-content: center; // todo check alignment for img
      align-items: center;
      min-height: 60px;
      max-height: 80px;
      overflow: hidden;
      > img {
        position: absolute;
        left: 0;
        top: 0;
      }
    }
  }
}
</style>