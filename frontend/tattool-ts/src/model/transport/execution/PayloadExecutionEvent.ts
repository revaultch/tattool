
enum Status {
    SUCCESS,
    FAILURE,
    RUNNING
}


interface PayloadExecutionEvent {

    readonly platformId: string;

    readonly recordingId: string;

    readonly recordingDisplayName: string;

    readonly recordingInstanceId: string;

    readonly id: string;

    readonly displayName: string;

    readonly payload: any;

    readonly status: Status;

    readonly durationInMs: number;

    readonly datasetRow: Array<string>;
}

interface RunTerminatedExecutionEvent {
    readonly runId: string;
}




export { PayloadExecutionEvent, RunTerminatedExecutionEvent, Status }