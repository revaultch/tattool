package ch.qarts.tattool.webplatform.intg.screenshot;

import ch.qarts.tattool.core.domain.element.ElementLocator;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.Objects;

import static ch.qarts.tattool.webplatform.locator.Utils.completeIfNeeded;
import static ch.qarts.tattool.webplatform.util.ImageUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class GeometryTest extends AbstractSessionTest {


    private static String stripB64Header(String b64WithHeader) {
        return b64WithHeader.replaceAll(JPEG_B64_HEADER, "");
    }


    @Test
    public void shouldTakeCorrectElementScreenshot() {

        var session = buildSession("<div style='height:100px;width:100%;position:fixed;background-color:#000000;z-index:1000'>fixed header</div>", "for (var i=0;i<200;i++) { document.write('<a id='' + i + '' href='#'' + i + ''' style='position:absolute;left:10px;top:'' + (i * 20) + ''px'> ---'' + i +  '' </a>''); }");

        for (ElementSuperLocator elementSuperLocator : session.getAnalyzerSnapshot().getClickable()) {
            var l = completeIfNeeded(session, elementSuperLocator);
            assertTrue(compareElement(l) > 99.0, () -> String.format("Failed comparing image %s was %s", l.getElementLocatorByType("id").orElseThrow().getQuery(), compareElement(l)));
        }


    }

    private float compareElement(ElementSuperLocator elementSuperLocator) {
        ElementLocator elementLocator = elementSuperLocator.getLocators().stream().filter(l -> l.getType().equals("id")).findFirst().orElseThrow();
        String resourceName = Objects.requireNonNull(this.getClass().getClassLoader().getResource("screenshot/png/" + elementLocator.getQuery() + ".png")).toString();
        return compareImage(parseB64Image(stripB64Header(elementSuperLocator.getScreenshot())), parseB64Image(fromFile(resourceName)));
    }


    public float compareImage(BufferedImage biA, BufferedImage biB) {

        float percentage = 0;
        try {
            // take buffer data from both image files //
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();

            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {

                for (int i = 0; i < sizeA; i++) {

                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }

                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size A: " + sizeA + " B: " + sizeB);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to compare images");
        }
        return percentage;
    }

    // NOTE : used to save files
    /*
    private void saveToFile(ElementSuperLocator elementSuperLocator) {
        ElementLocator elementLocator = elementSuperLocator.getLocators().stream().filter(l -> l.getType().equals("id")).findFirst().orElseThrow();
        toFile("/Users/borjafernandez/projects/qarts/qarts-studio/backend/web-target/src/test/resources/screenshot/png/" + elementLocator.getQuery() + ".png", elementSuperLocator.getScreenshot().replaceAll("data:image/png;base64,", ""));
    }*/
}
