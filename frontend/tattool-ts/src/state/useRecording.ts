import RecordingService
  from "@/services/RecordingService";

import { ref, computed } from "vue";

import useExecution from "@/state/useExecution";
import useSession from "@/state/useSession";

const recordingService: RecordingService = new RecordingService();

const recordingList = ref([] as Array<Recording>);

const recording = ref({} as Recording);

import useUIState from "@/state/useUIState";
import { Recording } from "@/model/transport/Recording";
const { withLoading } = useUIState();
const { log } = useExecution();
const { takeScreenshot } = useSession();

export default function useRecording() {

  function updateCurrentRecording(newRecording: Recording) {
    recording.value = newRecording;
  }

  async function loadRecording(recordingId: string) {
    await withLoading("loading recording", recordingService.getOne(recordingId).then(updateCurrentRecording));
  }

  async function loadRecordings() {
    recordingList.value = await withLoading("loading recordings", recordingService.getAll());
  }

  async function saveRecording() {
    await withLoading(
      "saving recording",
      recordingService.saveOne(recording.value).then(loadRecordings)
    );
  }

  async function deleteRecording(id: string) {
    withLoading("deleting recording", recordingService.deleteOne(id))
      .then(loadRecordings);
  }


  return {
    saveRecording,
    deleteRecording,
    loadRecordings,
    loadRecording,
    updateCurrentRecording,
    recording: computed(() => recording.value as Recording),
    recordingList: computed(() => recordingList.value),
  };
}
