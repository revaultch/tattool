<template>
  <div class="datasets">
    <div class="title">Datasets</div>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Description</th>
          <th></th>
        </tr>
      </thead>
      <tbody class="stripe">
        <tr v-for="dataset in datasetList" :key="dataset.id">
          <td>{{ dataset.name }}</td>
          <td>{{ dataset.description }}</td>
          <td class="tools">
            <action-icon
              @click="editDataset(dataset.id)"
              :icon="'edit'"
            ></action-icon>
            <action-icon
              @click="del(dataset.id)"
              :icon="'delete'"
            ></action-icon>
          </td>
        </tr>
      </tbody>
    </table>
    <div id="action-panel">
      <action-icon icon="plus" @click="addNewDataset()" />
    </div>
  </div>
</template>

<script lang="ts">
import { useRouter } from "vue-router";
import ActionIcon from "@/components/common/ActionIcon.vue";
import useDataset from "@/state/useDataset";
import { defineComponent } from "vue";

export default defineComponent({
  components: { ActionIcon },
  setup() {
    const router = useRouter();

    const { loadDatasets, datasetList, deleteDataset } = useDataset();

    loadDatasets();

    const addNewDataset = () => {
      router.push({ path: "/new-dataset" });
    };
    const editDataset = (id: string) => {
      router.push({ path: `/edit-dataset/${id}` });
    };

    const del = (id: string) => deleteDataset(id);

    return { datasetList, addNewDataset, editDataset, del };
  },
});
</script>

<style lang="scss" scoped>
.datasets {
  @extend .page;
  display: flex;
  flex-direction: column;
  height: auto;
  flex: 1;
  > table {
    @extend .item-list;
    width: 100%;
    margin-top: 2rem;
    .tools {
      display: flex;
      flex-direction: row;
      justify-content: flex-end;
      > div {
        margin-right: 1rem;
      }
    }
  }
}
</style>