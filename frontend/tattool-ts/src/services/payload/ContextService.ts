import { CommandDescriptor } from "@/model/transport/CommandDescriptor";

interface ContextService {
    getContext(command: string): Promise<any>;

    getAvailableCommandDescriptorsInContext(): Promise<Array<CommandDescriptor>>;
}



export { ContextService }