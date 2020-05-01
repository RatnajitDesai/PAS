package com.hecvd19.pas.model;

public class Citizen {

    private String username;
    private String pin_code, city, district, state;
    private String email;
    private String user_id;

    public Citizen() {
    }

    public Citizen(String username, String pin_code, String city, String district, String state, String email, String user_id) {
        this.username = username;
        this.pin_code = pin_code;
        this.city = city;
        this.district = district;
        this.state = state;
        this.email = email;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "username='" + username + '\'' +
                ", pin_code='" + pin_code + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}

