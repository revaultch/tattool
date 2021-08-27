import { expect } from 'chai'
import { shallowMount } from '@vue/test-utils'

import RecordingInputPanel from '@/components/recordings/editor/input-panel/RecorderInputPanel.vue';
import sinon from 'sinon';
import { Recording } from '@/model/transport/Recording';
import { recordingStepList } from '@/../tests/mocks/MockData';

const recording = {
  id: "1",
  name: "hola",
  description: "hello",
  target: "sometarget",
  tags: [],
  recordingStepList: recordingStepList.map((item) => ({ ...item, ...{ mappings: { x: "y" } } }))
} as Recording;

const props = {
}

const mountWithProps = (propsInput: any) => shallowMount(RecordingInputPanel, { props: propsInput } as any);

describe('RecorderInputPanel.vue', () => {

  afterEach(() => {
    // Restore the default sandbox here
    sinon.restore()
  });

  /*

  TODO redesign to fix this

  it('should clear mappings upon dataset change', () => {
    // given
    const wrapper = mountWithProps(props);

    const vm = wrapper.vm as any;

    vm.dirtyRecording = recording;

    // when
    vm.handleDatasetChanged({});

    // then
    expect(vm.dirtyRecording.recordingStepList[0].mappings).deep.eq({});

  });
*/




});
