interface ServerEventHandler {
  handlePayloadExecutionEvent(payloadExecutionEvent: PayloadExecutionEvent): void;
  handleDone(runTerminatedExecutionEvent: RunTerminatedExecutionEvent): void;
  handleError(err: any): void;
}

import { SSE } from "sse.js";
import { PayloadExecutionEvent, RunTerminatedExecutionEvent } from "@/model/transport/execution/PayloadExecutionEvent";
import useAxios from "./useAxios";
import { Run, Type } from "@/model/transport/execution/Run";
import AbstractDataService from "./AbstractDataService";

const { axiosConfig } = useAxios();


class RunService extends AbstractDataService<Run> {
  constructor() {
    super("run");
  }

  public executeReference(reference: string, type: Type, debug: boolean, serverEventHandler: ServerEventHandler): void {
    //TODO localhost$
    const source = new SSE(`${axiosConfig.baseURL}/api/v1/recordings/execution`, {
      headers: { 'Content-Type': 'application/json' },
      payload: JSON.stringify({ id: reference, type: type, sessionConfiguration: { debug: debug } })
    });
    source.addEventListener('payload', function (event: any) {
      serverEventHandler.handlePayloadExecutionEvent(JSON.parse(event.data) as PayloadExecutionEvent);
    });

    source.addEventListener("error", function (e: any) {
      serverEventHandler.handleError(e);
    });

    source.addEventListener("done", function (event: any) {
      serverEventHandler.handleDone(JSON.parse(event.data) as RunTerminatedExecutionEvent);
    });

    source.stream();

  }


}




export { RunService, ServerEventHandler };
