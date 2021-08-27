import { Status } from "./PayloadExecutionEvent";

enum Type {
    RECORDING,
    RECORDINGFILTER
}

enum RunStatus {
    RUNNING,
    STOPPED,
    FINISHED,
    ERROR
}

interface PayloadInstanceExecutionReport {
    readonly id: string;

    readonly payload: any;

    readonly updatedPayload: any;

    readonly status: Status;

    readonly humanReadable: string;

    readonly message: string;

    readonly screenshotBefore: string;

    readonly screenshotAfter: string;

    readonly durationInMs: number;

    readonly log: Array<string>;


}

interface ExecutableRecordingInstanceExecutionReport {
    readonly id: string;
    readonly platformId: string;
    readonly parentRecordingId: string;
    readonly parentRecordingName: string;
    readonly executableRecordingInstanceId: string;
    readonly durationInMs: number;
    readonly status: Status;
    readonly payloadInstanceExecutionReportList: Array<PayloadInstanceExecutionReport>;
    readonly datasetRow: Array<string>;
}

interface Run {

    readonly id: string;
    readonly type: Type;
    readonly title: string;
    readonly reference: string;
    readonly status: RunStatus;
    readonly executableRecordingInstanceExecutionReportList: Array<ExecutableRecordingInstanceExecutionReport>;

}




export { Run, ExecutableRecordingInstanceExecutionReport, PayloadInstanceExecutionReport, Type, RunStatus }