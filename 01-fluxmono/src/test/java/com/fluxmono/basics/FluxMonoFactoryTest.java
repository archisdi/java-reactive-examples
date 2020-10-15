package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import java.util.Optional;
import java.util.function.Supplier;

public class FluxMonoFactoryTest {
    List<String> names = List.of("adam", "anna", "jack", "jenny");

    @Test
    public void fluxIterableTest() {
        Flux<String> fluxNames = Flux.fromIterable(names);
        // fromArray
        // fromStream
        // fromRange

        fluxNames.subscribe(System.out::println);

        StepVerifier.create(fluxNames)
                .expectNext("adam")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void monoEmptyTest() {
        Mono<String> monoName = Mono.justOrEmpty(Optional.empty());
        StepVerifier.create(monoName).verifyComplete();
    }

    @Test
    public void monoSupplierTest() {
        Supplier<String> sup = () -> "Hola Amigos";

        Mono<String> fromSup = Mono.fromSupplier(sup);
        fromSup.subscribe(System.out::println);

        StepVerifier.create(fromSup)
                .expectNext("Hola Amigos")
                .verifyComplete();
    }
}
