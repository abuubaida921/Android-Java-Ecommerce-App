package com.abuubaida921.esupermarkt.model;

public class OrderItemModel {

    String order_id, delivery_address, order_total_amount, user_id;

    public OrderItemModel() {
    }

    public OrderItemModel(String order_id, String delivery_address, String order_total_amount, String user_id) {
        this.order_id = order_id;
        this.delivery_address = delivery_address;
        this.order_total_amount = order_total_amount;
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getOrder_total_amount() {
        return order_total_amount;
    }

    public void setOrder_total_amount(String order_total_amount) {
        this.order_total_amount = order_total_amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
