package ch.qarts.tattool.core.domain.session;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DefaultExecutionLogger implements SimpleLogger {

    @Getter
    private List<String> log = new ArrayList<>();

    private void addEvent(Class<?> cls, String message, Throwable t) {
        log.add(String.format("%s : %s : %s", cls.getSimpleName(), message, t != null ? t.getMessage() : ""));
    }

    @Override
    public void info(Class<?> cls, String message) {
        addEvent(cls, message, null);
    }

    @Override
    public void warning(Class<?> cls, String message, Throwable t) {
        addEvent(cls, message, null);
    }

    @Override
    public void error(Class<?> cls, String message, Throwable t) {
        addEvent(cls, message, t);
    }
}
