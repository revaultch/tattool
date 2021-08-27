import { Dataset } from "./Dataset";
import { RecordingStep } from "./RecordingStep";

interface Recording {
    readonly id?: string;
    readonly name?: string;
    readonly description?: string;
    readonly platform?: string;
    readonly dataset?: Dataset;
    readonly tags: Array<string>;
    readonly recordingStepList?: Array<RecordingStep>;
}


export { Recording }