package ch.qarts.tattool.webplatform.intg.screenshot;

import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.analysis.AnalyzerSnapshot;
import ch.qarts.tattool.webplatform.intg.Page;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScreenshotIntgTest extends AbstractSessionTest {


    @SneakyThrows
    @Test
    public void shouldTakeScreenshot() {
        Page page = Page.builder()
                .bodyContent("<textarea id='xyz'></textarea>")
                .scriptContent("")
                .build();
        WebPlatformSession webPlatformSession = buildSession(page);
        Awaitility.await().atMost(Duration.FIVE_SECONDS).until(() -> {
            AnalyzerSnapshot snapshot = webPlatformSession.getContextAnalyzerService().buildAnalyzerSnapshot(webPlatformSession, simpleLogger);
            assertEquals(1, snapshot.getClickable().size());
            assertNotNull(snapshot.getClickable().get(0).getScreenshot());
        });
    }

    @Disabled
    @Test
    public void shouldNotTakeScreenshot() {
        Page page = Page.builder()
                .bodyContent("<textarea id='xyz'></textarea>")
                .scriptContent("")
                .build();
 /*       WebTargetSession webTargetSession = buildSession(page, Boolean.FALSE);

        Awaitility.await().atMost(Duration.FIVE_SECONDS).until(() -> {
            AnalyzerSnapshot snapshot = webTargetSession.getContextAnalyzerService().buildAnalyzerSnapshot(webTargetSession);
            assertEquals(1, snapshot.getClickable().size());
            assertNull(snapshot.getClickable().get(0).getScreenshot());
        });*/
    }


    private int toInt(double x) {
        return (int) x;
    }


    @SneakyThrows
    private String toBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "src/test/resources/screenshot/png", os);
        return Base64.getEncoder().encodeToString(os.toByteArray());
    }

}
