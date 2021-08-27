import { ref, computed } from "vue";
import Notiflix from "notiflix";

Notiflix.Notify.init({
  position: "right-bottom",
});

enum MenuItem {
  DASHBOARD,
  DATASETS,
  RECORDINGFILTERS,
  RECORDINGS,
  SETTINGS
}

const isLoadingCount = ref(0);
const isNetworkError = ref(false);
const loadingMessage = ref("");
const currentMenuId = ref<MenuItem | null>(null);

export default function useUIState() {

  function setCurrentMenuId(menuItem: MenuItem) {
    currentMenuId.value = menuItem;
  }

  async function loading(msg: string) {
    isLoadingCount.value++;
    loadingMessage.value = msg;
  }

  function doneLoading() {
    isLoadingCount.value--;
  }

  async function withLoading<T>(msg: string, promise: Promise<T>): Promise<T> {
    loading(msg);
    return promise.then((data) => {
      doneLoading();
      return data;
    });
  }

  function flagNetworkError() {
    isNetworkError.value = true;
  }

  function notifyFailure(failure: string): void {
    Notiflix.Notify.failure(failure);
  }

  function notifySuccess(success: string): void {
    Notiflix.Notify.success(success);
  }


  return {
    loading,
    doneLoading,
    withLoading,
    flagNetworkError,
    notifySuccess,
    notifyFailure,
    MenuItem,
    setCurrentMenuId,
    currentMenuId: computed(() => currentMenuId.value),
    isNetworkError: computed(() => isNetworkError.value),
    isLoading: computed(() => isLoadingCount.value > 0),
    loadingMessage: computed(() => loadingMessage.value),
  };
}
