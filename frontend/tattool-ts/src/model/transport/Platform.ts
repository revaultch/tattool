import { CommandDescriptor } from "./CommandDescriptor";

interface Platform {
    readonly id: string;
    readonly description: string;
    readonly availableCommands: Array<CommandDescriptor>;
}

export default Platform;