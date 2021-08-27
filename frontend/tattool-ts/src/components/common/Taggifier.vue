<template>
  <span class="tag" v-for="tag in tagList" :key="tag"
    >{{ tag }}<span> | </span
    ><span class="delete" @click="$emit('delete', tag)"> x</span></span
  >
</template>

<script>
import { computed } from "vue";
export default {
  emits: ["delete"],
  props: ["tags"],
  setup(props) {
    const tagList = computed(() => (props.tags ? props.tags : []));

    return { tagList };
  },
};
</script>

<style lang="scss" scoped>
.tag {
  display: inline-block;
  color: #353b48;
  background-color: #dcdde1;
  margin-right: 0.5rem;
  padding: 0.5rem;
  margin: 0.5rem;
  border-radius: 3px;
  font-weight: 600;
  > span {
    color: #f5f6fa;
    &.delete {
      cursor: pointer;
    }
  }
}
</style>