package com.abuubaida921.esupermarkt.model;

public class PromoCodeModel {
    String code, description, id;
    double discount;
    long valid_from, valid_till;

    public PromoCodeModel() {
    }

    public PromoCodeModel(String code, String description, String id, double discount, long valid_from, long valid_till) {
        this.code = code;
        this.description = description;
        this.id = id;
        this.discount = discount;
        this.valid_from = valid_from;
        this.valid_till = valid_till;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public long getValid_from() {
        return valid_from;
    }

    public void setValid_from(long valid_from) {
        this.valid_from = valid_from;
    }

    public long getValid_till() {
        return valid_till;
    }

    public void setValid_till(long valid_till) {
        this.valid_till = valid_till;
    }
}
