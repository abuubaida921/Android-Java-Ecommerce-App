package com.abuubaida921.esupermarkt.model;

public class CartItemModel {

    String id, product_id;
    int added_unit;
    float unit_price;

    public CartItemModel() {
    }

    public CartItemModel(String id, String product_id, int added_unit, float unit_price) {
        this.id = id;
        this.product_id = product_id;
        this.added_unit = added_unit;
        this.unit_price = unit_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getAdded_unit() {
        return added_unit;
    }

    public void setAdded_unit(int added_unit) {
        this.added_unit = added_unit;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }
}
