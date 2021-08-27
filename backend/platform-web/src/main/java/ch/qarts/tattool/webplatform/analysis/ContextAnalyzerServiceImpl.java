package ch.qarts.tattool.webplatform.analysis;

import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v91.page.Page;
import org.openqa.selenium.devtools.v91.page.model.Viewport;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class ContextAnalyzerServiceImpl {


    private DevTools devTools;

    public ContextAnalyzerServiceImpl(DevTools devTools) {
        this.devTools = devTools;
    }


    public void injectSpy(boolean debug) {
        devTools.createSessionIfThereIsNotOne();
        devTools.send(Page.enable());
        devTools.send(Page.addScriptToEvaluateOnNewDocument(getJavaScript(debug), Optional.empty()));
    }


    public String getFullPageShot(int x, int y, int width, int height, int scale) {
        devTools.createSessionIfThereIsNotOne();
        return devTools.send(Page.captureScreenshot(Optional.empty(), Optional.empty(), Optional.of(new Viewport(x, y, width, height, scale)), Optional.empty(), Optional.of(true)));
    }

    private String buildScriptlet(boolean debug) {
        return String.format("const debug=%s;", debug);
    }

    @SneakyThrows
    private String getJavaScript(boolean debug) {
        final var templateTag = "/**template*/";

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(this.getClass().getClassLoader().getResource("js/main.js")).getFile()))) {
            reader.lines().forEach(sb::append);
        }
        String scriptlet = buildScriptlet(debug);
        return sb.toString().replace(templateTag, scriptlet);
    }

    @SneakyThrows
    public AnalyzerSnapshot buildAnalyzerSnapshot(WebPlatformSession session, SimpleLogger simpleLogger) {
        String str = (String) session.getWebDriver().executeScript("if (window.ttAnalysisHolder) {window.ttAnalysisHolder.refreshBounds(); return JSON.stringify(window.ttAnalysisHolder.getContent())} else {return '{}';};");
        var snapshot = new Gson().fromJson(str, AnalyzerSnapshot.class);
        mapJsLogs(snapshot, simpleLogger);
        if (snapshot.getItems().size() > 0) {
            ScreenshotService screenshotService = new ScreenshotService(session);
            BufferedImage bufferedImage = screenshotService.getFullPageScreenshot();
            for (var item : snapshot.getItems()) {
                screenshotService.updatePageElementScreenshot(item, bufferedImage);
            }
        }
        return snapshot;
    }

    private void mapJsLogs(AnalyzerSnapshot snapshot, SimpleLogger simpleLogger) {
        if (simpleLogger != null && snapshot != null) {
            for (var log : snapshot.getLog()) {
                simpleLogger.info(this.getClass(), String.format("from JS : %s", log));
            }
        }
    }

}
