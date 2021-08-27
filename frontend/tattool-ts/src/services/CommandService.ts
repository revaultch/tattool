import { CommandDescriptor } from "@/model/transport/CommandDescriptor";
import { RecordingStep } from "@/model/transport/RecordingStep";
import { ValidationResult } from "./payload/ValidationService";
import useAxios from "./useAxios";

const { axiosInstance } = useAxios();

class CommandService {

  public async getAvailableCommandsInCurrentContext(platformId: string, sessionId: string): Promise<Array<CommandDescriptor>> {
    return axiosInstance.get(
      `/api/v1/platforms/${platformId}/sessions/${sessionId}/commands`
    ).then((response) => response.data, (err) => { throw Error(err) });
  }

  public async getCommandContext(platformId: string, sessionId: string, commandName: string): Promise<any> {
    return axiosInstance.get(
      `/api/v1/platforms/${platformId}/sessions/${sessionId}/context/${commandName}`
    ).then((response) => response.data, (err) => { throw Error(err) });

  }

  public async validateBatch(platformId: string, sessionId: string, recordingSteps: Array<RecordingStep>): Promise<ValidationResult> {
    return axiosInstance.post(
      `/api/v1/platforms/${platformId}/sessions/${sessionId}/batch`,
      recordingSteps
    ).then((response) => response.data, (err) => { throw Error(err) });
  }


}




export default CommandService;



