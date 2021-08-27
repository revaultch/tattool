package ch.qarts.tattool.core.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryUtils {
    @SneakyThrows
    public static <T> T retry(Supplier<T> supplier, int times, long delay) {
        var counter = 0;
        while (counter < times) {
            try {
                return supplier.get();
            } catch (Throwable t) {
                log.warn("Retry failed " + t.getMessage());
                counter++;
                if (counter < times) {
                    Thread.sleep(delay);
                }
            }
        }
        throw new IllegalStateException(String.format("Unable to resolve supplier after %s attempts", times));
    }
}
