package ch.qarts.tattool.webplatform.locator;

import ch.qarts.tattool.core.domain.element.ElementLocator;
import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class Utils {

    @SneakyThrows
    public static WebElement moveTo(WebDriver driver, WebElement webElement) {
        /*
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        String cssClass = webElement.getAttribute("class");
        executor.executeScript("arguments[0].scrollIntoView({block: 'center'});arguments[0].className = '';", webElement);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
        try {
            return wait.until(
                    ExpectedConditions.visibilityOf(webElement));
        } finally {
            executor.executeScript(String.format("arguments[0].className = '%s';", cssClass), webElement);
        }*/
        Actions builder = new Actions(driver);
        builder.moveToElement(webElement).perform();
        return webElement;
    }


    public static ElementSuperLocator completeIfNeeded(WebPlatformSession session, ElementSuperLocator elementSuperLocator) {
        String uuid = elementSuperLocator.getId();
        if (StringUtils.isNotEmpty(uuid)) {
            String locatorsStr = (String) ((JavascriptExecutor) session.getWebDriver()).executeScript(String.format("return JSON.stringify(window.ttAnalysisHolder.buildLocators('%s'));", uuid));
            List<ElementLocator> locators = new Gson().fromJson(locatorsStr, new TypeToken<List<ElementLocator>>() {
            }.getType());
            elementSuperLocator.setLocators(locators);
            elementSuperLocator.setId(null);
        }
        return elementSuperLocator;
    }


}
