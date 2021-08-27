package ch.qarts.tattool.controller;

import ch.qarts.tattool.core.domain.platform.Platform;
import ch.qarts.tattool.core.domain.platform.PlatformProxy;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.port.in.PlatformService;
import ch.qarts.tattool.model.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class PlatformController {

    private final PlatformService platformService;

    @Autowired
    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    private static Platform mapOut(PlatformProxy t) {
        return Platform.builder()
                .id(t.getId())
                .description(t.getDescription()
                ).availableCommands(
                        t.getAvailableCommands()
                ).build();
    }


    // get available platforms
    @GetMapping(value = "/v1/platforms", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Platform> listPlatforms() {
        return platformService.getList().stream().map(PlatformController::mapOut).collect(Collectors.toList());
    }

    @GetMapping(value = "/v1/platforms/{platformId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Platform getPlatform(@PathVariable("platformId") String platformId) {
        return PlatformController.mapOut(platformService.getById(platformId));
    }

    @PostMapping(value = "/v1/platforms/{platformId}/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionData createSession(@PathVariable("platformId") String platformId) {
        return SessionData.builder().id(platformService.getById(platformId).createSession(new SessionConfiguration()).getId()).build();
    }

    @DeleteMapping(value = "/v1/platforms/{platformId}/sessions/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void destroySession(@PathVariable("platformId") String platformId, @PathVariable("sessionId") String sessionId) {
        platformService.getById(platformId).destroySession(sessionId);
    }

}
