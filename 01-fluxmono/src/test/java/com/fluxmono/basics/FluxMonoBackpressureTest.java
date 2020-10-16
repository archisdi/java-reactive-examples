package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoBackpressureTest {
    @Test
    public void backPressureTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressureImpTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(
                e -> System.out.println("Element is : " + e),
                e -> System.err.println("Exception is : " + e),
                () -> System.out.println("Completed"),
                s -> s.request(2) // backpressure, only takes 2 elements, will not emit onComplete
        );
    }

    @Test
    public void backPressureCancelTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(
                e -> System.out.println("Element is : " + e),
                e -> System.err.println("Exception is : " + e),
                () -> System.out.println("Completed"),
                Subscription::cancel // will not do anything
        );
    }

    @Test
    public void backPressureCustomizedTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        // manual control by using BaseSubscriber override
        integerFlux.subscribe(
                new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnNext(Integer value) {
                        request(1);
                        System.out.println("Value received : " + value);
                        if (value == 4) {
                            cancel();
                        }
                    }
                }
        );
    }
}
