import { expect } from 'chai'
import { mount } from '@vue/test-utils'
import TextTemplate from '@/components/recordings/editor/input-panel/recording-step/TextTemplate.vue'


const mountDefault = () => mount(TextTemplate, {
  props: {
    value: "hello",
  }
} as any);

const mountRO = () => mount(TextTemplate, {
  props: {
    value: "hello",
    readOnly: true
  }
} as any);


describe('TextTemplate.vue', () => {

  it('should be editable when in write mode', async () => {
    const wrapper = mountDefault();
    const text = wrapper.find("input");
    expect(text.attributes().readonly).not.to.exist
  });


  it('should not be editable when in read mode', async () => {
    const wrapper = mountRO();
    const text = wrapper.find("input");
    expect(text.attributes().readonly).to.exist
  });


  it('should raise next event on tab', async () => {
    const wrapper = mountDefault();
    const text = wrapper.find("input");
    await text.trigger("keydown.tab")
    expect(wrapper.emitted()).haveOwnProperty("next")
  });



})
