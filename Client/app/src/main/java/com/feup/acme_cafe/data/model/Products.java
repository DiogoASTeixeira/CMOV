package com.feup.acme_cafe.data.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Products {
    private final List<Product> products;

    public Products() {
        products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonobject = response.getJSONObject(i);
                String id = jsonobject.getString("id");
                String name = jsonobject.getString("name");
                String price = jsonobject.getString("value");
                String url = jsonobject.getString("icon_path");
                Product t = new Product(id, name, Float.parseFloat(price), url);
                if(!products.contains(t)){
                    products.add(t);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
