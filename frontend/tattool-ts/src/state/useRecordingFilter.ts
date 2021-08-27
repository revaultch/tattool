import RecordingFilterService from "@/services/RecordingFilterService";
import { ref, computed } from "vue";
import useUIState from "@/state/useUIState";
import { RecordingFilter } from "@/model/transport/RecordingFilter";

const recordingFilterService: RecordingFilterService = new RecordingFilterService();

const recordingFilterList = ref([] as Array<RecordingFilter>);

const recordingFilter = ref({} as RecordingFilter);

const { withLoading } = useUIState();


export default function useRecordingFilter() {

  async function loadRecordingFilters() {
    recordingFilterList.value = await withLoading("loading recording filters", recordingFilterService.getAll());
  }

  async function loadRecordingFilter(id: string) {
    recordingFilter.value = await withLoading("loading recording filter", recordingFilterService.getOne(id));
  }

  async function saveRecordingFilter(recordingFilter: RecordingFilter) {
    await withLoading("loading recording filter", recordingFilterService.saveOne(recordingFilter).then(loadRecordingFilters));
  }

  async function deleteRecordingFilter(id: string) {
    await withLoading("deleting recording filter", recordingFilterService.deleteOne(id).then(loadRecordingFilters));
  }

  return {
    loadRecordingFilter,
    loadRecordingFilters,
    saveRecordingFilter,
    deleteRecordingFilter,
    recordingFilterList: computed(() => recordingFilterList.value),
    recordingFilter: computed(() => recordingFilter.value),
  };
}
