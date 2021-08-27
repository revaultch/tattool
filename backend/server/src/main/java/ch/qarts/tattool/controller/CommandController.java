package ch.qarts.tattool.controller;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;
import ch.qarts.tattool.core.domain.execution.client.validation.ValidationResult;
import ch.qarts.tattool.core.domain.recording.RecordingStep;
import ch.qarts.tattool.core.domain.session.SessionProxy;
import ch.qarts.tattool.core.port.in.PayloadExecutionService;
import ch.qarts.tattool.core.port.in.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CommandController {

    private PlatformService platformService;

    private PayloadExecutionService payloadExecutionService;

    @Autowired
    public CommandController(PlatformService platformService, PayloadExecutionService payloadExecutionService) {
        this.platformService = platformService;
        this.payloadExecutionService = payloadExecutionService;
    }


    @GetMapping(value = "/v1/platforms/{platformId}/sessions/{sessionId}/context/{commandName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ContextData getCommandContext(@PathVariable("platformId") String platformId, @PathVariable("sessionId") String sessionId, @PathVariable("commandName") String commandName) {
        SessionProxy session = platformService.getById(platformId).getSession(sessionId);
        return session.commandContextData(commandName);
    }


    @GetMapping(value = "/v1/platforms/{platformId}/sessions/{sessionId}/commands", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommandDescriptor> getAvailableCommandsInCurrentContext(@PathVariable("platformId") String platformId, @PathVariable("sessionId") String sessionId) {
        return platformService.getById(platformId).getSession(sessionId).getAvailableCommands();
    }


    @PostMapping(value = "/v1/platforms/{platformId}/sessions/{sessionId}/batch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ValidationResult validateBatch(@PathVariable("platformId") String platformId, @PathVariable("sessionId") String sessionId, @RequestBody List<RecordingStep<? extends Payload>> recordingStepList) {
        var platform = platformService.getById(platformId);
        var session = platform.getSession(sessionId);
        return payloadExecutionService.validateBatch(session, recordingStepList);
    }


}
