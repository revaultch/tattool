package ch.qarts.tattool.core.domain.command.descriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Renderer {
    public String value() default "";
}
