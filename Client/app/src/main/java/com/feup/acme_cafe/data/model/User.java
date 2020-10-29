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
    private Float nif;
    private Float total_coffees;
    private List<Transaction> transactions;
    private List<String> vouchers;
    private String username;
    private String name;
    private Transaction basket;

    public Transaction getBasket() {
        return basket;
    }

    public void setBasket(Transaction basket) {
        this.basket = basket;
    }

    public User(JSONObject response) {
        transactions = new ArrayList();
        vouchers = new ArrayList();
        basket = new Transaction();
        try {
            this.id = response.getString("id");
            this.username = response.getString("username");
            this.name = response.getString("name");
            this.card_number = response.getInt("card_number");
            this.card_cvs = response.getInt("card_cvs");
            String nif = response.getString("nif");
            this.nif = Float.parseFloat(nif);
            String total_coffees = response.getString("total_coffees");
            this.total_coffees = Float.parseFloat(total_coffees);
            String hundred_multiples = response.getString("hundred_multiples");
            this.hundred_multiples = Float.parseFloat(hundred_multiples);
            String total_spent = response.getString("total_spent");
            this.total_spent = Float.parseFloat(total_spent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public int getCard_number() {
        return card_number;
    }

    public int getCard_cvs() {
        return card_cvs;
    }

    public Float getTotal_spent() {
        return total_spent;
    }

    public Float getHundred_multiples() {
        return hundred_multiples;
    }

    public Float getTotal_coffees() {
        return total_coffees;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Transaction getTransaction(int i) {
        return transactions.get(i);
    }

    public List<String> getVouchers() {
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
                transactions.add(t);
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
                vouchers.add(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void flushTransaction()
    {
        this.basket= new Transaction();
    }
}