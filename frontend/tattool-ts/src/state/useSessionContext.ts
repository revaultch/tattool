import { CommandDescriptor } from "@/model/transport/CommandDescriptor";
import { RecordingStep } from "@/model/transport/RecordingStep";
import CommandService from "@/services/CommandService";
import { ContextService } from "@/services/payload/ContextService";
import { ValidationResult, ValidationService } from "@/services/payload/ValidationService";
import useUIState from "@/state/useUIState";
import { ref } from "vue";
import useSession from "./useSession";
import usePlatform from "./usePlatform";

const { sessionId } = useSession();

const { currentPlatform } = usePlatform();

const { withLoading } = useUIState();

const commandService: CommandService = new CommandService();

const availableCommands = ref([] as Array<CommandDescriptor>);
const currentCommandContext = ref({} as any);


const validateContext = () => {
  if (currentPlatform.value == null || sessionId.value == null) {
    throw Error(`Validation error : Invalid state targetId : ${currentPlatform.value?.id} - sessionId : ${sessionId.value}`);
  }
};


class DefaultValidationService implements ValidationService {

  async validate(recordingSteps: RecordingStep[]): Promise<ValidationResult> {
    validateContext();
    return await withLoading(
      `performing recording steps validation`,
      commandService.validateBatch(currentPlatform.value!.id, sessionId.value!, recordingSteps)
    );

  }


}

const defaultValidationService: ValidationService = new DefaultValidationService();

class DefaultContextService implements ContextService {

  private readonly commandService: CommandService;

  constructor(commandService: CommandService) {
    this.commandService = commandService;
  }

  async getAvailableCommandDescriptorsInContext(): Promise<Array<CommandDescriptor>> {
    validateContext();
    return await withLoading(
      "loading commands",
      commandService.getAvailableCommandsInCurrentContext(currentPlatform.value!.id, sessionId.value!)
    );
  }

  public async getContext(commandName: string): Promise<any> {
    validateContext();
    return await withLoading(
      `loading context data for command ${commandName}`,
      commandService.getCommandContext(currentPlatform.value!.id, sessionId.value!, commandName)
    );
  }

}



const defaultContextService: ContextService = new DefaultContextService(commandService);



export default function useSessionContext() {


  return {
    defaultValidationService,
    defaultContextService,
  }
}
