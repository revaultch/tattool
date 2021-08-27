package ch.qarts.tattool.controller;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.port.in.PlatformService;
import ch.qarts.tattool.model.ImageData;
import ch.qarts.tattool.webplatform.analysis.ScreenshotService;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ScreenshotController {


    private final PlatformService platformService;

    @Autowired
    public ScreenshotController(PlatformService platformService) {
        this.platformService = platformService;
    }


    @GetMapping(value = "/v1/platforms/{platformId}/sessions/{sessionId}/screenshot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageData screenshot(@PathVariable("platformId") String platformId, @PathVariable("sessionId") String sessionId, @RequestParam(value = "tag", required = false) String tag) {
        WebPlatformSession session = (WebPlatformSession) platformService.getById(platformId).getSession(sessionId);
        var screenshotService = new ScreenshotService(session);
        if (tag != null) {
            ElementSuperLocator elementSuperLocator = session.getAnalyzerSnapshot().getItems().stream().filter(i -> i.getId().equals(tag)).findFirst().orElseThrow(IllegalStateException::new);
            return ImageData.builder().data(screenshotService.getFullPageScreenshotWithHighlightedElement(elementSuperLocator)).build();
        } else {
            return ImageData.builder().data(screenshotService.getViewportScreenshot()).build();
        }

    }
}
