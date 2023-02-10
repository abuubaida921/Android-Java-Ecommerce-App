package com.abuubaida921.esupermarkt.model;

public class AboutModel {

    String id,description;

    public AboutModel() {
    }

    public AboutModel(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
