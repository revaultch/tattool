<template>
  <div class="recordings">
    <div class="title">Recordings</div>
    <div class="activate-debug">
      <input type="checkbox" id="debug" v-model="activateDebug" />
      <label for="debug">Activate debug when running</label>
    </div>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Description</th>
          <th>Target</th>
          <th>Tags</th>
          <th></th>
        </tr>
      </thead>
      <tbody class="stripe">
        <tr v-for="recording in recordingList" :key="recording.id">
          <td class="recording-name">
            {{ recording.name }}
            <copy-to-clipboard :text="recording.id"></copy-to-clipboard>
          </td>
          <td>{{ recording.description }}</td>
          <td>{{ recording.target }}</td>
          <td><taggifier :tags="recording.tags"></taggifier></td>
          <td class="tools">
            <action-icon
              @click="play(recording.id)"
              :icon="'play'"
            ></action-icon>
            <action-icon
              :icon="'edit'"
              @click="edit(recording.id)"
            ></action-icon>
            <action-icon
              :icon="'delete'"
              @click="deleteRecording(recording.id)"
            ></action-icon>
          </td>
        </tr>
      </tbody>
    </table>
    <div id="action-panel">
      <action-icon icon="plus" @click="addNewRecording()" />
    </div>
  </div>
</template>

<script>
import { useRouter } from "vue-router";
import Taggifier from "@/components/common/Taggifier.vue";
import ActionIcon from "@/components/common/ActionIcon.vue";
import useRecording from "@/state/useRecording";
import CopyToClipboard from "@/components/common/CopyToClipboard.vue";
import { ref } from "vue";

export default {
  components: { Taggifier, ActionIcon, CopyToClipboard },
  setup() {
    const router = useRouter();
    const { loadRecordings, recordingList, deleteRecording } = useRecording();

    const activateDebug = ref(false);
    loadRecordings();

    const addNewRecording = () => {
      router.push({ path: "/new-recording" });
    };

    const play = (id) => {
      router.push({
        path: `/live-run/${id}/RECORDING`,
        query: { debug: activateDebug.value },
      });
    };

    const edit = (id) => {
      router.push({ path: `/edit-recording/${id}` });
    };

    return {
      recordingList,
      addNewRecording,
      play,
      edit,
      deleteRecording,
      activateDebug,
    };
  },
};
</script>

<style lang="scss" scoped>
.recordings {
  @extend .page;
  display: flex;
  flex-direction: column;
  flex: 1;
  > .activate-debug {
    > input {
      width: 2rem;
    }
    align-self: flex-end;
  }
  > table {
    @extend .item-list;
    width: 100%;
    margin-top: 2rem;
    .recording-name {
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