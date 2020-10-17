package com.fluxmono.v1;

import com.fluxmono.constant.ItemConstant;
import com.fluxmono.document.Item;
import com.fluxmono.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ItemControllerTest {

    List<Item> items = List.of(
            new Item(null, "Samsung Tv", 43.34),
            new Item(null, "Iphone", 643.34),
            new Item("WOW", "Airpod", 453.3),
            new Item(null, "Broiler", 423.3),
            new Item(null, "Macbook Pro", 643.35),
            new Item(null, "Monitor", 413.63)
    );

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemRepository::save)
                .doOnNext(System.out::println)
                .blockLast(); // only in test cases
    }

    @Test
    public void itemListTest() {
        webTestClient.get().uri(ItemConstant.ITEM_ENDPOINT_v1)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(6);
    }

    @Test
    public void itemListTest2() {
        webTestClient.get().uri(ItemConstant.ITEM_ENDPOINT_v1)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(6)
                .consumeWith(res -> {
                   List<Item> items = res.getResponseBody();
                    assert items != null;
                    items.forEach(Assertions::assertNotNull);
                });
    }

    @Test
    public void itemDetailTest() {
        webTestClient.get().uri(ItemConstant.ITEM_ENDPOINT_v1 + "/{id}", "WOW")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .consumeWith(Assertions::assertNotNull);
    }

    @Test
    public void itemCreateTest() {
        Item item = new Item(null, "hmm", 420.0);

        webTestClient.post().uri(ItemConstant.ITEM_ENDPOINT_v1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.description", item.getDescription());
    }

    @Test
    public void deleteItemTest() {
        webTestClient.delete().uri(ItemConstant.ITEM_ENDPOINT_v1 + "/{id}", "WOW")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItemTest() {
        Item item = new Item(null, "hmm", 420.0);

        webTestClient.put().uri(ItemConstant.ITEM_ENDPOINT_v1 + "/{id}", "WOW")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", item.getPrice());
    }

    @Test
    public void updateItemNotFoundTest() {
        Item item = new Item(null, "hmm", 420.0);

        webTestClient.put().uri(ItemConstant.ITEM_ENDPOINT_v1 + "/{id}", "HMM")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}