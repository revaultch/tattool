

import { ref, computed } from "vue";
import { RunService, ServerEventHandler } from "@/services/RunService";
import { Type } from "@/model/transport/execution/Run";


const runService = new RunService();

export default function useRun() {

  async function execute(reference: string, type: Type, debug: boolean, serverEventHandler: ServerEventHandler): Promise<void> {
    runService.executeReference(reference, type, debug, serverEventHandler);
  }

  return { execute };
}
