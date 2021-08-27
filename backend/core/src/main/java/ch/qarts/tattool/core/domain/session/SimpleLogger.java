package ch.qarts.tattool.core.domain.session;

import java.util.List;

public interface SimpleLogger {
    enum Level {
        ERROR,
        WARNING,
        INFO
    }

    void info(Class<?> cls, String message);

    void warning(Class<?> cls, String message, Throwable t);

    void error(Class<?> cls, String message, Throwable t);

    List<String> getLog();
}
