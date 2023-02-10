package com.abuubaida921.esupermarkt.model;

public class UserModel {

    String id,full_name,user_email,zone,area;

    public UserModel() {
    }

    public UserModel(String id, String full_name, String user_email, String zone, String area) {
        this.id = id;
        this.full_name = full_name;
        this.user_email = user_email;
        this.zone = zone;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
