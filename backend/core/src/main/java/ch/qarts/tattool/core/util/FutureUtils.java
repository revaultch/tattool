package ch.qarts.tattool.core.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FutureUtils {

    public static <T> CompletableFuture<List<T>> allOf(final List<CompletableFuture<T>> futuresList) {
        return CompletableFuture.allOf(futuresList.toArray(CompletableFuture<?>[]::new)).thenApply(v -> futuresList.stream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
    }


}
