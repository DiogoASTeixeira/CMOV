package com.feup.acme_cafe.data.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serializable {

    private String id;
    private float total_value;
    private String date;
    private List<Product> products;
    private double discount;

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Transaction){
            return this.id.equals(((Transaction) obj).id);
        }
        return false;
    }

    private String voucher;

    public Transaction(String id, float total_value, String date) {
        this.id = id;
        this.total_value = total_value;
        this.products = new ArrayList<>();
        this.date = date;
    }

    //Check how to delete this
    public Transaction() {
        this.total_value = 0;
        this.products = new ArrayList<>();
        this.voucher = "";
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return this.discount;
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
}