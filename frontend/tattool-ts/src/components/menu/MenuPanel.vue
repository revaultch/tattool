<template>
  <div id="menu">
    <div id="logo"></div>
    <div id="items">
      <div
        id="dashboard"
        @click="router.push({ path: '/dashboard' })"
        :class="{
          selected: currentMenuId == MenuItem.DASHBOARD,
        }"
      >
        <div class="text">DASHBOARD</div>
        <div class="marker"></div>
      </div>
      <div
        id="datasets"
        @click="router.push({ path: '/datasets' })"
        :class="{ selected: currentMenuId == MenuItem.DATASETS }"
      >
        <div class="text">DATASETS</div>
        <div class="marker"></div>
      </div>
      <div
        id="recording-filters"
        @click="router.push({ path: '/recordingfilters' })"
        :class="{ selected: currentMenuId == MenuItem.RECORDINGFILTERS }"
      >
        <div class="text">RECORDING FILTERS</div>
        <div class="marker"></div>
      </div>
      <div
        id="recordings"
        @click="router.push({ path: '/recordings' })"
        :class="{
          selected: currentMenuId == MenuItem.RECORDINGS,
        }"
      >
        <div class="text">RECORDINGS</div>
        <div class="marker"></div>
      </div>
      <div
        id="settings"
        @click="router.push({ path: '/settings' })"
        :class="{ selected: currentMenuId == MenuItem.SETTINGS }"
      >
        <div class="text">SETTINGS</div>
        <div class="marker"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import useUIState from "@/state/useUIState";
export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const { currentMenuId, MenuItem } = useUIState();
    const currentRoutePath = computed(() => (route.path ? route.path : ""));
    return { currentRoutePath, router, MenuItem, currentMenuId };
  },
};
</script>

<style lang="scss" scoped>
#menu {
  background-color: #353b48;
  max-width: 6rem;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: flex-start;
  width: 70px;
  height: auto;
  overflow: hidden;
  #logo {
    background-image: url("../../assets/menu/svg/logo.svg");
    background-repeat: no-repeat;
    background-position: center;
    background-size: 3rem;
    min-height: 7rem;
  }
  #items {
    display: flex;
    flex-direction: column;
    margin-top: 8rem;
    > div {
      position: relative;
      width: 100%;
      color: #888888;
      background-repeat: no-repeat;
      background-position: center;
      background-size: 3.5rem;
      min-height: 8rem;

      > .text {
        position: absolute;
        bottom: 1rem;
        font-size: 0.6rem;
        width: 100%;
        text-align: center;
        color: #7f8fa6;
      }

      &.selected {
        > .marker {
          position: absolute;
          height: 70%;
          left: 0;
          bottom: 10px;
          width: 3px;
          background-color: #e1b12c;
        }
        > .text {
          color: white;
        }
      }
      &#dashboard {
        background-image: url("~@/assets/menu/svg/item-dashboard-grey.svg");
        cursor: pointer;
        &:hover {
          background-image: url("~@/assets/menu/svg/item-dashboard-white.svg");
        }
        &.selected {
          background-image: url("~@/assets/menu/svg/item-dashboard-white.svg");
        }
      }
      &#datasets {
        background-image: url("~@/assets/menu/svg/item-datasets-grey.svg");
        cursor: pointer;
        &:hover {
          background-image: url("~@/assets/menu/svg/item-datasets-white.svg");
        }
        &.selected {
          background-image: url("~@/assets/menu/svg/item-datasets-white.svg");
        }
      }
      &#recording-filters {
        background-image: url("~@/assets/menu/svg/item-recordingfilters-grey.svg");
        cursor: pointer;
        &:hover {
          background-image: url("~@/assets/menu/svg/item-recordingfilters-white.svg");
        }
        &.selected {
          background-image: url("~@/assets/menu/svg/item-recordingfilters-white.svg");
        }
      }
      &#recordings {
        background-image: url("~@/assets/menu/svg/item-recordings-grey.svg");
        cursor: pointer;
        &:hover {
          background-image: url("~@/assets/menu/svg/item-recordings-white.svg");
        }
        &.selected {
          background-image: url("~@/assets/menu/svg/item-recordings-white.svg");
        }
      }
      &#settings {
        background-image: url("~@/assets/menu/svg/item-settings-grey.svg");
        cursor: pointer;
        &:hover {
          background-image: url("~@/assets/menu/svg/item-settings-white.svg");
        }
        &.selected {
          background-image: url("~@/assets/menu/svg/item-settings-white.svg");
        }
      }
    }
  }
}
</style>