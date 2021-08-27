package ch.qarts.tattool;

import ch.qarts.tattool.core.domain.element.ElementSuperLocator;
import ch.qarts.tattool.core.domain.platform.Platform;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebPlatformDefIntgTests {

    @Autowired
    private TestRestTemplate testRestTemplate;


    public void screenshot(String sessionId) {
        ResponseEntity<String> response31 =
                testRestTemplate.getForEntity(
                        String.format("/v1/platforms/%s/sessions/%s/screenshot", "web-chrome-headless", sessionId),
                        String.class);

        System.err.println(response31.getBody());
    }

    @Test
    void shouldConvertGoogleUnits() throws Exception {

        // list all platforms
        ResponseEntity<Platform[]> response =
                testRestTemplate.getForEntity(
                        "/v1/platforms",
                        Platform[].class);


        Platform[] platforms = response.getBody();
        assertEquals(1, platforms.length);
        // take web-platform
        Platform webPlatform = platforms[0];
        assertEquals("web-chrome-headless", webPlatform.getId());

        //    @PostMapping(value = "/v1/platforms/{platformId}/session", produces = MediaType.APPLICATION_JSON_VALUE)
        // create a session (1)
        ResponseEntity<String> response2 =
                testRestTemplate.postForEntity(
                        String.format("/v1/platforms/%s/sessions", webPlatform.getId()),
                        null,
                        String.class);

        assertNotNull(response2.getBody());

        String sessionId = response2.getBody();


        // navigate to google

        //     @PostMapping(value = "/v1/platforms/{platformId}/sessions/{sessionId}/command", consumes = MediaType.APPLICATION_JSON_VALUE)


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        ResponseEntity<Void> response3 = testRestTemplate.exchange(String.format("/v1/platforms/%s/sessions/%s/command", webPlatform.getId(), sessionId),
                HttpMethod.POST,
                new HttpEntity<>(String.format("{\"verb\" : \"navigateTo\", \"url\" : \"%s\"}", "https://www.google.ch"), headers), Void.class);

                /*
                testRestTemplate.postForEntity(
                        String.format("/v1/platforms/%s/sessions/%s/command", webPlatform.getId(), sessionId),
                        String.format("{\"verb\" : \"navigateTo\", \"url\" : \"%s\"}", "https://www.google.ch"),
                        Void.class);*/


//        text/plain;charset=UTF-8


        assertTrue(response3.getStatusCode().is2xxSuccessful());


        screenshot(sessionId);


        // go to unit converter
        // - list "writable"


        ResponseEntity<ElementSuperLocator[]> response4 =
                testRestTemplate.getForEntity(
                        String.format("/v1/platforms/%s/sessions/%s/elements?commandType=%s", "web-chrome-headless", sessionId, "write"),
                        ElementSuperLocator[].class);


        assertTrue(response4.getStatusCode().is2xxSuccessful());

        assertEquals(2, response4.getBody().length);

        // - enter unit converter in "writable" search field

        ResponseEntity<Void> response5 = testRestTemplate.exchange(String.format("/v1/platforms/%s/sessions/%s/command", webPlatform.getId(), sessionId),
                HttpMethod.POST,
                new HttpEntity<>(String.format("{\"verb\" : \"write\", \"text\" : \"unit converter\", \"elementSuperLocator\" : {\"id\" : \"xxx\", \"locators\" : [{\"type\" : \"id\", \"query\" : \"\"}, {\"type\" : \"xpath\", \"query\" : \"%s\"}] }}", "//FORM[@id='tsf']/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/INPUT[1]"), headers),
                Void.class);

        assertTrue(response5.getStatusCode().is2xxSuccessful());


        // take screenshot

        screenshot(sessionId);

        // - press enter
        /*
        ResponseEntity<Void> response6 = testRestTemplate.exchange(String.format("/v1/platforms/%s/sessions/%s/command", webPlatform.getId(), sessionId),
                HttpMethod.POST,
                new HttpEntity<>(String.format("{\"verb\" : \"press\", \"elementSuperLocator\" : {\"id\" : \"xxx\", \"locators\" : [{\"type\" : \"id\", \"query\" : \"\"}, {\"type\" : \"xpath\", \"query\" : \"%s\"}] }, \"key\" : \"%s\"}", "//FORM[@id='tsf']/DIV[2]/DIV[1]/DIV[1]/DIV[1]/DIV[2]/INPUT[1]", Keys.ENTER), headers),
                Void.class);*/

//        assertTrue(response6.getStatusCode().is2xxSuccessful());

        screenshot(sessionId);

        // get all selectable
        // select yard in (from) selector


        // get all selectable
        // select foot in (to) selector

        // set 20 yard in (from) selector

        // assert 60 foot


        System.err.println();

    }

}
