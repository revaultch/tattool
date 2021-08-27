package ch.qarts.tattool.core.domain.session;


import lombok.Data;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SessionConfiguration {

    private Map<String, String> items = new ConcurrentHashMap<>();

    private URL url;

    private Object options;

    private boolean debug = false;

    private int sessionCreationRetries = 3;

    private int sessionCreationRetryDelayInSeconds = 10;


}
