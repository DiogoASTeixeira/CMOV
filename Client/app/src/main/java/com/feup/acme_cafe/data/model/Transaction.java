package com.feup.acme_cafe.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serializable {

    private String id;
    private float total_value;
    private String date;
    private List<Product> products;

    private String voucher;

    public Transaction(String id, float total_value, String date) {
        this.id = id;
        this.total_value = total_value;
        this.products = new ArrayList();
        this.date = date;
    }

    public Transaction()
    {
        this.total_value = 0;
        this.products=new ArrayList();
        this.voucher = "";
    }


    public void addProducts(String product) {
        this.products.add(this.products.size(),new Product(product));
    }

    public void addProducts(Product product) {
        this.products.add(this.products.size(),product);
        this.total_value+=product.getPrice();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public float getTotal_value() {

        return total_value;
    }

    public void setTotal_value(float total_value) {
        this.total_value = total_value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Product> getProducts() {

        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public void removeProduct(String name) {
        for (int i=0; i<this.products.size();i++)
        {
            if (this.products.get(i).getName().equals(name))
            {
                this.total_value-=this.products.get(i).getPrice();
                this.products.remove(i);

            }
        }
    }
}