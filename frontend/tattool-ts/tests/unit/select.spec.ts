import { expect } from 'chai'
import { mount } from '@vue/test-utils'
import SelectTemplate from '@/components/recordings/editor/input-panel/recording-step/SelectTemplate.vue'


const mountDefault = () => mount(SelectTemplate, {
  props: {
    itemsProvider: () => [{ data: 'hello' }, { data: 'hola' }, { data: 'hilo' }, { data: 'zola' }],
    itemRenderer: (item: any) => item.data,
    itemFilter: (item: any, term: string) => item.data.includes(term),
    valueRenderer: (item: any) => item.data
  }
} as any);


describe('SelectTemplate.vue', () => {
  it('should display provided items', () => {
    const wrapper = mountDefault();
    expect(wrapper.findAll(".selector div").length).to.eq(4);
    expect(wrapper.findAll(".selector div")[0].text()).to.include("hello");
    expect(wrapper.findAll(".selector div")[1].text()).to.include("hola");
    expect(wrapper.findAll(".selector div")[2].text()).to.include("hilo");
    expect(wrapper.findAll(".selector div")[3].text()).to.include("zola");
  })

  /*
    it('should become editable on focus', async () => {
      const wrapper = mountDefault();
  
      expect.fail("not implemented");
    });
  */

  it('should filter items', async () => {
    const wrapper = mountDefault();
    const input: any = wrapper.find(".text");
    await input.setValue("lo");
    expect(wrapper.findAll(".selector div").length).to.eq(2);
    expect(wrapper.findAll(".selector div")[0].text()).to.include("hello");
    expect(wrapper.findAll(".selector div")[1].text()).to.include("hilo");
  });

  it('should set value when clicked', async () => {
    const wrapper = mountDefault();
    await wrapper.findAll('.selector div')[2].trigger("click");

    expect(wrapper.findAll(".select-template .value").length).to.eq(1);
    expect(wrapper.findAll(".select-template .value")[0].text()).to.eq("hilo");

    // no selector
    expect(wrapper.findAll(".selector div").length).to.eq(0);

  });

  it('should emit event when value changed (select with click)', async () => {
    const wrapper = mountDefault();

    await wrapper.findAll(".selector div")[0].trigger("click");

    expect(wrapper.emitted()).haveOwnProperty('change')
  });


  it('should emit event when value changed (select with tab)', async () => {
    const wrapper = mountDefault();

    const text = wrapper.find(".text");

    await text.setValue("hol");

    await text.trigger("keydown.tab");

    expect(wrapper.emitted()).haveOwnProperty("change")
  });

  /*
    it('should set old value when hitting esc', async () => {
      expect.fail("not implemented");
  
    });
  
  */

})
