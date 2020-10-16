package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxMonoTimeTest {
    @Test
    public void infiniteSeqTest() throws InterruptedException {
        Flux<Integer> infiniteFl = Flux
                .interval(Duration.ofMillis(200))
                .map(Long::intValue)
                .take(3)
                .log();

//        infiniteFl.subscribe(e -> System.out.println("Value is " + e));

        StepVerifier.create(infiniteFl)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }
}
