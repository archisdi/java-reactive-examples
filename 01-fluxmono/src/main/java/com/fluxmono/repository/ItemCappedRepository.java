package com.fluxmono.repository;

import com.fluxmono.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ItemCappedRepository extends ReactiveMongoRepository<ItemCapped, String> {
    @Tailable
    Flux<ItemCapped> findItemsBy();
}
