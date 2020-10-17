package com.fluxmono.initializer;

import com.fluxmono.document.Item;
import com.fluxmono.document.ItemCapped;
import com.fluxmono.repository.ItemCappedRepository;
import com.fluxmono.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class ItemDataInitializer implements CommandLineRunner { // will run when app start

    List<Item> items = List.of(
            new Item(null, "Samsung Tv", 43.34),
            new Item(null, "Iphone", 643.34),
            new Item("WOW", "Airpod", 453.3),
            new Item(null, "Broiler", 423.3),
            new Item(null, "Macbook Pro", 643.35),
            new Item(null, "Monitor", 413.63)
    );

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemCappedRepository itemCappedRepository;

    @Autowired
    ReactiveMongoOperations mongoOperations;

    @Override
    public void run(String... args) throws Exception {
        initializeData();
        initializeCappedCollection();
        initializeCappedData();
    }

    private void initializeCappedCollection() {
        mongoOperations.dropCollection(ItemCapped.class)
            .then(mongoOperations.createCollection(
                ItemCapped.class,
                CollectionOptions.empty().maxDocuments(20).size(50000).capped()
        )).subscribe();
    }

    private void initializeCappedData() {
        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "Random Item " + i, 100.0 + i));

        itemCappedRepository.insert(itemCappedFlux)
                .subscribe(i -> log.info("Item Created: " + i));
    }

    private void initializeData() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemRepository::save)
                .thenMany(itemRepository.findAll())
                .subscribe(i -> {
                    System.out.println("ITEM SAVED: " + i.getDescription());
                });
    }
}
