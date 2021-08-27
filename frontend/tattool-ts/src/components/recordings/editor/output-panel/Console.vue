<template>
  <table>
    <thead>
      <tr>
        <th>status</th>
        <th>Execution log</th>
        <th>duration in ms</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="executionItem in executionLog.slice().reverse()"
        :key="executionItem"
      >
        <td width="10%">
          <status-icon :status="executionItem.status"></status-icon>
        </td>
        <td width="60%">{{ executionItem.message }}</td>
        <td width="10%">{{ executionItem.durationInMs }}</td>
        <td width="20%"><img :src="toImg(executionItem)" /></td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import StatusIcon from "@/components/common/StatusIcon.vue";
import useExecution from "@/state/useExecution";
export default {
  components: { StatusIcon },
  setup() {
    const { executionLog } = useExecution();
    const toImg = (executionItem) => {
      if (executionItem && executionItem.screenshotAfter) {
        return "data:image/png;base64," + executionItem.screenshotAfter;
      } else {
        return "";
      }
    };
    return { executionLog, toImg };
  },
};
</script>

<style lang="scss" scoped>
table {
  @extend .item-list;
  margin-top: 2rem;
  width: 100%;
  > tbody {
    > tr {
      height: 14rem;
      td {
        text-align: left;
        background-repeat: no-repeat;
        background-position: center;
        background-size: 3rem;
      }
      img {
        width: 128px;
      }
    }
  }
}
</style>