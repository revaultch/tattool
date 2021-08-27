import { Status } from "./PayloadExecutionEvent";

interface RecordingExecutionReport {
    readonly recordingId: string;
    readonly recordingDisplayName: string;
    readonly payload: any;
    readonly status: Status;
}


interface RecordingFilterExecutionReport {
    readonly recordingFilterId: string;
    readonly recordingFilterDisplayName: string;
    readonly recordingExecutionReportList: Array<RecordingExecutionReport>;
}

export { RecordingFilterExecutionReport, RecordingExecutionReport }