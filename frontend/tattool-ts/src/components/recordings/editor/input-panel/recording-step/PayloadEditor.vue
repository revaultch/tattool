<template>
  <div class="payload-editor">
    <div
      class="field"
      v-for="fieldDescriptor in commandDescriptor.payloadDescriptor"
      :key="fieldDescriptor.name"
    >
      <!-- TODO Upgrade webpack to benefit from optional chaining -->
      <div
        class="prepend"
        v-if="
          fieldDescriptor &&
          fieldDescriptor.annotations &&
          fieldDescriptor.annotations.Prepend
        "
        v-html="fieldDescriptor.annotations.Prepend.value"
      ></div>
      <Suspense>
        <component
          class="inline"
          :class="{
            mapped:
              mappingData.mappings &&
              mappingData.mappings[`${fieldDescriptor.name}`],
          }"
          :is="fieldComponent(fieldDescriptor.name)"
          :value="payload[`${fieldDescriptor.name}`]"
          :readOnly="readOnly"
          :contextData="contextData"
          :field="fieldDescriptor.name"
          @change="handlePropertyChange($event)"
          @keypress.tab="handleTab($event)"
          @keypress.backspace="handleBackspace($event)"
        />
      </Suspense>
      <div
        class="inline mapping-editor-holder"
        v-if="isMappable(fieldDescriptor)"
      >
        <mapping-editor
          :mappableColumns="mappingData.mappableColumns"
          :readOnly="readOnly"
          :value="
            mappingData.mappings
              ? mappingData.mappings[`${fieldDescriptor.name}`]
              : null
          "
          @change="handleMappingChange($event, fieldDescriptor)"
        ></mapping-editor>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, defineAsyncComponent } from "vue";
import {
  CommandDescriptor,
  FieldDescriptor,
  findFieldDescriptorByName,
} from "@/model/transport/CommandDescriptor";
import { PayloadChangeEvent } from "@/model/common/Payload";
import { copyObject } from "@/utils/ObjectUtil";
import MappingEditor from "./MappingEditor.vue";

export default defineComponent({
  props: {
    // command metadata : describes a command (name, payload structure)
    commandDescriptor: {
      type: Object as PropType<CommandDescriptor>,
      required: true,
    },
    // actual payload to be edited - can be an empty object
    payload: {
      type: Object as PropType<any>,
      required: true,
    },
    //
    mappingData: {
      type: Object as PropType<any>,
      required: true,
    },
    contextData: {
      type: Object as PropType<any>,
      required: false,
    },
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  components: { MappingEditor },
  emits: ["change", "mapping"],
  setup(props, { emit }) {
    const capitalize = (s: string) => {
      if (typeof s !== "string") return "";
      return s.charAt(0).toUpperCase() + s.slice(1);
    };
    const payloadCopy = copyObject(props.payload);
    const fieldComponent = (fieldName: string) => {
      let fieldRendererName = "default";
      const fieldDescriptor: FieldDescriptor = findFieldDescriptorByName(
        fieldName,
        props.commandDescriptor.payloadDescriptor
      );
      if (fieldDescriptor.annotations.Renderer) {
        fieldRendererName = fieldDescriptor.annotations.Renderer.value;
      }
      const capitalizedFieldRendererName = capitalize(fieldRendererName);
      return defineAsyncComponent(
        () =>
          import(
            `@/components/recordings/editor/input-panel/recording-step/field-component/${capitalizedFieldRendererName}`
          )
      );
    };

    const isMappable = (fieldDescriptor: FieldDescriptor): boolean => {
      return (
        fieldDescriptor !== null &&
        fieldDescriptor.annotations !== null &&
        fieldDescriptor.annotations["Mappable"] != null &&
        (props.mappingData as any).mappableColumns &&
        (props.mappingData as any).mappableColumns.length > 0 &&
        props.readOnly
      );
    };

    const handlePropertyChange = (payloadChangeEvent: PayloadChangeEvent) => {
      payloadCopy[payloadChangeEvent.field] = payloadChangeEvent.payloadItem;
      emit("change", payloadCopy);
    };

    const handleMappingChange = (
      targetColumnName: string,
      fieldDescriptor: FieldDescriptor
    ) => {
      emit("mapping", {
        source: fieldDescriptor.name,
        target: targetColumnName,
      });
    };

    const handleTab = (evt: Event) => {
      throw Error("not iimplemented");
    };
    const handleBackspace = (evt: Event) => {
      throw Error("not iimplemented");
    };

    return {
      fieldComponent,
      handlePropertyChange,
      handleMappingChange,
      handleTab,
      handleBackspace,
      isMappable,
    };
  },
});
</script>

<style lang="scss">
.payload-editor {
  .field {
    padding-top: 1rem;
    padding-bottom: 1rem;
    > .prepend {
      padding-top: 1rem;
      padding-bottom: 1rem;
    }
    > .mapped {
      > * {
        text-decoration: line-through !important;
      }
    }
    > .inline {
      display: inline-block;
    }
  }
}
.element-super-locator-selector {
  border: 1px dashed #cccccc;
  padding: 0.5rem;
  width: 100%;
}

.text-editor {
  width: 100%;
  > * {
    font-style: italic;
    text-decoration: underline;
  }
}
</style>