package com.feup.acme_cafe.data.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Voucher implements Serializable {

    private final String id;
    private final boolean coffee;
    private final String name;

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Voucher) {
            return this.id.equals(((Voucher) obj).id);
        }
        return false;
    }

    public Voucher(String id, boolean coffee) {
        this.id = id;
        this.coffee = coffee;
        if(coffee){
            name = "Coffee Voucher";
        } else {
            name = "Normal Voucher";
        }
    }

    public String getId() {
        return id;
    }

    public boolean isCoffee() {
        return coffee;
    }

    public String getName() {
        return name;
    }
}
