package com.feup.acme_cafe.data.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String id;
    private int card_number;
    private int card_cvs;
    private Float total_spent;
    private Float hundred_multiples;
    private Float total_coffees;
    private final List<Transaction> transactions;
    private final List<Voucher> vouchers;
    private String username;
    private String name;
    private String email;
    private Transaction basket;
    private final List<Product> products;

    public Transaction getBasket() {
        return basket;
    }

    public void setBasket(Transaction basket) {
        this.basket = basket;
    }

    public User(JSONObject response) {
        transactions = new ArrayList<>();
        vouchers = new ArrayList<>();
        basket = new Transaction();
        products = new ArrayList<>();
        try {
            this.id = response.getString("id");
            this.username = response.getString("username");
            this.name = response.getString("name");
            this.card_number = response.getInt("card_number");
            this.card_cvs = response.getInt("card_cvs");
            String nif = response.getString("nif");
            Float nif1 = Float.parseFloat(nif);
            String total_coffees = response.getString("total_coffees");
            this.total_coffees = Float.parseFloat(total_coffees);
            String hundred_multiples = response.getString("hundred_multiples");
            this.hundred_multiples = Float.parseFloat(hundred_multiples);
            String total_spent = response.getString("total_spent");
            this.total_spent = Float.parseFloat(total_spent);
            this.email = response.getString("email");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Float getTotal_spent() {
        return total_spent;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTransactions(JSONArray response) {
        System.out.println(response);
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonobject = response.getJSONObject(i);
                String id = jsonobject.getString("id");
                String value = jsonobject.getString("total_value");
                String date = jsonobject.getString("createdAt");
                Transaction t = new Transaction(id, Float.parseFloat(value), date);
                if(!transactions.contains(t)){
                    transactions.add(t);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setVouchers(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonobject = response.getJSONObject(i);
                String id = jsonobject.getString("id");
                boolean coffee = jsonobject.getBoolean("coffee");
                Voucher voucher = new Voucher(id, coffee);
                if(!vouchers.contains(voucher)){
                    vouchers.add(voucher);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        int number_coffee = 0;
        int number_normal = 0;
        for(int j = 0; j < vouchers.size(); j++){
            if(vouchers.get(j).getName().equals("Coffee Voucher")) {
                number_coffee++;
                vouchers.get(j).fixName(String.valueOf(number_coffee));
            } else if (vouchers.get(j).getName().equals("Normal Voucher")){
                number_normal++;
                vouchers.get(j).fixName(String.valueOf(number_normal));
            }
        }
    }

    public String getEmail(){
        return this.email;
    }

    public void flushTransaction()
    {
        this.basket= new Transaction();
    }
}