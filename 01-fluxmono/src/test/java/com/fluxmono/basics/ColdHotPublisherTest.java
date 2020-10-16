package com.fluxmono.basics;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        stringFlux.subscribe(e -> System.out.println("Subs-one : " + e)); // emits a value from beginning

        Thread.sleep(2000);

        stringFlux.subscribe(e -> System.out.println("Subs-two : " + e)); // emits a value from beginning

        Thread.sleep(4000);

    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        // will continue data stream if theres a new subscriber in future
        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(e -> System.out.println("Subs-one : " + e));

        Thread.sleep(2000);

        connectableFlux.subscribe(e -> System.out.println("Subs-two : " + e));

        Thread.sleep(4000);

    }

}
