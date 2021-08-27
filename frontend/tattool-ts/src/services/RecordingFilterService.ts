import { RecordingFilter } from "@/model/transport/RecordingFilter";
import AbstractDataService from "./AbstractDataService";

class RecordingFilterService extends AbstractDataService<RecordingFilter> {
  constructor() {
    super("recordingFilter");
  }
}

export default RecordingFilterService;