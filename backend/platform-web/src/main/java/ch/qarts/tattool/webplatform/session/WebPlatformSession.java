package ch.qarts.tattool.webplatform.session;

import ch.qarts.tattool.core.domain.command.ContextData;
import ch.qarts.tattool.core.domain.command.Payload;
import ch.qarts.tattool.core.domain.command.descriptor.CommandDescriptor;
import ch.qarts.tattool.core.domain.execution.reporting.PayloadInstanceExecutionReport;
import ch.qarts.tattool.core.domain.session.SessionConfiguration;
import ch.qarts.tattool.core.domain.session.SessionProxy;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.analysis.AnalyzerSnapshot;
import ch.qarts.tattool.webplatform.analysis.ContextAnalyzerServiceImpl;
import ch.qarts.tattool.webplatform.analysis.ScreenshotService;
import ch.qarts.tattool.webplatform.command.WebPlatformExecutableCommand;
import ch.qarts.tattool.webplatform.commandfactory.CommandDescriptorMapper;
import ch.qarts.tattool.webplatform.commandfactory.CommandFactory;
import ch.qarts.tattool.webplatform.commandfactory.CommandListHolder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode
@Data
@Builder
@AllArgsConstructor
@Slf4j
public class WebPlatformSession implements SessionProxy {

    private String id;

    private ThreadLocal<RemoteWebDriver> webDriver;

    private ContextAnalyzerServiceImpl contextAnalyzerService;

    private AnalyzerSnapshot analyzerSnapshot;

    private SessionConfiguration sessionConfiguration;

    private boolean isAlertOpen;

    public RemoteWebDriver getWebDriver() {
        return this.webDriver.get();
    }

    @Override
    public void close() {
        try {
            this.webDriver.get().quit();
        } catch (Exception e) {
            log.error("Error while cleaning up ", e);
        }
    }

    @SneakyThrows
    @Override
    public Payload execute(Payload payload, SimpleLogger simpleLogger) {
        return doExecute(payload, simpleLogger);
    }

    // TODO review
    @SneakyThrows
    private Payload doExecute(Payload payload, SimpleLogger simpleLogger) {
        PayloadInstanceExecutionReport report = new PayloadInstanceExecutionReport();
        CommandDescriptor commandDescriptor = CommandDescriptorMapper.getDescriptor(payload.getVerb());
        var command = (WebPlatformExecutableCommand<? extends Payload>)
                commandDescriptor.getCommandType().getDeclaredConstructor().newInstance();
        var method = command.getClass().getDeclaredMethod("execute", WebPlatformSession.class, payload.getClass(), SimpleLogger.class);
        return (Payload) method.invoke(command, this, payload, simpleLogger);
    }


    @Override
    public ContextData commandContextData(String commandName) {
        forceAnalyzerSnapshot(null);
        return CommandFactory.create(commandName).contextData(this);
    }


    @Override
    public String getScreenshot() {
        this.waitUntilComplete();
        return new ScreenshotService(this).getViewportScreenshot();
    }

    @Override
    public List<CommandDescriptor> getAvailableCommands() {
        forceAnalyzerSnapshot(null);
        return CommandListHolder.getAvailableCommands().stream().filter(this::filterCommandInContext).map(CommandDescriptorMapper::map).collect(Collectors.toList());
    }

    private boolean filterCommandInContext(WebPlatformExecutableCommand<? extends Payload> webPlatformCommand) {
        try {
            return webPlatformCommand.isEligible(this);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }


    public void forceAnalyzerSnapshot(SimpleLogger simpleLogger) {
        this.analyzerSnapshot = contextAnalyzerService.buildAnalyzerSnapshot(this, simpleLogger);
    }


    public void waitUntilComplete() {
        WebDriverWait wait = new WebDriverWait(this.getWebDriver(), Duration.ofSeconds(30));
        wait.until((ExpectedCondition<Boolean>) wdriver -> ((JavascriptExecutor) wdriver).executeScript(
                "return document.readyState"
        ).equals("complete"));
    }


}
