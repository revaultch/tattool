import { expect } from 'chai'
import { shallowMount } from '@vue/test-utils'
import PayloadEditor from '@/components/recordings/editor/input-panel/recording-step/PayloadEditor.vue';
import { CommandDescriptor } from '@/model/transport/CommandDescriptor';
import { commandDescriptors } from '@/../tests/mocks/MockData';
import { ContextServiceMock } from '@/../tests/mocks/ContextServiceMock';
import { recordingStepList } from '@/../tests/mocks/MockData';

const contextService = new ContextServiceMock();


const buildProps = (command: string, payload: any, readOnly: boolean) => ({
  commandDescriptor: commandDescriptors.filter(cd => cd.name == command)[0] as CommandDescriptor,
  payload: payload,
  mappingData: {},
  contextData: contextService.getContext(command),
  readOnly: readOnly
});

const mountWithProps = (p: any) => shallowMount(PayloadEditor, {
  props: p
} as any);


describe('PayloadEditor.vue', () => {

  it('should prepend text if present in field descriptor', async () => {
    // given
    const wrapper = mountWithProps(buildProps("write", recordingStepList[2].payload, true));
    // when
    // then
    expect(wrapper.find('.prepend').exists()).to.be.true;
  });

  it('should display mapping editor element if dataset is available and field is mappable', () => {
    // given
    const wrapper = mountWithProps({ ...buildProps("write", recordingStepList[2].payload, true), ...{ mappingData: { mappableColumns: ["col1", "col2"] } } });
    // when
    // then
    expect(wrapper.find('.mapping-editor-holder').exists()).to.be.true;
  });

  it('should not display mapping editor element if mappableColumns are null', () => {
    // given
    const wrapper = mountWithProps({ ...buildProps("write", recordingStepList[2].payload, true), ...{ mappingData: { mappableColumns: null } } });
    // when
    // then
    expect(wrapper.find('.mapping-editor-holder').exists()).to.be.false;
  });

  it('should not display mapping editor element if mappableColumns are empty', () => {
    // given
    const wrapper = mountWithProps({ ...buildProps("write", recordingStepList[2].payload, true), ...{ mappingData: { mappableColumns: [] } } });
    // when
    // then
    expect(wrapper.find('.mapping-editor-holder').exists()).to.be.false;
  });

  it('should not display mapping editor element if no field is mappable', () => {
    // given
    const wrapper = mountWithProps({ ...buildProps("click", recordingStepList[1].payload, true), ...{ mappingData: { mappableColumns: ["col1", "col2"] } } });
    // when
    // then
    expect(wrapper.find('.mapping-editor-holder').exists()).to.be.false;
  });


  it('should not display mapping editor in edit mode', () => {
    // given
    const wrapper = mountWithProps({ ...buildProps("write", recordingStepList[2].payload, false), ...{ mappingData: { mappableColumns: ["col1", "col2"] } } });
    // when
    // then
    expect(wrapper.find('.mapping-editor-holder').exists()).to.be.false;
  });





})
