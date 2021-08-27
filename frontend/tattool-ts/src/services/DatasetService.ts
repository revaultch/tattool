import { Dataset } from "@/model/transport/Dataset";
import AbstractDataService from "./AbstractDataService";

class DatasetService extends AbstractDataService<Dataset> {
  constructor() {
    super("dataset");
  }
}

export default DatasetService;