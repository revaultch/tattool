import { CommandDescriptor } from "@/model/transport/CommandDescriptor";
import { Dataset } from "@/model/transport/Dataset";
import { Recording } from "@/model/transport/Recording";
import { RecordingStep } from "@/model/transport/RecordingStep";

const commandDescriptors: Array<CommandDescriptor> = [
    {
        name: "navigateTo",
        description: "navigates to url",
        payloadDescriptor:
            [
                {
                    name: "url",
                    annotations: {
                        Renderer: {
                            value: "textEditor",
                        },
                    },
                },
            ],
    },
    {
        name: "click",
        description: "clicks element",
        payloadDescriptor: [
            {
                name: "elementSuperLocator",
                annotations: {
                    DataProvided: {},
                    Renderer: {
                        value: "elementSuperLocatorSelector",
                    },
                },
            },
        ],

    },
    {
        name: "write",
        description: "writes something into element",
        payloadDescriptor:
            [
                {
                    name: "text",
                    annotations: {
                        Mappable: {},
                        Renderer: {
                            value: "textEditor",
                        },
                    },
                },
                {
                    name: "elementSuperLocator",
                    annotations: {
                        Prepend: {
                            value: "into",
                        },
                        DataProvided: {},
                        Renderer: {
                            value: "elementSuperLocatorSelector",
                        },
                    },
                },
            ],
    },
];

const elementSuperLocator: any = {
    screenshot:
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAAAjCAIAAAChPqBDAAADLUlEQVR4Xu2cy2tTQRSH8xe1iILiouBOQVwILtxY0YUJoqC4KCqI6KIUXFifWaRV2lpabOurNFErKFa0tqZaIdjWpBRSk1bS9J00j+vQo5PrTCJt6Mwk8Xf4AnfOnDM3i/m494ZLHJH4SrWrCQCgnyPX+yzLcsgTAABNOD2d78YgIQDGqHI21dS1Q0IADAMJATAMJATAMJAQAMNAQgAMAwkBMAwk1M2e8x19n0KBcKxjYKy2sU+YPXbTxzhwtUdu3BJUrw+KABJqxcoXtY1eoWB2YVXu3RJUrw+KABLqI6edFEKNOklUrw+KABLqgwRYTqR4JpnKUPK297NcD/4TIKE+yDfLdt3beaZlYXUtHFvs/jBhr5mdz12pfCOTvPFQwxM6SGey9vrGXv/bwDQvc7n75bPnXX+z7UAFkFAffJezyGSzd31fCtXw28X4ctLexUOQUI5dZ1vlxeX1/27KRaF2oAJIqI/D13rFzb4e7HrIayhDkuw4fZ/XsGM+a+WT8NXoFBuyiyoNv03PyV9AWL+IdqACSKiVbSeb2TMh3/o89l3uogIakiSe/q80PHHnJW+njCwhPwUNY0tJ+ezC+kW0AxVAQjPsPtc2HJzhDnAN6Jgk+R6N26fsBYKE8ys5Z+SM3C5IuPF2oAJIqIlIfIX2t5CnJM/TMUkSCMfkFsoIEv6YWxYKCllEs4KEG28HKoCEmmh5HZB3fPU/JXS/GKXh4ESEZi+0DVAGElYSkFAftL+t9Z9Gnw4F7bej/BmMhvIzWzi2GJqZ50NIWElAQn00PPpIW1wOXkNDLkl9T/4WSFhJQEKtbD91L5X+/ZYMhT80Yy+gZNT2F3gH6x+za2A2a/lGJnlBYi1tr5/6uSCsEFtK2JcttP5m24EKIGHpsvfSQ+bDUiI1FIzyJEnCtJTrQZkCCUsaUo5UjP75fZXF/ivdcjEoUyBhSeP1514c5YF7xQoDEpYBFx8MPBsODo5HWt8E2FOlXADKGkgIgGEgIQCGgYQAGAYSAmAYSAiAYSAhACY5fuu54+gNb5XTU+1qlqcBAKqxLMvBPl3vx2vq2uVpAIA6XO5+evviF1k8YymltdtwAAAAAElFTkSuQmCC",
    asString: "input submit signin",
    locators: [
        {
            type: "id",
            query: null,
        },
        {
            type: "text",
            query: "INPUT:",
        },
    ],
};


const recordingStepList: Array<RecordingStep> = [
    {
        id: "aaa",
        payload: {
            verb: "navigateTo",
            url: "http://somewhere.blue",
        },
        mappings: undefined
    },
    {
        id: "bbb",
        payload: {
            verb: "click",
            elementSuperLocator: elementSuperLocator
        },
        mappings: undefined

    },
    {
        id: "ccc",
        payload: {
            verb: "write",
            text: "some text",
            elementSuperLocator: elementSuperLocator
        },
        mappings: undefined

    }
];


const someRecording: Recording = {
    id: "",
    name: "",
    description: "",
    dataset: {
        content: {
            columns: ["col1", "col2", "col3"],
        },
    } as Dataset,
    recordingStepList: recordingStepList
} as Recording;

export { someRecording, recordingStepList, commandDescriptors };

