import { PayloadInstanceExecutionReport } from "@/model/transport/execution/Run";
import { RecordingStep } from "@/model/transport/RecordingStep";

interface ValidationService {
    validate(recordingSteps: Array<RecordingStep>): Promise<ValidationResult>;
}

interface ValidationResult {

    readonly success: boolean;
    readonly payloadInstanceExecutionReportList: Array<PayloadInstanceExecutionReport>;
    readonly validatedList: Array<RecordingStep>;
}


export { ValidationService, ValidationResult };