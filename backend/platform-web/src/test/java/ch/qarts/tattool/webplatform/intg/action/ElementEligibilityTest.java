package ch.qarts.tattool.webplatform.intg.action;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.session.DefaultExecutionLogger;
import ch.qarts.tattool.core.domain.session.SimpleLogger;
import ch.qarts.tattool.webplatform.AbstractSessionTest;
import ch.qarts.tattool.webplatform.analysis.AnalyzerSnapshot;
import ch.qarts.tattool.webplatform.command.impl.click.ClickPayload;
import ch.qarts.tattool.webplatform.command.impl.select.SelectPayload;
import ch.qarts.tattool.webplatform.command.impl.select.SelectedItem;
import ch.qarts.tattool.webplatform.command.impl.write.WritePayload;
import ch.qarts.tattool.webplatform.intg.Page;
import ch.qarts.tattool.webplatform.session.WebPlatformSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElementEligibilityTest extends AbstractSessionTest {

    private SimpleLogger simpleLogger = new DefaultExecutionLogger();

    private AnalyzerSnapshot getSnapshot(WebPlatformSession webPlatformSession, Page page) {
        webPlatformSession.forceAnalyzerSnapshot(simpleLogger);
        return webPlatformSession.getAnalyzerSnapshot();
    }


    @SneakyThrows
    private void assertClickable(String id, Page page) {
        WebPlatformSession webPlatformSession = buildSession(page);
        var snapshot = getSnapshot(webPlatformSession, page);
        assertEquals(1, snapshot.getClickable().size());
        String uuid = snapshot.getClickable().get(0).getId();
        ClickPayload clickPayload = ClickPayload.builder().verb("click").elementSuperLocator(ElementSuperLocator.builder().id(uuid).build()).build();
        ClickPayload newPayload = (ClickPayload) webPlatformSession.execute(clickPayload, simpleLogger);
        assertEquals(id, newPayload.getElementSuperLocator().getElementLocatorByType("id").orElseThrow(IllegalStateException::new).getQuery());
    }


    private void assertWritable(String id, Page page) {
        WebPlatformSession webPlatformSession = buildSession(page);
        var snapshot = getSnapshot(webPlatformSession, page);
        assertEquals(1, snapshot.getWritable().size());
        String uuid = snapshot.getWritable().get(0).getId();
        WritePayload payload = WritePayload.builder().verb("write").text("hello").elementSuperLocator(ElementSuperLocator.builder().id(uuid).build()).build();
        WritePayload newPayload = (WritePayload) webPlatformSession.execute(payload, simpleLogger);
        assertEquals(id, newPayload.getElementSuperLocator().getElementLocatorByType("id").orElseThrow(IllegalStateException::new).getQuery());
    }

    private void assertNoItem(Page page) {
        WebPlatformSession webPlatformSession = buildSession(page);
        var snapshot = getSnapshot(webPlatformSession, page);
        assertEquals(0, snapshot.getFilteredItems().size());
    }

    private void assertSelectable(String id, String option, Page page) {
        WebPlatformSession webPlatformSession = buildSession(page);
        var snapshot = getSnapshot(webPlatformSession, page);
        assertEquals(1, snapshot.getSelectable().size());
        String uuid = snapshot.getSelectable().get(0).getId();
        SelectPayload payload = SelectPayload.builder().verb("select").selection(SelectedItem.builder()
                .option(option)
                .elementSuperLocator(ElementSuperLocator.builder().id(uuid).build())
                .build()).build();
        SelectPayload newPayload = (SelectPayload) webPlatformSession.execute(payload, simpleLogger);
        assertEquals(id, newPayload.getSelection().getElementSuperLocator().getElementLocatorByType("id").orElseThrow(IllegalStateException::new).getQuery());
    }


    @Test
    public void textareaShouldBeEditable() {
        Page page = Page.builder()
                .bodyContent("<textarea id='abc'></textarea>")
                .scriptContent("")
                .build();
        assertWritable("abc", page);
    }

    @Test
    public void textareaShouldBeClickable() {
        Page page = Page.builder()
                .bodyContent("<textarea id='xyz'></textarea>")
                .scriptContent("")
                .build();
        assertClickable("xyz", page);
    }


    @Test
    public void inputButtonShouldBeClickable() {
        Page page = Page.builder()
                .bodyContent("<input type='button' id='xyz'>")
                .scriptContent("")
                .build();
        assertClickable("xyz", page);
    }

    @Test
    public void selectShouldBeSelectable() {
        Page page = Page.builder()
                .bodyContent("<select id='xyz'><option>hola</option></select>")
                .scriptContent("")
                .build();
        assertSelectable("xyz", "hola", page);
    }

    @Test
    public void selectShouldBeClickable() {
        Page page = Page.builder()
                .bodyContent("<select id='xyz'><option>hallo</option></select>")
                .scriptContent("")
                .build();
        assertClickable("xyz", page);
    }

    @Test
    public void inputShouldBeClickable() {
        Page page = Page.builder()
                .bodyContent("<input id='xyz' ></input>")
                .scriptContent("")
                .build();
        assertClickable("xyz", page);
    }

    @Test
    public void inputTextShouldBeEditable() {
        Page page = Page.builder()
                .bodyContent("<input id='xyz' type='text'></input>")
                .scriptContent("")
                .build();

        assertWritable("xyz", page);
    }

    @Test
    public void inputEmailShouldBeEditable() {
        Page page = Page.builder()
                .bodyContent("<input id='xyz' type='email'></input>")
                .scriptContent("")
                .build();
        assertWritable("xyz", page);
    }

    @Test
    public void inputPasswordShouldBeEditable() {
        Page page = Page.builder()
                .bodyContent("<input id='xyz' type='password'></input>")
                .scriptContent("")
                .build();
        assertWritable("xyz", page);
    }

    @Test
    public void clickShouldBeClickable() {
        Page page = Page.builder()
                .bodyContent("<div id='xyz'>hello world</div>")
                .scriptContent("var element = document.getElementById('xyz'); element.addEventListener('click', function () { console.log('hello'); }, false);")
                .build();
        assertClickable("xyz", page);
    }

    @Test
    public void buttonShouldBeClickable() {
        Page page = Page.builder()
                .bodyContent("<button id='xyz' >hellow world</button>")
                .scriptContent("")
                .build();
        assertClickable("xyz", page);

    }


    @Test
    public void linkShouldBeClickable() {
        // NOTE link with href='#' not clickable ...
        Page page = Page.builder()
                .bodyContent("<a href='fwef' id='xyz' >hello world</a>")
                .scriptContent("")
                .build();
        assertClickable("xyz", page);
    }

    @Test
    public void newElementShouldBeClickable() {

        Page page = Page.builder()
                .bodyContent("")
                .scriptContent("var child = document.createElement('BUTTON'); child.id = 'xyz';document.body.appendChild(child);")
                .build();
        assertClickable("xyz", page);

    }

    @Test
    public void newElementChildShouldBeClickable() {

        Page page = Page.builder()
                .bodyContent("")
                .scriptContent("var div = document.createElement('DIV'); var child = document.createElement('BUTTON'); child.id = 'xyz'; div.appendChild(child);document.body.appendChild(div);")
                .build();
        assertClickable("xyz", page);

    }


    @Test
    public void inputRadioShouldBeClickable() {

        Page page = Page.builder()
                .bodyContent("<input type='radio' id='axx'>")
                .scriptContent("")
                .build();
        assertClickable("axx", page);

    }


    @Test
    public void shouldNotBeEligibleWhenInvisible1() {
        Page page = Page.builder()
                .bodyContent("<input style='visibility:hidden' type='radio' id='axx'>")
                .scriptContent("")
                .build();
        assertNoItem(page);
    }

    @Test
    public void shouldNotBeEligibleWhenInvisible2() {
        Page page = Page.builder()
                .bodyContent("<input style='display:none' type='radio' id='axx'>")
                .scriptContent("")
                .build();
        assertNoItem(page);
    }


    // TODO how to test ?
    @Disabled
    @Test
    public void shouldNotBeEligibleWhenOverlayed() {
        Page page = Page.builder()
                .bodyContent("<input style='z-index:-1' type='radio' id='axx'><div style='position:fixed;width:100%;height:100%;z-index:1'> some text here </div> ")
                .scriptContent("")
                .build();
        assertNoItem(page);
    }


}
