interface ElementSuperLocator {
    readonly id: string;
    readonly screenshot: string;
    readonly asString: string;
    readonly locators: Array<ElementLocator>;
}

interface ElementLocator {
    readonly type: string;
    readonly query: string;
}

export { ElementSuperLocator, ElementLocator }