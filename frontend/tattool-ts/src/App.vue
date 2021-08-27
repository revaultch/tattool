<template>
  <div class="network-error" v-if="isNetworkError">
    A network error occurred. Please check log for details and refresh your
    browser.
  </div>
  <div id="app2" v-else>
    <menu-panel v-if="isLoggedIn"></menu-panel>
    <!-- hidden if not loggedIn-->
    <router-view></router-view>
    <spinner v-if="isLoading"></spinner>
    <login v-if="!isLoggedIn"></login>
  </div>
</template>

<script>
import Spinner from "./components/common/Spinner.vue";
import MenuPanel from "@/components/menu/MenuPanel";
import Login from "@/components/login/Login";
import useUIState from "@/state/useUIState";
import useSession from "@/state/useSession";
export default {
  components: { Spinner, MenuPanel, Login },
  setup() {
    const { isLoading, isNetworkError } = useUIState();
    const { isLoggedIn } = useSession();
    return { isLoading, isLoggedIn, isNetworkError };
  },
};
</script>
<style lang="scss" >
html,
body {
}
#app {
  text-align: center;
  color: #2c3e50;
  height: 100%;
  width: 100%;
  > #app2 {
    background-color: #ffffff;
    height: auto;
    min-height: 100%;
    width: 100%;
    align-content: stretch;
    display: flex;
    flex-direction: row;
    flex: 1;
  }
}

.network-error {
  text-align: center;
  position: absolute;
  top: 50%;
  margin: auto;
  width: 100%;
  font-size: 2rem;
}
</style>
