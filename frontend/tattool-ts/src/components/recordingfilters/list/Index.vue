<template>
  <div class="recording-filters">
    <div class="title">Recording filters</div>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Description</th>
          <th>Tags</th>
          <th></th>
        </tr>
      </thead>
      <tbody class="stripe">
        <tr
          v-for="recordingFilter in recordingFilterList"
          :key="recordingFilter.id"
        >
          <td class="recordingfilter-name">
            {{ recordingFilter.name }}
            <copy-to-clipboard :text="recordingFilter.id"> </copy-to-clipboard>
          </td>
          <td>{{ recordingFilter.description }}</td>
          <td><taggifier :tags="recordingFilter.tags"></taggifier></td>
          <td class="tools">
            <action-icon
              :icon="'play'"
              @click="play(recordingFilter.id)"
            ></action-icon>
            <action-icon
              @click="editRecordingFilter(recordingFilter.id)"
              :icon="'edit'"
            ></action-icon>
            <action-icon
              @click="deleteRecordingFilter(recordingFilter.id)"
              :icon="'delete'"
            ></action-icon>
          </td>
        </tr>
      </tbody>
    </table>
    <div id="action-panel">
      <action-icon icon="plus" @click="addNewRecordingFilter()" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useRouter } from "vue-router";
import Taggifier from "@/components/common/Taggifier.vue";
import ActionIcon from "@/components/common/ActionIcon.vue";
import useRecordingFilter from "@/state/useRecordingFilter";
import CopyToClipboard from "@/components/common/CopyToClipboard.vue";

export default defineComponent({
  components: { Taggifier, ActionIcon, CopyToClipboard },
  setup() {
    const router = useRouter();
    const { loadRecordingFilters, recordingFilterList, deleteRecordingFilter } =
      useRecordingFilter();
    loadRecordingFilters();
    const addNewRecordingFilter = () => {
      router.push({ path: "/new-recordingfilter" });
    };
    const editRecordingFilter = (id: string) => {
      router.push({ path: `/edit-recordingfilter/${id}` });
    };
    const play = (id: string) => {
      router.push({ path: `/live-run/${id}/RECORDINGFILTER` });
    };

    return {
      recordingFilterList,
      addNewRecordingFilter,
      editRecordingFilter,
      deleteRecordingFilter,
      play,
    };
  },
});
</script>
<style lang="scss" scoped>
.recording-filters {
  @extend .page;
  display: flex;
  flex-direction: column;
  flex: 1;
  > table {
    @extend .item-list;
    width: 100%;
    margin-top: 2rem;
    .recordingfilter-name {
      display: flex;
      flex-direction: row;
      align-items: center;
    }

    .tools {
      display: flex;
      flex-direction: row;
      justify-content: flex-end;
      > div {
        margin-right: 1rem;
      }
    }
  }
}
</style>