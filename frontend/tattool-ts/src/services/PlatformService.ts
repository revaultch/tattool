import ImageData from "@/model/transport/ImageData";
import SessionData from "@/model/transport/SessionData";
import Platform from "@/model/transport/Platform";
import AbstractDataService from "./AbstractDataService";
import useAxios from "./useAxios";

const { axiosInstance } = useAxios();


class TargetService extends AbstractDataService<Platform> {

  constructor() {
    super("platform");
  }

  saveOne(one: Platform): Promise<any> {
    throw Error("invalid method invocation / not available");
  }

  deleteOne(id: string): Promise<void> {
    throw Error("invalid method invocation / not available");
  }


  public async createSession(platformId: string): Promise<SessionData> {
    return axiosInstance.post(`/api/v1/platforms/${platformId}/sessions`)
      .then((response) => response.data, (err) => { throw Error(err) });
  }

  public async destroySession(platformId: string, sessionId: string): Promise<SessionData> {
    return axiosInstance.delete(`/api/v1/platforms/${platformId}/sessions/${sessionId}`)
      .then((response) => response.data, (err) => { throw Error(err) });
  }


  public async takeScreenshot(platformId: string, sessionId: string, tagId: string): Promise<ImageData> {
    return axiosInstance.get(
      `/api/v1/platforms/${platformId}/sessions/${sessionId}/screenshot${tagId ? "?tag=" + tagId : ""
      }`
    ).then((response) => response.data, (err) => { throw Error(err) });
  }


}






export default TargetService;