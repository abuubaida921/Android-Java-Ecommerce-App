package com.abuubaida921.esupermarkt.model;

public class ExclusiveOfferItemModel {
    String id, img_url, name, unit, unit_price, cat_id;
    Boolean is_best_selling, is_exclusive;

    public ExclusiveOfferItemModel() {
    }

    public ExclusiveOfferItemModel(String id, String img_url, String name, String unit, String unit_price, Boolean is_best_selling, Boolean is_exclusive, String cat_id) {
        this.id = id;
        this.img_url = img_url;
        this.name = name;
        this.unit = unit;
        this.unit_price = unit_price;
        this.is_exclusive = is_exclusive;
        this.is_best_selling = is_best_selling;
        this.cat_id = cat_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public Boolean getIs_best_selling() {
        return is_best_selling;
    }

    public void setIs_best_selling(Boolean is_best_selling) {
        this.is_best_selling = is_best_selling;
    }

    public Boolean getIs_exclusive() {
        return is_exclusive;
    }

    public void setIs_exclusive(Boolean is_exclusive) {
        this.is_exclusive = is_exclusive;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}
