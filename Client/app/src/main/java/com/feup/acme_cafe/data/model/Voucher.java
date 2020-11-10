package com.feup.acme_cafe.data.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Voucher implements Serializable {

    private final String id;
    private final boolean coffee;
    private final String name;
    private static final AtomicInteger countC = new AtomicInteger(0);
    private static final AtomicInteger countN = new AtomicInteger(0);

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
            name = "Coffee Voucher " + countC.incrementAndGet();
        } else {
            name = "Normal Voucher " + countN.incrementAndGet();
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

    public boolean isUsed() {
        boolean used = false;
        return used;
    }
}
