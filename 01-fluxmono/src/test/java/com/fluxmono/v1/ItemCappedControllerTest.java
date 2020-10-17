package com.fluxmono.v1;

import com.fluxmono.constant.ItemConstant;
import com.fluxmono.document.ItemCapped;
import com.fluxmono.repository.ItemCappedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ItemCappedControllerTest {

    @Autowired
    ItemCappedRepository itemCappedRepository;

    @Autowired
    ReactiveMongoOperations mongoOperations;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        mongoOperations.dropCollection(ItemCapped.class)
                .then(mongoOperations.createCollection(
                        ItemCapped.class,
                        CollectionOptions.empty().maxDocuments(20).size(50000).capped()
                )).subscribe();

        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "Random Item " + i, 100.0 + i))
                .take(5);

        itemCappedRepository.insert(itemCappedFlux)
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    void getItemsStream() {
        Flux<ItemCapped> itemCappedFlux = webTestClient.get().uri(ItemConstant.ITEM_STREAM_ENDPOINT_v1)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ItemCapped.class)
                .getResponseBody()
                .take(5);

        StepVerifier.create(itemCappedFlux)
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }
}