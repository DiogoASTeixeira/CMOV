package com.feup.acme_cafe.data.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Product implements Serializable {

    private String imageUrl;
    private String id;
    private String name;
    private double price;
    private int count;

    public Product(String id, String name, Float price, String imageUrl){
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String id, String name, Float price, int count){
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Product){
            Product aux = (Product) obj;
            return this.id.equals(aux.id);
        }
        return false;
    }

    public int getCount() { return  count; }

    public void setCount(int count) { this.count = count; }

    public String getUrl() {
        return this.imageUrl;
    }

    public void setUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
