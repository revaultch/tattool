
interface CommandDescriptor {
    readonly name: string;
    readonly description: string;
    readonly payloadDescriptor: FieldDescriptor[];
}


interface FieldDescriptor {
    readonly name: string;
    readonly annotations: any;
}

const findCommandDescriptorByName = (name: string, commandDescriptors: Array<CommandDescriptor>): CommandDescriptor => {
    const index = commandDescriptors.findIndex((commandDescriptor) => commandDescriptor.name === name);
    if (~index) {
        return commandDescriptors[index];
    } else {
        throw Error(`Error while finding index for command descriptor with name ${name}`);
    }
}


const findFieldDescriptorByName = (name: string, fieldDescriptors: Array<FieldDescriptor>): FieldDescriptor => {
    const index = fieldDescriptors.findIndex((fieldDescriptor: FieldDescriptor) => fieldDescriptor.name === name);
    if (~index) {
        return fieldDescriptors[index];
    } else {
        throw Error(`Error while finding index for field descriptor with name ${name}`);
    }
}

export { CommandDescriptor, FieldDescriptor, findCommandDescriptorByName, findFieldDescriptorByName }