package ch.qarts.tattool.webplatform.analysis;

import ch.qarts.tattool.core.domain.element.Bounds;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import static ch.qarts.tattool.webplatform.util.ImageUtils.*;

/**
 * Page / Element screenshot service
 */
// TODO fix "circular" dependency between ScreenshotService and ContextAnalyzer service
@Data
@Slf4j
public class ScreenshotService {


    private WebPlatformSession session;

    public ScreenshotService(WebPlatformSession session) {
        this.session = session;
    }

    private BrowserGeometry getGeometry() {
        JavascriptExecutor executor = this.getSession().getWebDriver();

        var map = (Map<String, Long>) executor.executeScript("" +
                "let contentWidth = [...document.body.children].reduce( \n" +
                "    (a, el) => Math.max(a, el.getBoundingClientRect().right), 0) \n" +
                "    - document.body.getBoundingClientRect().x;" +
                "var body = document.body, html = document.documentElement;" +
                "return {" +
                "pageHeight : html.scrollHeight, " +
                "pageWidth: Math.min(body.scrollWidth, Math.floor(contentWidth)), " +
                "windowWidth: html.clientWidth, " +
                "windowHeight:  html.clientHeight };");
        return BrowserGeometry.builder().pageHeight(map.get("pageHeight")).pageWidth(map.get("pageWidth")).windowHeight(map.get("windowHeight")).windowWidth(map.get("windowWidth")).build();
    }

    private void setScrollTop(Long position) {
        this.session.getWebDriver().executeScript(String.format("window.scrollTo(0, %d);", position));
    }


    private BufferedImage rescaleTo(BufferedImage source, int newWidth, int newHeight) {
        BufferedImage rescaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = rescaled.createGraphics();
        g.drawImage(source, 0, 0, newWidth, newHeight, null);
        return rescaled;
    }


    private BufferedImage getSubImage(BufferedImage bufferedImage, int x, int y, int width, int height) {
        var normalizedX = Math.max(x, 0);
        var normalizedY = Math.max(y, 0);
        var normalizedWidth = Math.min(width, bufferedImage.getWidth() - normalizedX);
        var normalizedHeight = Math.min(height, bufferedImage.getHeight() - normalizedY);
        return bufferedImage.getSubimage(normalizedX, normalizedY, normalizedWidth, normalizedHeight);
    }

    private BufferedImage getNotFoundImage(ElementSuperLocator item) {
        BufferedImage bufferedImage = new BufferedImage(600, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D g0 = bufferedImage.createGraphics();
        g0.setColor(Color.red);
        g0.setFont(new Font("SansSerif", Font.BOLD, 12));
        g0.drawString(String.format("Element not visible !!! x:%d, y:%d, width:%d, height:%d", (int) item.getBounds().getX(), (int) item.getBounds().getY(), (int) item.getBounds().getWidth(), (int) item.getBounds().getHeight()), 20, 15);
        g0.drawString(item.getAsString(), 20, 35);
        return bufferedImage;
    }

    /**
     * <p>
     * Updates screenshot in element super locator
     *
     * @param item
     * @param bufferedImage
     */
    public void updatePageElementScreenshot(ElementSuperLocator item, BufferedImage bufferedImage) {
        String b64 = "";
        try {
            Bounds bounds = item.getBounds();
            if (bounds.getSize() != 0) {
                b64 = toBase64(this.getSubImage(bufferedImage, (int) item.getBounds().getX(), (int) item.getBounds().getY(), (int) item.getBounds().getWidth(), (int) item.getBounds().getHeight()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (StringUtils.isEmpty(b64)) {
                b64 = toBase64(this.getNotFoundImage(item));
            }
            item.setScreenshot(JPEG_B64_HEADER + b64);
        }
    }


    /**
     * <p>
     * Get screenshot for visible viewport
     *
     * @return
     */
    public String getViewportScreenshot() {
        String webDriverImage = session.getWebDriver().getScreenshotAs(OutputType.BASE64);
        if (webDriverImage != null) {
            return JPEG_B64_HEADER + toBase64(removeAlphaChannel(parseB64Image(webDriverImage)));
        } else {
            return null;
        }
    }


    /**
     * <p>
     * Get screenshot for entire page
     *
     * @return
     */
    @SneakyThrows
    public BufferedImage getFullPageScreenshot() {
        setScrollTop(0L);
        session.waitUntilComplete();
        BrowserGeometry geometry = getGeometry();
        var original = parseB64Image(session.getContextAnalyzerService().getFullPageShot(0, 0, geometry.getPageWidth().intValue(), geometry.getPageHeight().intValue(), 1));
        return rescaleTo(original, geometry.getPageWidth().intValue(), geometry.getPageHeight().intValue());
    }


    /**
     * <p>
     * Get screenshot for entire page with highlighted element
     *
     * @return
     */
    public String getFullPageScreenshotWithHighlightedElement(ElementSuperLocator elementSuperLocator) {
        Bounds bounds = elementSuperLocator.getBounds();
        BufferedImage bufferedImage = getFullPageScreenshot();
        Graphics2D graphics2D = bufferedImage.createGraphics();
        float thickness = 4;
        graphics2D.setStroke(new BasicStroke(thickness));
        graphics2D.setColor(Color.ORANGE);
        graphics2D.drawRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
        return JPEG_B64_HEADER + toBase64(bufferedImage);
    }


}
