package ch.qarts.tattool.client.junit.runner;

import ch.qarts.tattool.core.domain.run.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Tattool {
    String apiUri();

    String clientUri();

    String reference();

    Type type();
}
