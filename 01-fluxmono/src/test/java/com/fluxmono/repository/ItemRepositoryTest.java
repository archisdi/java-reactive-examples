package com.fluxmono.repository;

import com.fluxmono.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest

class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    List<Item> items = List.of(
            new Item(null, "Samsung Tv", 43.34),
            new Item(null, "Iphone", 643.34),
            new Item(null, "Airpod", 453.3),
            new Item("id-123", "Washing Machine", 433.63),
            new Item(null, "Broiler", 423.3),
            new Item(null, "Macbook Pro", 643.35),
            new Item(null, "Monitor", 413.63)
    );

    @BeforeEach
    public void setUp() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemRepository::save)
                .doOnNext(System.out::println)
                .blockLast(); // only in test cases
    }

    @Test
    public void getAllItemsTest() {
        Flux<Item> itemFlux = itemRepository.findAll();

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    public void getItemByIdTest() {
        Mono<Item> itemMono = itemRepository.findById("id-123");

        StepVerifier.create(itemMono)
                .expectSubscription()
                .expectNextMatches(i -> i.getDescription().equals("Washing Machine"))
                .verifyComplete();
    }

    @Test
    public void getItemByDescriptionTest() {
        Flux<Item> itemFlux = itemRepository.findByDescription("Broiler");

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextMatches(i -> i.getPrice().equals(423.3))
                .verifyComplete();
    }

    @Test
    public void saveItemTest() {
        Item item = new Item("DEF", "Google Home", 300.0);
        Mono<Item> itemMono = itemRepository.save(item);

        StepVerifier.create(itemMono)
                .expectSubscription()
                .expectNextMatches(i -> "DEF".equals(i.getId()))
                .verifyComplete();
    }

    @Test
    public void updateItemTest() {
        Double newPrice = 69.00;
        Flux<Item> itemFlux = itemRepository.findByDescription("Broiler")
                .map(i -> {
                    i.setPrice(newPrice);
                    return i;
                })
                .flatMap(itemRepository::save);

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextMatches(i -> i.getPrice().equals(69.00))
                .verifyComplete();

    }

    @Test
    public void deleteItemTest() {
        Mono<Void> mono = itemRepository.findById("id-123")
                .flatMap(itemRepository::delete);

        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }
}