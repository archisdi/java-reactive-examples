package com.fluxmono.basics;

import com.fluxmono.error.CustomError;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoErrorTest {

    @Test
    public void errorHandlingeResumeTest() {
        Flux<String> stringFlux = Flux
                .just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e -> {
                    System.out.println("Exception is : " + e);
                    return Flux.just();
                });
                // .onErrorReturn("default")

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

    @Test
    public void errorHandlingCustomTest() {
        Flux<String> stringFlux = Flux
                .just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomError::new);

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomError.class)
                .verify();
    }

    @Test
    public void errorHandlingRetryTest() {
        Flux<String> stringFlux = Flux
                .just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomError::new)
                .retry(2);

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomError.class)
                .verify();
    }

}
