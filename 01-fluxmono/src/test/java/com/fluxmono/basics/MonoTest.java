package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {
    @Test
    public void monoTest() {
        Mono<String> monoString = Mono.just("Archie");

        monoString
                .map(String::toUpperCase)
                .subscribe(System.out::println);

        StepVerifier
                .create(monoString)
                .expectNext("Archie")
                .verifyComplete();
    }

    @Test
    public void monoTestWithError() {
        StepVerifier
                .create(Mono.error(new RuntimeException("Exception Occurred")))
                .expectError(RuntimeException.class)
                .verify();
    }
}
