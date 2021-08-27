import { RecordingStep } from "@/model/transport/RecordingStep";
import { ValidationResult, ValidationService } from "@/services/payload/ValidationService";

class ValidationServiceMock implements ValidationService {

    async validate(recordingSteps: Array<RecordingStep>): Promise<ValidationResult> {
        return new Promise((resolve, reject) => {
            resolve({
                success: true, payloadInstanceExecutionReportList: [], validatedList: recordingSteps
            } as ValidationResult);

        });
    }

}


export { ValidationServiceMock }