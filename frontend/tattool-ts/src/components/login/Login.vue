<template>
  <div id="loginForm">
    <input type="text" name="user" v-model="user" />
    <input type="password" name="password" v-model="password" />
    <button @click="login()">Login</button>
  </div>
</template>

<script>
import { ref } from "vue";
export default {
  setup() {
    let user = ref("");
    let password = ref("");
    const login = function () {
      throw Error("not implemented");
    };
    return { user, password, login };
  },
};
</script>

<style lang="scss" scoped>
#loginForm {
  position: fixed;
  display: flex;
  flex-direction: column;
}
</style>