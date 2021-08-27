<template >
  <div id="run-panel">
    <div
      class="run"
      v-if="availablePlatforms && availablePlatforms.length > 0 && run"
    >
      <div
        class="recording-group"
        v-for="key in Object.keys(groupedRecordingReports)"
        :key="key"
      >
        <div class="header" v-if="getRecording(key)">
          Recording
          <span>"{{ getRecording(key).name }}"</span>&nbsp;
          <span>({{ getRecording(key).id }})</span>
        </div>
        <div
          class="recording-instance"
          v-for="recordingReport in orderByInstanceId(
            groupedRecordingReports[key]
          )"
          :key="recordingReport.id"
        >
          <div class="header">
            <div class="left">
              <div v-if="recordingReport.id">
                instance id <span>{{ recordingReport.id }}</span>
              </div>
              <div v-if="recordingReport.datasetRow">
                with dataset row
                <span>
                  {{ recordingReport?.datasetRow }}
                </span>
              </div>
              <div v-if="recordingReport.durationInMs">
                run in
                <span>{{ recordingReport.durationInMs / 1000 }} seconds</span>
              </div>
            </div>
            <div class="right">
              <status-icon :status="recordingReport.status"></status-icon>
            </div>
          </div>

          <div
            class="payload-instance"
            v-for="payloadReport in recordingReport.payloadInstanceExecutionReportList"
            :key="payloadReport.id"
          >
            <div class="verb" v-if="payloadReport.payload">
              {{ payloadReport.payload.verb }}
            </div>
            <div class="detail">
              <payload-editor
                v-if="payloadReport.payload"
                class="payload"
                :mappingData="{}"
                :commandDescriptor="
                  findCommandDescriptor(
                    recordingReport.platformId,
                    payloadReport.payload.verb
                  )
                "
                :payload="payloadReport.payload"
                :readOnly="true"
              ></payload-editor>
              <div
                class="failed-payload-report"
                v-if="isFailedPayloadReport(payloadReport)"
              >
                <div class="log" v-if="payloadReport.log">
                  <div
                    class="log-item"
                    v-for="(log, index) in payloadReport.log"
                    :key="index"
                  >
                    {{ log }}
                  </div>
                </div>
                <div
                  class="screenshots"
                  v-if="
                    payloadReport.screenshotBefore &&
                    payloadReport.screenshotAfter
                  "
                >
                  <div class="screenshot-before">
                    <img
                      alt="before action"
                      title="before action"
                      :src="payloadReport.screenshotBefore"
                    />
                  </div>
                  <div class="transition">></div>
                  <div class="screenshot-after">
                    <img
                      alt="after action"
                      title="after action"
                      :src="payloadReport.screenshotAfter"
                    />
                  </div>
                </div>
                <div class="no-screenshots" v-else>
                  NO SCREENSHOTS AVAILABLE - MAKE SURE YOU RUN THIS WITH DEBUG
                  ACTIVATED
                </div>
              </div>
            </div>
            <div
              class="status"
              :alt="payloadReport.durationInMs"
              :title="payloadReport.durationInMs"
            >
              <div :class="{ running: payloadReport.status == 'RUNNING' }">
                <status-icon :status="payloadReport.status"></status-icon>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import _ from "lodash";
import {
  ExecutableRecordingInstanceExecutionReport,
  PayloadInstanceExecutionReport,
  Run,
} from "@/model/transport/execution/Run";
import { computed, ref, defineComponent, PropType } from "vue";
import PayloadEditor from "../recordings/editor/input-panel/recording-step/PayloadEditor.vue";
import usePlatform from "@/state/usePlatform";
import Platform from "@/model/transport/Platform";
import { CommandDescriptor } from "@/model/transport/CommandDescriptor";
import StatusIcon from "../common/StatusIcon.vue";
import { Recording } from "@/model/transport/Recording";
import RecordingService from "@/services/RecordingService";
import { Status } from "@/model/transport/execution/PayloadExecutionEvent";
export default defineComponent({
  props: {
    run: {
      type: Object as PropType<Run>,
      required: true,
    },
  },
  components: { PayloadEditor, StatusIcon },
  setup(props) {
    const { loadAvailablePlatforms, availablePlatforms } = usePlatform();
    const recordingService = new RecordingService();
    const recordings = ref<Array<Recording>>([]);

    loadAvailablePlatforms();

    const getRecording = (recordingId: string) => {
      const recording = recordings.value.find((r) => r.id === recordingId);
      if (recording) {
        return recording;
      } else {
        recordingService.getOne(recordingId).then((recording) => {
          recordings.value.push(recording);
          return recording;
        });
      }
    };

    const groupedRecordingReports = computed(() =>
      _.groupBy(
        props?.run.executableRecordingInstanceExecutionReportList,
        "parentRecordingId"
      )
    );

    const orderByInstanceId = (
      items: Array<ExecutableRecordingInstanceExecutionReport>
    ) => _.orderBy(items, ["id"], ["asc"]);

    const findCommandDescriptor = (platformId: string, verb: string) => {
      const platform = availablePlatforms.value.find(
        (platform: Platform) => platform.id == platformId
      );
      if (platform) {
        const commandDescriptor = platform.availableCommands.find(
          (commandDescriptor: CommandDescriptor) =>
            commandDescriptor.name === verb
        );
        if (commandDescriptor) {
          return commandDescriptor;
        } else {
          throw Error(`Could not find command descriptor for ${verb}`);
        }
      } else {
        throw Error(`Could not find platform ${platformId}`);
      }
    };

    const isFailedPayloadReport = (
      payloadReport: PayloadInstanceExecutionReport
    ) => (Status[payloadReport.status] as unknown as Status) == Status.FAILURE; // TODO ... huhhh check how to properly compare enums in TS
    return {
      availablePlatforms,
      groupedRecordingReports,
      findCommandDescriptor,
      getRecording,
      orderByInstanceId,
      isFailedPayloadReport,
    };
  },
});
</script>

<style lang="scss">
@keyframes runanimation {
  0% {
    zoom: 0.5;
  }
  25% {
    zoom: 1;
  }
  50% {
    zoom: 2;
  }
  75% {
    zoom: 4;
  }
  100% {
    zoom: 10;
  }
}

.running {
  > div {
    animation: runanimation 1s ease-in-out 0.1s infinite !important;
  }
}
#run-panel {
  @extend .page;
  width: 100%;
  .recording-group {
    margin-right: 1rem;
    text-align: left;
    padding-bottom: 2rem;
    > .header {
      font-size: 2rem;
      padding-top: 4rem;
      padding-bottom: 2rem;
      span {
        font-weight: 600;
      }
    }

    > .recording-instance {
      > .header {
        font-size: 2rem;
        padding-top: 2rem;
        padding-bottom: 2rem;
        padding-left: 1rem;
        padding-right: 1rem;
        margin-bottom: 2rem;
        border: 4px dashed;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        justify-content: space-between;
        .left {
          display: flex;
          flex-direction: column;
          justify-content: space-between;
        }
        .right {
          align-self: center;
          zoom: 2;
        }
        span {
          font-weight: 600;
        }
      }
      padding-left: 2rem;
      padding-top: 2rem;
      margin-bottom: 2rem;
      border-left: 2px dashed;

      > .payload-instance {
        display: flex;
        flex-direction: row;
        &:nth-child(2n) {
          background-color: #f8f8f8 !important;
        }
        padding-left: 1rem;
        padding-right: 1rem;
        padding-top: 1rem;
        justify-content: space-between;

        > .verb {
          align-self: center;
          font-weight: 600;
          flex: 2;
        }
        > .detail {
          justify-self: start;
          flex: 6;
          display: flex;
          flex-direction: column;
          > .failed-payload-report {
            border: 4px dashed red;
            padding: 2rem;
            > .log {
              padding: 2rem;
              > .log-item {
                word-wrap: break-word !important;
                word-break: break-all;
                white-space: normal;
                padding: 2rem;
                &:nth-child(odd) {
                  background-color: #ffffff !important;
                }
                &:nth-child(even) {
                  background-color: #f8f8f8 !important;
                }
              }
            }
            > .screenshots {
              display: flex;
              flex-direction: row;
              justify-content: center;
              justify-self: center;
              > div {
                > img {
                  width: 20rem;
                  cursor: pointer;
                }
              }
              > .transition {
                font-size: 10rem;
                align-self: center;
              }
            }
            > .no-screenshots {
              text-align: center;
              font-weight: 600;
              font-size: 1.5rem;
            }
          }
        }
        > .status {
          align-self: center;
          padding-left: 2rem;
        }
      }
    }
  }
}
</style>