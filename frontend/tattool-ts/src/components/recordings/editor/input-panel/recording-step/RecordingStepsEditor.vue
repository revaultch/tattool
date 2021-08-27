<template>
  <div class="recording-steps-editor">
    <div
      class="recording-step-holder"
      v-for="recordingStep in recording.recordingStepList"
      :key="recordingStep.id"
    >
      <recording-step-editor
        :commandDescriptors="commandDescriptors"
        :recordingStep="recordingStep"
        :dataset="recording.dataset"
        :readOnly="true"
        :contextService="contextService"
        :validationService="validationService"
        @change="handleUpdateRecordingStep($event)"
      ></recording-step-editor>
      <div class="command-panel">
        <action-icon
          class="delete"
          @click="handleDelete(recordingStep.id)"
          v-if="deletingState.state != true"
          icon="delete"
        ></action-icon>

        <action-icon
          class="play"
          @click="handlePlayUntil(recordingStep.id)"
          v-if="deletingState.state != true"
          icon="play"
        ></action-icon>
      </div>
      <div
        class="popup in-container pre-deletion-warning"
        v-if="
          deletingState.state == true && deletingState.id == recordingStep.id
        "
      >
        <div>Are you sure you want to delete this step ?</div>
        <button class="accept yes" @click="confirmDelete(recordingStep.id)">
          Accept
        </button>
        <button class="cancel no" @click="cancelDelete()">Cancel</button>
      </div>
    </div>

    <div
      v-if="newRecordingStep"
      id="new-recording-step-placeholder"
      :class="{ 'validation-error': newRecordingValidationError }"
    >
      <recording-step-editor
        :commandDescriptors="commandDescriptors"
        :recordingStep="newRecordingStep"
        :contextService="contextService"
        :validationService="validationService"
        @change="handleNewRecordingStepChange($event)"
      ></recording-step-editor>
      <div class="command-panel-new">
        <action-icon
          class="play"
          icon="play"
          @click="handlePlayNew()"
          v-if="newRecordingStep.payload.verb"
        />
        <action-icon
          class="abort"
          icon="delete"
          @click="handleAbortNew()"
          v-if="newRecordingStep.payload.verb"
        />
      </div>
    </div>
    <action-icon
      id="new-recording-step-button"
      @click="initializeNewRecordingStep"
      v-if="!newRecordingStep"
      class="new"
      icon="plus"
    ></action-icon>
  </div>
</template>

<script lang="ts">
import { CommandDescriptor } from "@/model/transport/CommandDescriptor";
import { RecordingStep } from "@/model/transport/RecordingStep";
import { defineComponent, ref, PropType } from "vue";
import RecordingStepEditor from "./RecordingStepEditor.vue";
import {
  ValidationService,
  ValidationResult,
} from "@/services/payload/ValidationService";
import { ContextService } from "@/services/payload/ContextService";
import { generateUUID } from "@/utils/UUIDUtil";
import { Recording } from "@/model/transport/Recording";
import ActionIcon from "@/components/common/ActionIcon.vue";
import useUIState from "@/state/useUIState";

export default defineComponent({
  components: { RecordingStepEditor, ActionIcon },
  props: {
    commandDescriptors: {
      type: Array as PropType<Array<CommandDescriptor>>,
      required: true,
    },
    recording: {
      type: Object as PropType<Recording>,
      required: true,
    },
    validationService: {
      type: Object as PropType<ValidationService>,
      required: true,
    },
    contextService: {
      type: Object as PropType<ContextService>,
      required: true,
    },
  },
  emits: ["change", "validation-failure", "validation-success"],
  setup(props, { emit }) {
    interface DeleteState {
      state: boolean;
      id: string | null;
    }
    const firstValidationCall = ref(true);
    const newRecordingStep = ref<RecordingStep | null>(null);
    const newRecordingValidationError = ref(false);
    const deletingState = ref({ state: false, id: null } as DeleteState);
    const { notifyFailure, notifySuccess } = useUIState();
    const mergeWithRecording = (recordingStepList: Array<RecordingStep>) => ({
      ...props.recording,
      ...{
        recordingStepList: recordingStepList,
      },
    });

    const handlePlayNew = () => {
      props.validationService
        .validate([newRecordingStep.value as RecordingStep])
        .then((validationResult: ValidationResult) => {
          if (validationResult.success) {
            emit(
              "change",
              mergeWithRecording(
                props.recording.recordingStepList!.concat(
                  validationResult.validatedList
                )
              )
            );
            newRecordingStep.value = null;
            newRecordingValidationError.value = false;
            notifySuccess("New recording step list validated");
            emit("validation-success", validationResult);
          } else {
            newRecordingValidationError.value = true;
            notifyFailure("Error while validating list - please refresh");
            emit("validation-failure", validationResult);
          }
        });
    };

    const handleAbortNew = () => {
      newRecordingStep.value = null;
      newRecordingValidationError.value = false;
    };

    const handlePlayUntil = async (id: string) => {
      let keep = true;
      const listToValidate: Array<RecordingStep> =
        props.recording.recordingStepList!.filter(
          (recordingStep: RecordingStep) => {
            if (recordingStep.id == id) {
              keep = false;
              return true;
            } else {
              return keep;
            }
          }
        );
      await props.validationService
        .validate(listToValidate)
        .then((validationResult: ValidationResult) => {
          if (validationResult.success) {
            emit("change", mergeWithRecording(validationResult.validatedList));
            notifySuccess("New recording step list validated");
            firstValidationCall.value = false;
          } else {
            notifyFailure("Error while validating list - please refresh");
            emit(
              "validation-failure",
              validationResult.payloadInstanceExecutionReportList
            );
            firstValidationCall.value = false;
          }
        });
    };

    const initializeNewRecordingStep = async () => {
      if (
        firstValidationCall.value &&
        props.recording.recordingStepList &&
        props.recording.recordingStepList.length > 0
      ) {
        await handlePlayUntil(
          props.recording.recordingStepList[
            props.recording.recordingStepList.length - 1
          ].id
        ).then(() => {
          newRecordingStep.value = {
            id: generateUUID(),
            payload: {},
          } as RecordingStep;
          firstValidationCall.value = false;
        });
      } else {
        newRecordingStep.value = {
          id: generateUUID(),
          payload: {},
        } as RecordingStep;
      }
    };

    // used for mapping modifications
    const handleUpdateRecordingStep = (updatedRecordingStep: RecordingStep) => {
      let newSteps = props.recording.recordingStepList!;
      let stepToUpdateIndex = newSteps.findIndex(
        (item) => item.id == updatedRecordingStep.id
      );
      newSteps[stepToUpdateIndex] = updatedRecordingStep;
      emit("change", mergeWithRecording(newSteps));
    };

    const handleNewRecordingStepChange = (
      changedRecordingStep: RecordingStep
    ) => {
      newRecordingStep.value = changedRecordingStep;
    };

    /** handle deletion of a given recording step */

    const handleDelete = (id: any) => {
      deletingState.value.state = true;
      deletingState.value.id = id;
    };

    const confirmDelete = async (id: string) => {
      const listToValidate: Array<RecordingStep> =
        props.recording.recordingStepList!.filter(
          (recordingStep: RecordingStep) => recordingStep.id !== id
        );
      await props.validationService
        .validate(listToValidate)
        .then((validationResult: ValidationResult) => {
          if (validationResult.success) {
            emit("change", mergeWithRecording(validationResult.validatedList));
            clearDeleteState();
            notifySuccess("New recording step list validated");
            emit("validation-success", validationResult);
            firstValidationCall.value = false;
          } else {
            clearDeleteState();
            notifyFailure(
              "Error while deleting recording step - submitted sequence could be invalid - please refresh"
            );
            emit("validation-failure", validationResult);
            firstValidationCall.value = false;
          }
        });
    };

    const cancelDelete = () => {
      clearDeleteState();
    };

    const clearDeleteState = () => {
      deletingState.value = { state: false, id: null };
    };

    return {
      newRecordingStep,
      newRecordingValidationError,
      handlePlayNew,
      handleAbortNew,
      handlePlayUntil,
      initializeNewRecordingStep,
      handleNewRecordingStepChange,
      handleUpdateRecordingStep,
      deletingState,
      handleDelete,
      confirmDelete,
      cancelDelete,
    };
  },
});
</script>

<style lang="scss" scoped >
.recording-steps-editor {
  border: 1px double #eeeeee;
  display: flex;
  flex-direction: column;
  > .recording-step-holder:nth-child(2n) {
    background-color: #f8f8f8 !important;
  }

  > .recording-step-holder {
    position: relative;
    display: flex;
    flex-direction: row;
    padding-top: 1rem;
    padding-bottom: 1rem;

    > .command-panel {
      display: flex;
      flex-direction: row;
      justify-items: center;
      align-self: center;
      align-items: stretch;
      > .action-icon {
        flex: 1;
        margin: 0.5rem;
      }
    }
  }

  #new-recording-step-placeholder {
    display: flex;
    flex-direction: row;
    &.validation-error {
      > .recording-step-editor {
        border: 2px solid red !important;
        background: #eeeeee !important;
        > .payload-editor {
          border: 2px solid red !important;
        }
      }
    }
    > .command-panel-new {
      display: flex;
      flex-direction: column;
      justify-content: center;
      padding: 2rem;

      > .action-icon {
        margin-top: 1rem;
        margin-bottom: 1rem;
      }
    }
  }

  #new-recording-step-button {
    margin: auto;
    margin-top: 4rem;
    margin-bottom: 2rem;
  }
}
</style>