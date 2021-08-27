<template>
  <div id="dataset-input-panel">
    <div class="title">Dataset</div>
    <div id="form">
      <div id="name" class="field" :class="{ error: !isNameValid }">
        <input type="text" placeholder="Name" v-model="dirtyDataset.name" />
      </div>
      <div
        id="description"
        class="field"
        :class="{ error: !isDescriptionValid }"
      >
        <textarea
          rows="6"
          placeholder="Description"
          v-model="dirtyDataset.description"
        ></textarea>
      </div>

      <div id="dataset" class="field dataset">
        <table>
          <thead>
            <tr>
              <th
                v-for="(col, index) in currentDatasetContentModel.content
                  .columns"
                :key="col"
              >
                <div class="header-column-content">
                  <input
                    type="text"
                    :value="col"
                    @change="
                      currentDatasetContentModel.content.columns[index] =
                        $event.target.value
                    "
                  />
                  <div class="action-buttons">
                    <action-icon
                      icon="plus"
                      size="small"
                      @click="currentDatasetContentModel.addColumn(index + 1)"
                    ></action-icon>
                    <action-icon
                      icon="delete"
                      size="small"
                      @click="currentDatasetContentModel.removeColumn(index)"
                    ></action-icon>
                  </div>
                </div>
              </th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(row, rowIndex) in currentDatasetContentModel.content.rows"
              :key="row"
            >
              <td v-for="(cell, cellIndex) in row" :key="cell">
                <input
                  type="text"
                  :value="cell"
                  @change="row[cellIndex] = $event.target.value"
                />
              </td>
              <td>
                <action-icon
                  icon="delete"
                  @click="currentDatasetContentModel.removeRow(rowIndex)"
                ></action-icon>
              </td>
            </tr>
          </tbody>
        </table>
        <action-icon
          class="add-empty-row"
          icon="plus"
          @click="currentDatasetContentModel.addNewRow()"
        />
      </div>

      <div id="commit-panel">
        <button :disabled="!isFormValid" class="yes" @click="save()">
          Save
        </button>
        <button class="no" @click="goBackToDatasets()">Cancel</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, ref, watch, defineComponent } from "vue";
import { useRouter } from "vue-router";
import useDataset from "@/state/useDataset";
import { DatasetContentModel } from "./DatasetContentModel";
import { Dataset, Content } from "@/model/transport/Dataset";
import ActionIcon from "@/components/common/ActionIcon.vue";

export default defineComponent({
  props: ["id"],
  components: { ActionIcon },
  setup(props) {
    const router = useRouter();
    const currentDatasetContentModel = ref(new DatasetContentModel());
    const dirtyDataset = ref({} as Dataset);
    const { loadDataset, saveDataset, dataset } = useDataset();

    if (props.id != null) {
      loadDataset(props.id).then(() => {
        dirtyDataset.value = dataset.value as Dataset;
      });
    }

    watch(
      () => dataset.value,
      // eslint-disable-next-line no-unused-vars
      (newId, oldId) => {
        currentDatasetContentModel.value.content = dataset.value
          .content as Content;
      }
    );

    const goBackToDatasets = () => {
      router.push({ path: "/datasets" });
    };

    const save = () => {
      saveDataset({
        ...dirtyDataset.value,
        ...{ content: currentDatasetContentModel.value.content },
      }).then(() => goBackToDatasets());
    };

    const isNameValid = computed(
      () => dirtyDataset.value.name && dirtyDataset.value.name.trim() !== ""
    );

    const isDescriptionValid = computed(
      () =>
        dirtyDataset.value.description &&
        dirtyDataset.value.description.trim() !== ""
    );

    const isFormValid = computed(
      () => isNameValid.value && isDescriptionValid.value
    );

    return {
      save,
      goBackToDatasets,
      isNameValid,
      isDescriptionValid,
      isFormValid,
      dirtyDataset,
      currentDatasetContentModel,
    };
  },
});
</script>

<style lang="scss" scoped>
#dataset-input-panel {
  @extend .page;
  display: flex;
  flex-direction: column;
  flex: 1;
  width: auto;
  overflow-y: scroll;
  height: 100%;
  padding-right: 10px !important;
  margin-right: 5px !important;

  .header-column-content {
    display: flex;
    flex-direction: row;
    align-items: center;
    > .action-buttons {
      display: flex;
      flex-direction: column;
      padding-left: 0.5rem;
    }
  }

  .field {
    &.tags {
      input {
        margin-top: 1rem;
      }
    }
    &#name {
      width: 40%;
    }
    &#description {
      width: 90%;
    }
  }

  #dataset {
    > div {
      padding-top: 1rem;
      padding-bottom: 1rem;
      font-weight: 600;
    }
    > table {
      @extend .item-list;
    }
  }

  #commit-panel {
    margin-top: 4rem;
    margin-bottom: 4rem;
    display: flex;
    flex-direction: row;
    align-content: flex-end;
    justify-content: flex-end;
    width: 100%;
  }

  .add-empty-row {
    margin-top: 2rem;
    margin-bottom: 2rem;
    margin-left: auto;
    margin-right: auto;
  }
}
</style>