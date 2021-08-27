package ch.qarts.tattool.core.domain.execution;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ExecutableTestExecutionTest {


    private ExecutableTestExecutionService testedExecutableTestExecutionService;

// shouldFailWhenPayloadExecutionFails
// shouldSucceedWhenPayloadExecutionSucceeds
// shouldFailWhenSessionCreationFails


    @Test
    public void shouldFailWhenPayloadExecutionFails() throws Exception {

        CompletableFuture<String> c1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(12000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello1";
        });

        CompletableFuture<String> c2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new IllegalStateException();
            // return "hello2";
        });

        CompletableFuture<String> c3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello3";
        });


        var list = Arrays.asList(c1, c2, c3);
/*
        var handler = new BiFunction<String, Throwable, String>() {
            @Override
            public String apply(String s, Throwable throwable) {
                if (throwable == null) {
                    System.out.println(s);
                    return s.toUpperCase(Locale.ROOT);
                } else {
                    System.out.println("error for " + s);
                    return "ERROR ...";
                }
            }
        };


        var executionFuture = FutureUtils
                .allOf(list, handler);

        executionFuture.join().forEach(System.err::println);
        ;
*/

        /*
        var newList = list.stream()
                .map(c -> c.whenComplete((s, x) -> System.err.println(s))).collect(Collectors.toList());


        newList.stream().map(CompletableFuture::join).forEach(System.err::println);*/

    }

    // shouldPerformAllItemsEvenIfOneFails

}
