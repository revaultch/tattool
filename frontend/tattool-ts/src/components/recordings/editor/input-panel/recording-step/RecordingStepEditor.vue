<template>
  <div class="recording-step-editor" :class="{ editing: !readOnly }">
    <select-template
      class="verb"
      v-if="!readOnly"
      :itemsProvider="() => availableCommandDescriptors"
      :itemRenderer="(item) => item.name"
      :itemFilter="
        (item, term) => item.name?.toLowerCase().includes(term?.toLowerCase())
      "
      :valueRenderer="(item) => item.name"
      :value="selectedCommandDescriptor"
      @change="handleCommandDescriptorChange($event)"
    />
    <div class="verb" v-else>{{ recordingStep.payload.verb }}</div>
    <!--only if commandDescriptor is not null-->
    <payload-editor
      v-if="selectedCommandDescriptor"
      :commandDescriptor="selectedCommandDescriptor"
      :readOnly="readOnly"
      :payload="recordingStep.payload"
      :contextData="contextData"
      :mappingData="{
        mappableColumns:
          dataset && dataset.content ? dataset.content.columns : [],
        mappings: recordingStep.mappings,
      }"
      @change="handlePayloadChange($event)"
      @mapping="handleMappingChange($event)"
    ></payload-editor>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, PropType, inject } from "vue";
import {
  CommandDescriptor,
  findCommandDescriptorByName,
} from "@/model/transport/CommandDescriptor";
import SelectTemplate from "@/components/recordings/editor/input-panel/recording-step/SelectTemplate.vue";
import PayloadEditor from "@/components/recordings/editor/input-panel/recording-step/PayloadEditor.vue";
import { RecordingStep } from "@/model/transport/RecordingStep";
import { ContextService } from "@/services/payload/ContextService";
import { Dataset } from "@/model/transport/Dataset";

export default defineComponent({
  components: { SelectTemplate, PayloadEditor },
  props: {
    commandDescriptors: {
      type: Array as PropType<Array<CommandDescriptor>>,
      required: true,
    },
    recordingStep: {
      type: Object as PropType<RecordingStep>,
      required: true,
    },
    dataset: {
      type: Object as PropType<Dataset>,
      required: false,
    },
    readOnly: {
      type: Boolean,
      required: false,
      default: false,
    },
  },
  emits: ["change"],
  setup(props, { emit }) {
    const selectedCommandDescriptor = ref<CommandDescriptor | null>(null);
    const contextData = ref<any | null>(null);
    const contextService = inject<ContextService>("contextService")!;

    const availableCommandDescriptors = ref([] as Array<CommandDescriptor>);

    contextService
      .getAvailableCommandDescriptorsInContext()
      .then((descriptors) => {
        availableCommandDescriptors.value = descriptors;
      });

    const selectCommandDescriptor = (
      commandDescriptor: CommandDescriptor | null
    ) => {
      selectedCommandDescriptor.value = commandDescriptor;
    };

    const handleCommandDescriptorChange = (
      commandDescriptor: CommandDescriptor
    ) => {
      if (commandDescriptor != null) {
        // get context for command & set command descriptor for payload editor
        contextService.getContext(commandDescriptor.name).then((result) => {
          contextData.value = result;
          selectCommandDescriptor(commandDescriptor);
          handlePayloadChange({});
        });
      } else {
        selectCommandDescriptor(null);
      }
    };

    const handlePayloadChange = (newPayload: any) => {
      // https://github.com/webpack/webpack/pull/11221 (optional not chaining w/webpack 4.0) TODO upgrade
      //      newPayload.verb = selectedCommandDescriptor.value?.name;
      newPayload.verb = selectedCommandDescriptor.value
        ? selectedCommandDescriptor.value.name
        : null;
      emit("change", {
        ...props.recordingStep,
        ...{ payload: newPayload },
      } as RecordingStep);
    };

    const handleMappingChange = (newMapping: any) => {
      const mapping = { [newMapping.source]: newMapping.target };
      emit("change", {
        ...props.recordingStep,
        ...{ mappings: { ...mapping } },
      });
    };

    if (props.readOnly) {
      selectCommandDescriptor(
        findCommandDescriptorByName(
          props.recordingStep.payload.verb,
          props.commandDescriptors
        )
      );
    }

    return {
      handleCommandDescriptorChange,
      selectedCommandDescriptor,
      availableCommandDescriptors,
      contextData,
      handlePayloadChange,
      handleMappingChange,
      findCommandDescriptorByName,
    };
  },
});
</script>

<style lang="scss" >
.recording-step-editor {
  display: flex;
  flex-direction: row;
  flex-flow: wrap;
  &.editing {
    flex-direction: column !important;
    padding: 1rem;

    > .verb {
      padding: 1rem;
    }
    > .payload-editor {
      border: 1px solid black;
      padding: 1rem;
    }
  }

  .verb {
    min-width: 10rem;
    text-align: left;
    padding-left: 1rem;
    margin-top: auto;
    margin-bottom: auto;
    font-weight: 600;
  }
  .payload-editor {
    display: flex;
    flex-direction: column;
    align-content: stretch;
  }
  width: 100%;
}
</style>
