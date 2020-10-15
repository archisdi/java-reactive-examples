package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxMonoTransformTest {
    List<String> names = List.of("adam", "anna", "jack", "jenny");

    @Test
    public void filterTest() {
        Flux<String> nameFl = Flux.fromIterable(names)
                .filter(s ->  s.startsWith("a"));

        StepVerifier.create(nameFl)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void mapTest() {
        Flux<Integer> nameFl = Flux.fromIterable(names)
                .map(String::length)
                .repeat(1); // repeat all items

        StepVerifier.create(nameFl)
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    public void flatMapTest() {
        // if you have to external service for every elements that returns a flux
        Flux<String> stringFl = Flux.fromIterable(names)
                .window(2)
                // .flatMap(s -> s.map(this::converToList).subscribeOn(parallel()))
                .flatMapSequential(s -> s.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier.create(stringFl)
                .expectNextCount(8)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return List.of(s, "WOW");
    }
}
