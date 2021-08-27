import { Dataset } from "@/model/transport/Dataset";
import DatasetService from "@/services/DatasetService";
import useUIState from "@/state/useUIState";

import { ref, computed } from "vue";

const datasetService: DatasetService = new DatasetService();

const datasetList = ref([] as Array<Dataset>);
const dataset = ref({} as Dataset);

const { withLoading } = useUIState();

export default function useDataset() {
  async function loadDatasets() {
    datasetList.value = await withLoading("loading datasets", datasetService.getAll());
  }

  async function loadDataset(id: string) {
    dataset.value = await withLoading("loading dataset", datasetService.getOne(id));
  }

  async function saveDataset(dataset: Dataset) {
    withLoading("saving dataset", datasetService.saveOne(dataset).then(loadDatasets));
  }

  function deleteDataset(id: string) {
    withLoading("deleting dataset", datasetService.deleteOne(id).then(loadDatasets));
  }


  return {
    loadDataset,
    loadDatasets,
    saveDataset,
    deleteDataset,
    datasetList: computed(() => datasetList.value),
    dataset: computed(() => dataset.value),
  };
}
