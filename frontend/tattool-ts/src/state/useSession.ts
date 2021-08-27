import PlatformService from "@/services/PlatformService";

import useUIState from "@/state/useUIState";
import usePlatform from "@/state/usePlatform";

import { ref, computed } from "vue";

const platformService: PlatformService = new PlatformService();

const { loading, doneLoading } = useUIState();
const { setCurrentPlatformId } = usePlatform();

const isLoggedIn = ref(true);
const sessionId = ref(null as string | null);
const lastScreenshot = ref(null as string | null);

const { withLoading } = useUIState();


export default function useSession() {
  async function login(user: string, password: string) {
    console.log(user + ":" + password);
    throw Error("not implemented");
  }

  async function createSession(platformId: string) {
    await withLoading("creating session", platformService.createSession(platformId).then((sessionData) => {
      setCurrentPlatformId(platformId);
      sessionId.value = sessionData.id;
      takeScreenshot(platformId, sessionId.value, null);
    }));
  }


  async function destroySession(platformId: string, sId: string) {
    platformService.destroySession(platformId, sId);
    sessionId.value = null;
  }


  async function takeScreenshot(platformId: string, sessionId: string, tagId: string | null) {
    platformService.takeScreenshot(platformId, sessionId, tagId!)
      .then((screenshot) => lastScreenshot.value = screenshot.data);
  }

  return {
    login,
    createSession,
    destroySession,
    takeScreenshot,
    lastScreenshot: computed(() => lastScreenshot.value),
    isLoggedIn: computed(() => isLoggedIn.value),
    sessionId: computed(() => sessionId.value),
  };
}
