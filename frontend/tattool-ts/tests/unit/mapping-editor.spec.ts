import { expect } from 'chai'
import { mount } from '@vue/test-utils'
import MappingEditor from '@/components/recordings/editor/input-panel/recording-step/MappingEditor.vue';

const mountWithProps = (p: any) => mount(MappingEditor, {
  props: p,
} as any);


describe('MappingEditor.vue', () => {

  it('should display column selector on add mapping button click', async () => {
    // given
    const wrapper = mountWithProps({
      mappableColumns: ["col1", "col2", "col3"],
      value: null,
      readOnly: true,
    });
    // when
    await wrapper.find(".map").trigger("click");
    // then
    expect(wrapper.find(".editing")).to.exist;
  });

  it('should remove add mapping button when mapping triggered', async () => {
    // given
    const wrapper = mountWithProps({
      mappableColumns: ["col1", "col2", "col3"],
      value: null,
      readOnly: true
    });
    // when
    await wrapper.find(".map").trigger("click");
    // then
    expect(wrapper.find(".map").exists()).to.be.false;
  });


  it('should display column name if mapping was set for this field', () => {
    // given
    const wrapper = mountWithProps({
      mappableColumns: ["col1", "col2", "col3"],
      value: "col1",
      readOnly: true
    });
    // when
    // then
    expect(wrapper.find(".value")).to.exist;
  });

  it('should display remove mapping button if mapping was set for this field', () => {
    // given
    const wrapper = mountWithProps({
      mappableColumns: ["col1", "col2", "col3"],
      value: "col1",
      readOnly: true
    });
    // when
    // then
    expect(wrapper.find(".unset")).to.exist;
  });

  it('should remove mapping when clicking remove mapping button', async () => {
    // given
    const wrapper = mountWithProps({
      mappableColumns: ["col1", "col2", "col3"],
      value: "col1",
      readOnly: true
    });
    // when
    await wrapper.find(".unset").trigger("click");
    // then
    expect(wrapper.find(".value").exists()).to.be.false;
  });






})
