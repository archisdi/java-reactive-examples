package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux
                .just("Archie", "Atrie", "Isdiningrat")
                // .concatWith(Flux.error(new RuntimeException()))
                // .concatWith(Flux.just("Angga")) // will not received
                .log(); // print logs

        // process subscription and catch error
        stringFlux.subscribe(
                System.out::println, // onNext
                e -> System.err.println("Exception is : " + e), // onError
                () -> System.out.println("Completed !") // onComplete
        );
    }

    @Test
    public void fluxTestElementWithoutError() {
        Flux<String> stringFlux = Flux
                .just("Archie", "Atrie", "Isdiningrat").log();

        // assert every element in flux
        StepVerifier.create(stringFlux)
                .expectNext("Archie")
                .expectNext("Atrie")
                .expectNext("Isdiningrat")
                .verifyComplete();
    }

    @Test
    public void fluxTestElementWithError() {
        Flux<String> stringFlux = Flux
                .just("Archie", "Atrie", "Isdiningrat")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .log();

        // assert every element in flux
        StepVerifier.create(stringFlux)
                .expectNext("Archie", "Atrie", "Isdiningrat")
                // .expectError()
                .expectErrorMessage("Error Occurred")
                .verify();
    }

    @Test
    public void fluxTestCountWithError() {
        Flux<String> stringFlux = Flux
                .just("Archie", "Atrie", "Isdiningrat")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .log();

        // assert every element in flux
        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .verifyError();
    }
}
