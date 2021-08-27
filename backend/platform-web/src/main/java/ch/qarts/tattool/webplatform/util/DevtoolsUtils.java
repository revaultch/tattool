package ch.qarts.tattool.webplatform.util;

import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.devtools.CdpVersionFinder;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.SeleniumCdpConnection;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Map;
import java.util.function.Supplier;

public class DevtoolsUtils {

    @SneakyThrows
    public static DevTools getDevtools(RemoteWebDriver driver, URL url) {
        // TODO remove when there is a viable / documented option from selenium
        Supplier<Capabilities> fixCdp = () -> {
            Capabilities other = new DesiredCapabilities();
            var seCdp = (String) driver.getCapabilities().getCapability("se:cdp");
            var sessionPos = seCdp.indexOf("/session");
            var seCdpFixed = String.format("ws://%s:%s%s", url.getHost(), url.getPort(), seCdp.substring(sessionPos));
            return driver.getCapabilities().merge(new DesiredCapabilities(Map.of("se:cdp", seCdpFixed)));
        };
        var cdpInfo = new CdpVersionFinder().match(driver.getCapabilities().getBrowserVersion()).orElseThrow(IllegalAccessException::new);
        return new DevTools(cdpInfo::getDomains, SeleniumCdpConnection.create(fixCdp.get()).orElseThrow(IllegalAccessException::new));
    }

    @SneakyThrows
    public static DevTools getDevtools(RemoteWebDriver driver) {
        var cdpInfo = new CdpVersionFinder().match(driver.getCapabilities().getBrowserVersion()).orElseThrow(IllegalStateException::new);
        return new DevTools(cdpInfo::getDomains, SeleniumCdpConnection.create(driver).orElseThrow(IllegalAccessException::new));
    }

}
