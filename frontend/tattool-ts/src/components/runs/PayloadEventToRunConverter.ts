import { PayloadExecutionEvent, Status } from "@/model/transport/execution/PayloadExecutionEvent";
import { ExecutableRecordingInstanceExecutionReport, PayloadInstanceExecutionReport, Run, Type } from "@/model/transport/execution/Run";


const nullOrUndefined = (obj: any): boolean => obj === null || obj === undefined

const ensureNotNull = (obj: any): any => {
    if (obj != null) {
        return obj;
    } else {
        throw new Error("Unexpected null object");
    }
};




class PayloadEventToRunConverter {

    private run: Run = {
        id: "0000-0000-live-run-0000-0000",
        status: Status.RUNNING,
        executableRecordingInstanceExecutionReportList: []
    } as unknown as Run;

    constructor(reference: string, title: string, type: Type) {
        this.run = { ...this.run, ...{ reference: reference, title: title, type: type } };
    }


    private findRecordReportFor(payloadExecutionEvent: PayloadExecutionEvent): ExecutableRecordingInstanceExecutionReport {
        return this.run.executableRecordingInstanceExecutionReportList.find((item: ExecutableRecordingInstanceExecutionReport) => item.executableRecordingInstanceId === payloadExecutionEvent.recordingInstanceId)!;
    }

    private recordReportExists(payloadExecutionEvent: PayloadExecutionEvent): boolean {
        return !nullOrUndefined(this.findRecordReportFor(payloadExecutionEvent));
    }

    private findPayloadReportFor(payloadExecutionEvent: PayloadExecutionEvent): PayloadInstanceExecutionReport {
        return this.findRecordReportFor(payloadExecutionEvent).payloadInstanceExecutionReportList.find((item: PayloadInstanceExecutionReport) =>
            item.id === payloadExecutionEvent.id)!;
    }


    private payloadReportExists(payloadExecutionEvent: PayloadExecutionEvent): boolean {
        return !nullOrUndefined(this.findPayloadReportFor(payloadExecutionEvent));
    }

    private mapRecordReport(payloadExecutionEvent: PayloadExecutionEvent): ExecutableRecordingInstanceExecutionReport {
        return {
            id: payloadExecutionEvent.recordingInstanceId,
            platformId: payloadExecutionEvent.platformId,
            parentRecordingId: payloadExecutionEvent.recordingId,
            parentRecordingName: payloadExecutionEvent.recordingDisplayName,
            executableRecordingInstanceId: payloadExecutionEvent.recordingInstanceId,
            status: payloadExecutionEvent.status,
            datasetRow: payloadExecutionEvent.datasetRow,
            payloadInstanceExecutionReportList: []
        } as unknown as ExecutableRecordingInstanceExecutionReport;
    }


    private createRecordReport(payloadExecutionEvent: PayloadExecutionEvent): void {
        this.run = {
            ...this.run, ...{
                executableRecordingInstanceExecutionReportList: [...this.run.executableRecordingInstanceExecutionReportList,
                this.mapRecordReport(payloadExecutionEvent)]
            }
        } as unknown as Run;
    }

    private updateRecordReport(payloadExecutionEvent: PayloadExecutionEvent): void {
        let recordReport = ensureNotNull(this.findRecordReportFor(payloadExecutionEvent));
        recordReport = { ...recordReport, ...{ status: payloadExecutionEvent.status } };
        const newExecutableRecordingInstanceExecutionReportList = this.run.executableRecordingInstanceExecutionReportList.map((item: ExecutableRecordingInstanceExecutionReport) => item.executableRecordingInstanceId === payloadExecutionEvent.recordingInstanceId ? recordReport : item);
        this.run = {
            ...this.run, ...{ executableRecordingInstanceExecutionReportList: newExecutableRecordingInstanceExecutionReportList }
        }
    }

    private mapPayloadReport(payloadExecutionEvent: PayloadExecutionEvent): PayloadInstanceExecutionReport {
        return {
            id: payloadExecutionEvent.id,
            payload: payloadExecutionEvent.payload,
            status: payloadExecutionEvent.status,
            humanReadable: payloadExecutionEvent.displayName,
            message: payloadExecutionEvent.status.toString(),
            screenshotAfter: "",
            durationInMs: payloadExecutionEvent.durationInMs
        } as PayloadInstanceExecutionReport;
    }

    private createPayloadReport(payloadExecutionEvent: PayloadExecutionEvent): void {
        const recordReport = ensureNotNull(this.findRecordReportFor(payloadExecutionEvent));
        const newPayloadExecutionReport = this.mapPayloadReport(payloadExecutionEvent);
        const newPayloadInstanceExecutionReportList = [...recordReport.payloadInstanceExecutionReportList, newPayloadExecutionReport];
        const newRecordReport = { ...recordReport, ...{ payloadInstanceExecutionReportList: newPayloadInstanceExecutionReportList } as ExecutableRecordingInstanceExecutionReport };
        const newExecutableRecordingInstanceExecutionReportList = this.run.executableRecordingInstanceExecutionReportList.map((item: ExecutableRecordingInstanceExecutionReport) => item.id == newRecordReport.id ? newRecordReport : item);
        this.run = { ...this.run, ...{ executableRecordingInstanceExecutionReportList: newExecutableRecordingInstanceExecutionReportList } };
    }

    private updatePayloadReport(payloadExecutionEvent: PayloadExecutionEvent): void {
        const recordReport = ensureNotNull(this.findRecordReportFor(payloadExecutionEvent));
        const newPayloadExecutionReport = this.mapPayloadReport(payloadExecutionEvent);
        const newPayloadInstanceExecutionReportList = recordReport.payloadInstanceExecutionReportList.map((item: PayloadInstanceExecutionReport) => payloadExecutionEvent.id == item.id ? newPayloadExecutionReport : item);
        const newRecordReport = { ...recordReport, ...{ payloadInstanceExecutionReportList: newPayloadInstanceExecutionReportList } };
        const newExecutableRecordingInstanceExecutionReportList = this.run.executableRecordingInstanceExecutionReportList.map((item: ExecutableRecordingInstanceExecutionReport) => item.id == newRecordReport.id ? newRecordReport : item);
        this.run = { ...this.run, ...{ executableRecordingInstanceExecutionReportList: newExecutableRecordingInstanceExecutionReportList } };
    }

    public reduce(payloadExecutionEvent: PayloadExecutionEvent): Run {


        if (this.recordReportExists(payloadExecutionEvent)) {
            this.updateRecordReport(payloadExecutionEvent);
        } else {
            this.createRecordReport(payloadExecutionEvent);
        }
        if (this.payloadReportExists(payloadExecutionEvent)) {
            this.updatePayloadReport(payloadExecutionEvent);
        } else {
            this.createPayloadReport(payloadExecutionEvent);
        }

        return this.run;
    }

}

export default PayloadEventToRunConverter;


