package com.abuubaida921.esupermarkt.model;

public class DeliveryAddressModel {

    String id,delivery_address,user_id;

    public DeliveryAddressModel() {
    }

    public DeliveryAddressModel(String id, String delivery_address, String user_id) {
        this.id = id;
        this.delivery_address = delivery_address;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
