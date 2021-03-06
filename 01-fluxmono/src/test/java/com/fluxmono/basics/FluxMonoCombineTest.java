package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.Date;

public class FluxMonoCombineTest {
    @Test
    public void combineMergeTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFl = Flux.merge(flux1, flux2);
        // concat

        StepVerifier.create(mergedFl)
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineMergeDelayTest() {

        VirtualTimeScheduler.getOrSet();

        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFl = Flux.concat(flux1, flux2);

        StepVerifier.withVirtualTime(() -> mergedFl)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineZipTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFl = Flux.zip(flux1, flux2, String::concat);

        StepVerifier.create(mergedFl)
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

}
