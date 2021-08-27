import useAxios from "./useAxios";

const { axiosInstance } = useAxios();

import { Recording } from "@/model/transport/Recording";
import AbstractDataService from "./AbstractDataService";

class RecordingService extends AbstractDataService<Recording> {
  constructor() {
    super("recording");
  }

  public async getRecordingsWithTags(tags: Array<string>): Promise<Array<Recording>> {
    return axiosInstance.get(`/api/v1/recordings?tags=${tags}`)
      .then((response) => response.data, (err) => { throw Error(err) });
  }

}

export default RecordingService;
