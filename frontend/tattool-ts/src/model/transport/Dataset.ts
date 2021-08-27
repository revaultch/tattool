interface Dataset {
    readonly id?: string;
    readonly name?: string;
    readonly description?: string;
    readonly content?: Content;
}

interface Content {
    readonly columns: Array<string>;
    readonly rows: Array<Array<string>>;
}

export { Dataset, Content }