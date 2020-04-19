package com.hecvd19.pas.model;

public class Citizen {

    private String username;
    private String pinCode, district, state, country;
    private String email;

    public Citizen() {

    }

    public Citizen(String username, String pinCode, String district, String state, String country, String email) {
        this.username = username;
        this.pinCode = pinCode;
        this.district = district;
        this.state = state;
        this.country = country;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "username='" + username + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
