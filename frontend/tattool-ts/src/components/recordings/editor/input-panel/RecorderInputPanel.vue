<template>
  <div id="recorder-input-panel">
    <div class="title">Recording</div>
    <div id="form" v-if="atLeastOnePlatformAvailable">
      <div id="name" class="field" :class="{ error: !isNameValid }">
        <input type="text" placeholder="Name" v-model="dirtyRecording.name" />
      </div>
      <div
        id="description"
        class="field"
        :class="{ error: !isDescriptionValid }"
      >
        <textarea
          rows="6"
          placeholder="Description"
          v-model="dirtyRecording.description"
        ></textarea>
      </div>

      <div id="dataset" class="field">
        <select
          @change="handleDatasetChanged($event)"
          v-model="dirtyRecording.dataset"
        >
          <option :value="null">-- no dataset selected --</option>

          <option
            v-for="dataset in datasetList"
            :key="dataset.id"
            :value="dataset"
          >
            {{ dataset.name }}
          </option>
        </select>
      </div>

      <div id="Platform" class="field" v-if="mode === modes.NEW">
        <select
          v-if="moreThanOnePlatformAvailable"
          v-model="dirtyRecording.Platform"
          @change="createSession($event.Platform.value)"
        >
          <option selected="selected">select Platform</option>
          <option v-for="Platform in availablePlatforms" :key="Platform.id">
            {{ Platform.id }}
          </option>
        </select>
        <div v-else>
          <div v-if="exactlyOnePlatformAvailable">
            {{ dirtyRecording.Platform }}
          </div>
        </div>
      </div>
      <div id="Platform" class="field tags">
        <taggifier
          :tags="dirtyRecording.tags"
          @delete="removeTag($event)"
        ></taggifier>
        <input
          type="text"
          placeholder="Add tags (comma separated and hit enter)"
          v-model="newTags"
          @keypress.enter="addNewTags()"
        />
      </div>
      <div id="command-list" v-if="sessionId != null">
        <recording-steps-editor
          v-if="sessionId"
          :commandDescriptors="currentPlatform.availableCommands"
          :recording="dirtyRecording"
          @change="handleRecordingStepsChanged($event)"
          @validation-failure="handleValidationFailure($event)"
          @validation-success="handleValidationSuccess($event)"
        ></recording-steps-editor>
      </div>

      <div id="commit-panel">
        <button
          :disabled="!isFormValid"
          class="yes"
          @click="invokeSaveCurrentRecording()"
        >
          Save
        </button>
        <button class="no" @click="goBackToRecordings()">Cancel</button>
      </div>
    </div>
    <div v-else>No platform available. Unable to proceed</div>
  </div>
</template>

<script lang="ts">
import { computed, ref, defineComponent } from "vue";
import Taggifier from "@/components/common/Taggifier.vue";
import { onBeforeRouteLeave, useRouter } from "vue-router";
import useRecording from "@/state/useRecording";
import useSession from "@/state/useSession";
import usePlatform from "@/state/usePlatform";
import { Recording } from "@/model/transport/Recording";
import RecordingStepsEditor from "@/components/recordings/editor/input-panel/recording-step/RecordingStepsEditor.vue";

import useUIState from "@/state/useUIState";
import useDataset from "@/state/useDataset";
import { ValidationResult } from "@/services/payload/ValidationService";
export default defineComponent({
  props: ["id"],
  components: {
    Taggifier,
    RecordingStepsEditor,
  },
  setup(props) {
    const modes = {
      EDIT: "edit",
      NEW: "new",
    };

    const router = useRouter();

    const { loadAvailablePlatforms, availablePlatforms, currentPlatform } =
      usePlatform();

    const { sessionId, takeScreenshot, createSession, destroySession } =
      useSession();

    const { loadDatasets, datasetList } = useDataset();

    const { loadRecording, recording, updateCurrentRecording, saveRecording } =
      useRecording();

    const { notifySuccess } = useUIState();

    const newTags = ref("");
    const mode = ref(modes.NEW);

    onBeforeRouteLeave((to: any, from: any) => {
      if (
        currentPlatform &&
        currentPlatform.value &&
        sessionId &&
        sessionId.value
      ) {
        destroySession(currentPlatform.value.id, sessionId.value).then(() =>
          notifySuccess("invoked session cleanup")
        );
      }
    });

    const dirtyRecording = ref({
      recordingStepList: [],
      tags: [],
    } as Recording);

    if (props.id != null) {
      loadRecording(props.id).then(
        () => (dirtyRecording.value = recording.value)
      );
      mode.value = modes.EDIT;
    }

    const atLeastOnePlatformAvailable = computed(
      () => availablePlatforms?.value?.length > 0
    );
    const moreThanOnePlatformAvailable = computed(
      () => availablePlatforms?.value?.length > 1
    );
    const exactlyOnePlatformAvailable = computed(
      () => availablePlatforms?.value?.length == 1
    );

    loadAvailablePlatforms()
      .then(() => {
        if (exactlyOnePlatformAvailable.value == true) {
          dirtyRecording.value = {
            ...dirtyRecording.value,
            ...{ platform: availablePlatforms.value[0].id },
          };
          createSession(dirtyRecording.value.platform!);
        }
      })
      .then(() => loadDatasets());

    const goBackToRecordings = () => {
      router.push({ path: "/recordings" });
    };

    const invokeSaveCurrentRecording = async () => {
      updateCurrentRecording(dirtyRecording.value);
      await saveRecording();
      goBackToRecordings();
    };

    // tag management
    const addNewTags = () => {
      dirtyRecording.value.tags.push(...newTags.value.split(","));
      newTags.value = "";
    };

    const removeTag = (tag: string) => {
      const index = dirtyRecording.value.tags.indexOf(tag);
      if (index > -1) {
        dirtyRecording.value.tags.splice(index, 1);
      }
    };

    // form validation
    const isNameValid = computed(
      () => dirtyRecording.value.name && dirtyRecording.value.name.trim() !== ""
    );

    const isDescriptionValid = computed(
      () =>
        dirtyRecording.value.description &&
        dirtyRecording.value.description.trim() !== ""
    );

    const isFormValid = computed(
      () => isNameValid.value && isDescriptionValid.value
    );

    const handleRecordingStepsChanged = (changedRecording: Recording) => {
      dirtyRecording.value = changedRecording;
      takeScreenshot(currentPlatform!.value!.id!, sessionId.value!, null);
    };

    const handleValidationFailure = (validationResult: ValidationResult) => {
      takeScreenshot(currentPlatform!.value!.id!, sessionId.value!, null);
    };

    const handleValidationSuccess = (validationResult: ValidationResult) => {
      takeScreenshot(currentPlatform!.value!.id!, sessionId.value!, null);
    };

    const handleDatasetChanged = (event: any) => {
      // remove mappings
      dirtyRecording.value = {
        ...dirtyRecording.value,
        ...{
          recordingStepList: dirtyRecording.value.recordingStepList?.map(
            (item) => ({
              ...item,
              ...{ mappings: {} },
            })
          ),
        },
      } as Recording;
    };

    return {
      dirtyRecording,
      sessionId,
      availablePlatforms,
      createSession,
      invokeSaveCurrentRecording,
      handleRecordingStepsChanged,
      datasetList,
      handleDatasetChanged,
      handleValidationFailure,
      handleValidationSuccess,
      addNewTags,
      removeTag,
      newTags,
      goBackToRecordings,
      isNameValid,
      isDescriptionValid,
      isFormValid,
      mode,
      modes,
      currentPlatform,
      atLeastOnePlatformAvailable,
      moreThanOnePlatformAvailable,
      exactlyOnePlatformAvailable,
    };
  },
});
</script>

<style lang="scss" >
#recorder-input-panel {
  @extend .page;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  flex: 1;
  width: auto;
  padding-right: 1rem !important;
  margin-right: 5px !important;
  overflow: auto;
  height: 100%;
  #form {
    width: 100%;
    max-width: 100%;
    .field {
      &.tags {
        input {
          margin-top: 1rem;
        }
      }
      &#name {
        width: 40%;
      }
      &#description {
        width: 90%;
      }
      &#Platform {
        width: 60%;
      }
    }
    #command-list {
      > table {
        @extend .item-list;
        width: 100%;
        > tbody {
          > tr {
            border: 1px solid;
            width: 100% !important;
            height: auto !important;
          }
          > tr:nth-child(2n) {
            background-color: #f5f6fa !important;
          }
        }
      }
      height: auto;
      padding-top: 4rem;
    }
    #action-panel {
      display: flex;
      flex-direction: row;
      align-content: center;
      justify-content: center;
      padding: 2rem;

      height: auto;
      > img {
        cursor: pointer;
        width: 5rem;
        height: 5rem;
      }
    }
    #commit-panel {
      margin-top: 4rem;
      margin-bottom: 4rem;
      display: flex;
      flex-direction: row;
      align-content: flex-end;
      justify-content: flex-end;
      width: 100%;
    }
  }
}
</style>