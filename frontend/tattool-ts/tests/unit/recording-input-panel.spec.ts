import { expect } from 'chai'
import { shallowMount } from '@vue/test-utils'

import RecordingInputPanel from '@/components/recordings/editor/input-panel/RecorderInputPanel.vue';
import sinon from 'sinon';
import { Recording } from '@/model/transport/Recording';
import { recordingStepList } from '@/../tests/mocks/MockData';

import { ContextService } from '@/services/payload/ContextService';
import { ValidationService } from '@/services/payload/ValidationService';
import { ContextServiceMock } from "@/../tests/mocks/ContextServiceMock";
import { ValidationServiceMock } from '@/../tests/mocks/ValidationServiceMock';

const recording = {
  id: "1",
  name: "hola",
  description: "hello",
  target: "sometarget",
  tags: [],
  recordingStepList: recordingStepList.map((item) => ({ ...item, ...{ mappings: { x: "y" } } }))
} as Recording;


const contextService: ContextService = new ContextServiceMock();
const validationService: ValidationService = new ValidationServiceMock();

const props = {
}


/*

TO BE FIXED

const mountWithProps = (propsInput: any) => shallowMount(RecordingInputPanel, {
  props: propsInput, global: {
    provide: {
      'contextService': contextService,
      'validationService': validationService
    }
  }
} as any);

describe('RecorderInputPanel.vue', () => {

  afterEach(() => {
    // Restore the default sandbox here
    sinon.restore()
  });


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





});*/
