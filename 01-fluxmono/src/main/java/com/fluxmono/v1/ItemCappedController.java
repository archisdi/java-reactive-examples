package com.fluxmono.v1;

import com.fluxmono.constant.ItemConstant;
import com.fluxmono.document.ItemCapped;
import com.fluxmono.repository.ItemCappedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class ItemCappedController {

    @Autowired
    ItemCappedRepository itemCappedRepository;

    @GetMapping(value = ItemConstant.ITEM_STREAM_ENDPOINT_v1, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemsStream() {
        return itemCappedRepository.findItemsBy();
    }

}

