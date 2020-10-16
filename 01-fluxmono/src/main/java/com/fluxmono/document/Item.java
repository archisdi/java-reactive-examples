package com.fluxmono.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document // @Entity
public class Item {
    @Id
    private String id;
    private String description;
    private Double price;

    public Item() {
    }

    public Item(String id, String description, Double price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public Item(String description, Double price) {
        this.description = description;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
