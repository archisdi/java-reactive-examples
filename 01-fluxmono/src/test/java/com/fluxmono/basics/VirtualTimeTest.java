package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTest {
    @Test
    public void withoutVirtualTime() {
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3);

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    public void withVirtualTime() {
        // enable virtual time
        VirtualTimeScheduler.getOrSet();
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3);

        StepVerifier.withVirtualTime(() -> longFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3)) // await 3 seconds virtually
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }
}
