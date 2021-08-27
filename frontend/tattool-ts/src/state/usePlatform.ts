import Platform from "@/model/transport/Platform";
import PlatformService from "@/services/PlatformService";

import useUIState from "@/state/useUIState";

import { ref, computed } from "vue";

const { withLoading } = useUIState();

const platformService: PlatformService = new PlatformService();
const availablePlatforms = ref([] as Array<Platform>);
const currentPlatformId = ref(null as string | null);

export default function usePlatform() {


  function updateAvailablePlatforms(newAvailablePlatforms: Array<Platform>): void {
    availablePlatforms.value = newAvailablePlatforms;
  }

  function setCurrentPlatformId(newPlatformId: string): void {
    currentPlatformId.value = newPlatformId;
  }


  async function loadAvailablePlatforms(): Promise<void> {
    await withLoading(
      "loading available platforms ",
      platformService.getAll().then(updateAvailablePlatforms)
    );
  }


  return {
    loadAvailablePlatforms,
    setCurrentPlatformId,
    availablePlatforms: computed(() => availablePlatforms.value),
    currentPlatform: computed(() =>
      availablePlatforms.value ? availablePlatforms.value.find(platform => platform.id == currentPlatformId.value) : null
    ),
  };
}
