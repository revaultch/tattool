import { expect } from 'chai'
import { shallowMount } from '@vue/test-utils'

import RecordingStepsEditor from '@/components/recordings/editor/input-panel/recording-step/RecordingStepsEditor.vue';
import { RecordingStep } from '@/model/transport/RecordingStep';
import { ContextService } from '@/services/payload/ContextService';
import { ValidationService } from '@/services/payload/ValidationService';
import { ContextServiceMock } from "@/../tests/mocks/ContextServiceMock";
import { ValidationServiceMock } from '@/../tests/mocks/ValidationServiceMock';
import sinon from 'sinon';
import { Recording } from '@/model/transport/Recording';
import { recordingStepList } from '@/../tests/mocks/MockData';
import { commandDescriptors } from '@/../tests/mocks/MockData';


const recording = {
  id: "1",
  name: "hola",
  description: "hello",
  mappings: {},
  tags: [],
  recordingStepList: recordingStepList
} as Recording;

const contextService: ContextService = new ContextServiceMock();
const validationService: ValidationService = new ValidationServiceMock();

const props = {
  commandDescriptors: commandDescriptors,
  recording: recording,
  contextService: contextService,
  validationService: validationService
} as any;

const mountWithProps = (propsInput: any) => shallowMount(RecordingStepsEditor, { props: propsInput } as any);

describe('RecordingStepsEditor.vue', () => {

  afterEach(() => {
    // Restore the default sandbox here
    sinon.restore()
  });

  it('should add empty step when clicked new step', () => {
    // given
    const wrapper = mountWithProps(props);
    const newRecordingStepButton: any = wrapper.find("#new-recording-step-button");
    expect(wrapper.find("#new-recording-step-placeholder").exists()).to.be.false;
    // when
    newRecordingStepButton.trigger("click");
    // then
    expect(wrapper.find("#new-recording-step-placeholder")).to.exist;
  });

  it('should remove new recording placeholder when new step played', async () => {
    // given
    const wrapper = mountWithProps(props);
    const vm = wrapper.vm as any;
    const newRecordingStepButton: any = wrapper.find("#new-recording-step-button");
    await newRecordingStepButton.trigger("click"); // trigger new record
    await vm.$nextTick(); await vm.$nextTick(); // multiple async processing
    const changeRecordingStepCopy = vm.newRecordingStep as RecordingStep;
    changeRecordingStepCopy.payload.verb = "click";
    await vm.handleNewRecordingStepChange(changeRecordingStepCopy);
    expect(wrapper.find("#new-recording-step-button").exists()).to.be.false;
    expect(wrapper.find("#new-recording-step-placeholder .command-panel-new .play").exists()).to.be.true; // play & validate
    // when
    const playButton = wrapper.find("#new-recording-step-placeholder .command-panel-new .play");
    await playButton.trigger("click");

    // then
    await vm.$nextTick(() => expect(wrapper.find("#new-recording-step-placeholder").exists()).to.be.false);
  });


  it('should show warning when invoking step deletion', async () => {
    // given
    const wrapper = mountWithProps(props);
    const firstDeleteButton: any = wrapper.findAll(".delete")[0];
    expect(wrapper.findAll(".recording-step-holder").length).to.eq(3);
    // when    
    await firstDeleteButton.trigger("click");
    // then
    expect(wrapper.findAll(".pre-deletion-warning").length).to.eq(1);

  });

  it('should delete when deletion warning accepted', async () => {
    // given
    const wrapper = mountWithProps(props);
    const vm = wrapper.vm as any;
    expect(wrapper.findAll(".recording-step-holder").length).to.eq(3);
    const firstDeleteButton: any = wrapper.findAll(".delete")[0];
    await firstDeleteButton.trigger("click");
    expect(wrapper.find(".pre-deletion-warning").exists()).to.be.true;
    expect(wrapper.findAll(".recording-step-holder").length).to.eq(3);
    // when    
    const firstAcceptButton = wrapper.find(".accept");
    await firstAcceptButton.trigger("click");
    // then
    expect(wrapper.emitted()).haveOwnProperty('change');
    const changeEvent = wrapper.emitted('change');


    // TODO really why so deep?
    expect((changeEvent as any)[0][0].recordingStepList.length).to.eq(2);

  });

  it('should not delete when deletion warning rejected', async () => {
    // given
    const wrapper = mountWithProps(props);
    const vm = wrapper.vm as any;
    expect(wrapper.findAll(".recording-step-holder").length).to.eq(3);
    const firstDeleteButton: any = wrapper.findAll(".delete")[0];
    await firstDeleteButton.trigger("click");
    expect(wrapper.find(".pre-deletion-warning").exists()).to.be.true;
    expect(wrapper.findAll(".recording-step-holder").length).to.eq(3);
    // when    
    const firstCancelButton = wrapper.find(".cancel");
    await firstCancelButton.trigger("click");
    // then
    expect(wrapper.emitted()).not.haveOwnProperty('change');

  });


  it('should replay all steps when step was removed and warning accepted', () => {
    // given
    const sandbox = sinon.createSandbox();
    sinon.spy(props.validationService);
    const wrapper = mountWithProps(props);
    const vm = wrapper.vm as any;
    // when
    vm.confirmDelete("ccc");
    // then 
    expect(props.validationService.validate.calledOnce);
    expect(props.validationService.validate.getCall(0).args[0].length).to.eq(2);
    expect(props.validationService.validate.getCall(0).args[0][0].id).to.eq("aaa");
    expect(props.validationService.validate.getCall(0).args[0][1].id).to.eq("bbb");
    sinon.verify();
  });

  // replay

  it('should validate all steps up to played one', async () => {
    // given
    const sandbox = sinon.createSandbox();
    sinon.spy(props.validationService);
    const wrapper = mountWithProps(props);
    expect(wrapper.findAll(".recording-step-holder").length).to.eq(3);
    const lastPlayButton: any = wrapper.findAll(".play")[1];
    // when    
    await lastPlayButton.trigger("click");
    // then
    expect(props.validationService.validate.calledOnce);
    expect(props.validationService.validate.getCall(0).args[0].length).to.eq(2);
    expect(props.validationService.validate.getCall(0).args[0][0].id).to.eq("aaa");
    expect(props.validationService.validate.getCall(0).args[0][1].id).to.eq("bbb");
    sinon.verify();
  });



  it('should validate step on creation', async () => {
    // given
    const sandbox = sinon.createSandbox();
    sinon.spy(props.validationService);
    const wrapper = mountWithProps(props);
    const vm = wrapper.vm as any;
    const newRecordingStepButton: any = wrapper.find("#new-recording-step-button");
    await newRecordingStepButton.trigger("click");
    await vm.$nextTick(); await vm.$nextTick(); // multiple async processing
    const changeRecordingStepCopy = vm.newRecordingStep as RecordingStep;
    changeRecordingStepCopy.payload.verb = "click";
    await vm.handleNewRecordingStepChange(changeRecordingStepCopy);
    expect(wrapper.find("#new-recording-step-button").exists()).to.be.false;
    expect(wrapper.find("#new-recording-step-placeholder .command-panel-new .play").exists()).to.be.true;
    // when
    const playButton = wrapper.find("#new-recording-step-placeholder .command-panel-new .play");
    await playButton.trigger("click");

    // then
    await vm.$nextTick(() => {
      expect(props.validationService.validate.calledOnce);
    });

  });




  it('should validate all items before adding new item for the first time', async () => {
    // given
    const sandbox = sinon.createSandbox();
    sinon.spy(props.validationService);
    const wrapper = mountWithProps(props);
    const vm = wrapper.vm as any;
    const newRecordingStepButton: any = wrapper.find("#new-recording-step-button");
    expect(wrapper.findAll(".recording-step-holder").length).to.be.greaterThan(0); // important

    // when
    await newRecordingStepButton.trigger("click");
    await vm.$nextTick(); await vm.$nextTick(); // multiple async processing

    // then
    await vm.$nextTick(() => {
      expect(props.validationService.validate.calledOnce);
      expect(props.validationService.validate.getCall(0).args[0].length).to.eq(3); // validate all 3 existing records before proceeding
    });
  });

  it('should skip validation when adding new item for the 2nd+ time', async () => {

    // given
    const sandbox = sinon.createSandbox();
    sinon.spy(props.validationService);
    const wrapper = mountWithProps(props);
    const vm = wrapper.vm as any;
    const newRecordingStepButton: any = wrapper.find("#new-recording-step-button");
    expect(wrapper.findAll(".recording-step-holder").length).to.be.greaterThan(0); // important

    // when
    await newRecordingStepButton.trigger("click");
    await vm.$nextTick(); await vm.$nextTick(); // multiple async processing
    const changeRecordingStepCopy = vm.newRecordingStep as RecordingStep;
    changeRecordingStepCopy.payload.verb = "click";
    await vm.$nextTick();

    const abortButton: any = wrapper.find(".abort");
    await abortButton.trigger("click");

    // new recording (AGAIN)
    await newRecordingStepButton.trigger("click");
    await vm.$nextTick(); await vm.$nextTick(); // multiple async processing

    // then
    await vm.$nextTick(() => {
      expect(props.validationService.validate.calledOnce); // called ONLY once
    });

  });



});
