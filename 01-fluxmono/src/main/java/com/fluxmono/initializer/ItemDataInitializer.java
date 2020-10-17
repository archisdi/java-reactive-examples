package com.fluxmono.initializer;

import com.fluxmono.document.Item;
import com.fluxmono.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
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

    @Override
    public void run(String... args) throws Exception {
        initializeData();
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
