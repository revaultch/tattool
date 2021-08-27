<template>
  <div id="recording-filter-input-panel">
    <div class="title">Recording filter</div>
    <div id="form">
      <div id="name" class="field" :class="{ error: !isNameValid }">
        <input
          type="text"
          placeholder="Name"
          v-model="dirtyRecordingFilter.name"
        />
      </div>
      <div
        id="description"
        class="field"
        :class="{ error: !isDescriptionValid }"
      >
        <textarea
          rows="6"
          placeholder="Description"
          v-model="dirtyRecordingFilter.description"
        ></textarea>
      </div>
      <div id="tags" class="field tags">
        <taggifier
          :tags="dirtyRecordingFilter.tags"
          @delete="removeTag($event)"
        ></taggifier>
        <input
          type="text"
          placeholder="Add tags (comma separated and hit enter)"
          v-model="newTags"
          @keypress.enter="addNewTags()"
        />
      </div>
      <div id="recordings" class="field recordings">
        <div>
          The following recordings will be executed when running this test set
        </div>
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Description</th>
              <th>Target</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="recording in recordingsMatchingTags" :key="recording.id">
              <td>{{ recording.name }}</td>
              <td>{{ recording.description }}</td>
              <td>{{ recording.target }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div id="commit-panel">
        <button :disabled="!isFormValid" class="yes" @click="save()">
          Save
        </button>
        <button class="no" @click="goBackToRecordingFilters()">Cancel</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, ref, defineComponent } from "vue";
import Taggifier from "@/components/common/Taggifier.vue";
import { useRouter } from "vue-router";
import useRecordingFilter from "@/state/useRecordingFilter";
import RecordingService from "@/services/RecordingService";
import { RecordingFilter } from "@/model/transport/RecordingFilter";
import { Recording } from "@/model/transport/Recording";

export default defineComponent({
  props: ["id"],
  components: {
    Taggifier,
  },
  setup(props) {
    const router = useRouter();
    const { loadRecordingFilter, recordingFilter, saveRecordingFilter } =
      useRecordingFilter();
    const dirtyRecordingFilter = ref({
      tags: [],
    } as RecordingFilter);
    const newTags = ref("");
    const recordingsMatchingTags = ref([] as Array<Recording>);

    // init
    if (props.id != null) {
      loadRecordingFilter(props.id).then(() => {
        dirtyRecordingFilter.value = recordingFilter.value;
        updateRecordingsMatchingTags(
          dirtyRecordingFilter.value.tags as Array<string>
        );
      });
    }

    // projection of recordings matching tags
    const updateRecordingsMatchingTags = async (tags: Array<string>) => {
      recordingsMatchingTags.value =
        await new RecordingService().getRecordingsWithTags(tags);
    };

    // tag management
    const addNewTags = () => {
      dirtyRecordingFilter.value.tags.push(...newTags.value.split(","));
      newTags.value = "";
      updateRecordingsMatchingTags(dirtyRecordingFilter.value.tags);
    };

    const removeTag = (tag: string) => {
      const index = dirtyRecordingFilter.value.tags.indexOf(tag);
      if (index > -1) {
        dirtyRecordingFilter.value.tags.splice(index, 1);
      }
      updateRecordingsMatchingTags(dirtyRecordingFilter.value.tags);
    };

    // recording filter form validation / save / navigation
    const save = () => {
      saveRecordingFilter(dirtyRecordingFilter.value).then(() =>
        goBackToRecordingFilters()
      );
    };

    const goBackToRecordingFilters = () => {
      router.push({ path: "/recordingfilters" });
    };

    const isNameValid = computed(
      () =>
        dirtyRecordingFilter.value.name &&
        dirtyRecordingFilter.value.name.trim() !== ""
    );
    const isDescriptionValid = computed(
      () =>
        dirtyRecordingFilter.value.description &&
        dirtyRecordingFilter.value.description.trim() !== ""
    );
    const isFormValid = computed(
      () => isNameValid.value && isDescriptionValid.value
    );
    return {
      save,
      addNewTags,
      removeTag,
      goBackToRecordingFilters,
      newTags,
      isNameValid,
      isDescriptionValid,
      isFormValid,
      dirtyRecordingFilter,
      recordingsMatchingTags,
    };
  },
});
</script>
<style lang="scss" scoped>
#recording-filter-input-panel {
  @extend .page;
  display: flex;
  flex-direction: column;
  flex: 1;
  width: auto;
  overflow-y: scroll;
  height: 100%;
  padding-right: 10px !important;
  margin-right: 5px !important;

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
  }

  #recordings {
    > div {
      padding-top: 1rem;
      padding-bottom: 1rem;
      font-weight: 600;
    }
    > table {
      @extend .item-list;
    }
  }

  #action-panel {
    display: flex;
    flex-direction: row;
    align-content: center;
    justify-content: center;
    padding: 2rem;
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
</style>