import PayloadEventToRunConverter from '@/components/runs/PayloadEventToRunConverter';
import { PayloadExecutionEvent, Status } from '@/model/transport/execution/PayloadExecutionEvent';
import { Type } from '@/model/transport/execution/Run';
import { expect } from 'chai'


const defaultEvent = {
  platformId: "some-target-id",
  recordingId: "610a79f9ac941027a5632d9d",
  recordingDisplayName: "recordingDisplayName",
  recordingInstanceId: "recordingInstanceId",
  id: "id",
  displayName: "displayName",
  payload: {},
  status: Status.RUNNING,
  durationInMs: 111
} as PayloadExecutionEvent;



describe('PayloadExecutionToRunConverter.ts', () => {

  it('should create a new record instance report if it does not exist', async () => {
    // given
    const convert = new PayloadEventToRunConverter("some-ref", "some title", Type.RECORDING);
    // when
    const result = convert.reduce(defaultEvent)
    // then
    expect(result.executableRecordingInstanceExecutionReportList.length).to.be.equal(1);
  });

  it('should create a new payload instance report if it does not exist', async () => {
    // given
    const convert = new PayloadEventToRunConverter("some-ref", "some title", Type.RECORDING);
    // when
    const result = convert.reduce(defaultEvent)
    // then
    expect(result.executableRecordingInstanceExecutionReportList[0].payloadInstanceExecutionReportList.length).to.be.equal(1);
  });


  it('should update record instance report if it exists', async () => {
    // given
    const convert = new PayloadEventToRunConverter("some-ref", "some title", Type.RECORDING);
    // when
    let result = convert.reduce(defaultEvent)
    result = convert.reduce({ ...defaultEvent, ...{ status: Status.FAILURE } })
    // then
    expect(result.executableRecordingInstanceExecutionReportList.length).to.be.equal(1);
    expect(result.executableRecordingInstanceExecutionReportList[0].status).to.be.equal(Status.FAILURE);
  });

  it('should update payload instance report if it exists', async () => {
    // given
    const convert = new PayloadEventToRunConverter("some-ref", "some title", Type.RECORDING);
    // when
    let result = convert.reduce(defaultEvent)
    result = convert.reduce({ ...defaultEvent, ...{ status: Status.FAILURE } })
    // then
    expect(result.executableRecordingInstanceExecutionReportList.length).to.be.equal(1);
    expect(result.executableRecordingInstanceExecutionReportList[0].payloadInstanceExecutionReportList.length).to.be.equal(1);
    expect(result.executableRecordingInstanceExecutionReportList[0].payloadInstanceExecutionReportList[0].status).to.be.equal(Status.FAILURE);
  });



})
