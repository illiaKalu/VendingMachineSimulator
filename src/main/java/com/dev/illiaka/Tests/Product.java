package com.dev.illiaka.Tests;

/**
 * Created by sonicmaster on 07.09.16.
 */
public class Product {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Product(String type, String price, int amount) {
        this.type = type;
        this.price = price;
        this.amount = amount;
    }

    public String getPrice() {
        return price;

    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    String type;
    String price;
    int amount;

}
