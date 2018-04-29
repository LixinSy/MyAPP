package com.lx.myapp;

/**
 * Created by 李莘 on 2018/4/10.
 */

public class Goods {

    private String id;
    private String name;
    private String price;

    public Goods(String id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
