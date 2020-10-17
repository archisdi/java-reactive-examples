package com.fluxclient.controller;

import com.fluxclient.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;

@RestController
public class ItemClientController {
    WebClient webClient = WebClient.create("http://127.0.0.1:8080");

    @GetMapping("/client/retrieve")
    public Flux<Item> getAllItemsRetrieve() {
        return webClient.get().uri("/v1/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log();
    }

    @GetMapping("/client/exchange")
    public Flux<Item> getAllItemsExchange() {
        return webClient.get().uri("/v1/items")
                .exchange()
                .flatMapMany(res -> res.bodyToFlux(Item.class))
                .log();
    }

    @GetMapping("/client/retrieve/{id}")
    public Mono<Item> getItemRetrieve(@PathVariable String id) {
        return webClient.get().uri("/v1/items/{id}", id)
                .retrieve()
                .bodyToMono(Item.class)
                .log();
    }

    @GetMapping("/client/exchange/{id}")
    public Mono<Item> getItemExchange(@PathVariable String id) {
        return webClient.get().uri("/v1/items/{id}", id)
                .exchange()
                .flatMap(res -> res.bodyToMono(Item.class))
                .log();
    }

    @PostMapping("/client/create")
    public Mono<Item> createItem(@RequestBody Item item) {
        return webClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log();
    }

    @PutMapping("/client/update/{id}")
    public Mono<Item> updateItem(
            @PathVariable String id,
            @RequestBody Item item
    ) {
        return webClient.put().uri("/v1/items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log();
    }

    @DeleteMapping("/client/delete/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return webClient.delete().uri("/v1/items/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .log();
    }
}
