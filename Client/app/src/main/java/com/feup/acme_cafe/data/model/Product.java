package com.feup.acme_cafe.data.model;

import java.io.Serializable;

public class Product implements Serializable {

    private String imageUrl;
    private String id;
    private String name;
    private float price;
    private Integer count;

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    //id;name;price
    public Product(String content){
        String[] ss = content.split(";");
        id = ss[0];
        name = ss[1];
        price = Float.parseFloat(ss[2]);
    }

    public Product(String name, Float price, String imageUrl){
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String name, Float price, Integer count){
        this.name = name;
        this.price = price;
        this.count = count;
    }


    public String getUrl() {
        return this.imageUrl;
    }

    public void setUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
