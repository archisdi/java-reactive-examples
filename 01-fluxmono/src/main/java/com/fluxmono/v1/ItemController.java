package com.fluxmono.v1;

import com.fluxmono.constant.ItemConstant;
import com.fluxmono.document.Item;
import com.fluxmono.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping(value = ItemConstant.ITEM_ENDPOINT_v1)
    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping(ItemConstant.ITEM_ENDPOINT_v1 + "/{id}")
    public Mono<ResponseEntity<Item>> getItemDetail(@PathVariable String id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ItemConstant.ITEM_ENDPOINT_v1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@Validated @RequestBody Item item) {
        return itemRepository.save(item);
    }

    @DeleteMapping(ItemConstant.ITEM_ENDPOINT_v1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return itemRepository.deleteById(id);
    }

    @PutMapping(ItemConstant.ITEM_ENDPOINT_v1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(
            @PathVariable String id,
            @RequestBody Item updateItem
    ) {
        return itemRepository.findById(id)
                .flatMap(i -> {
                    i.setPrice(updateItem.getPrice());
                    i.setDescription(updateItem.getDescription());
                    return itemRepository.save(i);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

