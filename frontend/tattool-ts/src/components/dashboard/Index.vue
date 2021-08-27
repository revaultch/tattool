
<template>
  <div class="dashboard">
    <div class="title">Dashboard</div>
    <div class="runs">
      <div class="title">Last runs</div>
      <table>
        <thead>
          <tr>
            <th>Id</th>
            <th>Title</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody class="stripe">
          <tr v-for="run in runs" :key="run.id" @click="openRun(run)">
            <td>{{ run.id }}</td>
            <td>{{ run.title }}</td>
            <td>{{ run.status }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import { Run } from "@/model/transport/execution/Run";
import { RunService } from "@/services/RunService";
import { ref } from "vue";
import { defineComponent } from "vue";
import { useRouter } from "vue-router";

export default defineComponent({
  setup() {
    const router = useRouter();
    const runService = new RunService();
    const runs = ref<Array<Run>>([]);
    runService.getAll().then((items) => (runs.value = items));
    const openRun = (run: Run) => router.push({ path: `/runs/${run.id}` });
    return { runs, openRun };
  },
});
</script>

<style lang="scss" scoped>
.dashboard {
  @extend .page;
  display: flex;
  flex-direction: column;
  height: auto;
  flex: 1;

  > .runs {
    margin-top: 2rem;
    .title {
      font-size: 2rem;
    }
    > table {
      @extend .item-list;
      width: 100%;
      margin-top: 1rem;
      tr {
        cursor: pointer;
      }
    }
  }
}
</style>