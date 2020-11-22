package com.feup.acme_cafe.data.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Voucher implements Serializable {

    private final String id;
    private final boolean coffee;
    private String name;

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

    public void fixName(String i) {
        this.name = name + " " + i;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
