package com.abuubaida921.esupermarkt.model;

public class CategoryItemModel {
    String id, img_url, name, item_holder_color_code;

    public CategoryItemModel() {
    }

    public CategoryItemModel(String id, String img_url, String name, String item_holder_color_code) {
        this.id = id;
        this.img_url = img_url;
        this.name = name;
        this.item_holder_color_code = item_holder_color_code;
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

    public String getItem_holder_color_code() {
        return item_holder_color_code;
    }

    public void setItem_holder_color_code(String item_holder_color_code) {
        this.item_holder_color_code = item_holder_color_code;
    }
}
