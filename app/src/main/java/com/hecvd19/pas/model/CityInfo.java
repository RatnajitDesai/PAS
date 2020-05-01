package com.hecvd19.pas.model;

import com.google.gson.annotations.SerializedName;

public class CityInfo {

    @SerializedName("Name")
    private String name;

    @SerializedName("City")
    private String city;

    @SerializedName("District")
    private String district;

    @SerializedName("Division")
    private String division;

    @SerializedName("Region")
    private String region;

    @SerializedName("State")
    private String state;

    @SerializedName("Pincode")
    private String pinCode;

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
    public String getDistrict() {
        return district;
    }

    public String getDivision() {
        return division;
    }

    public String getRegion() {
        return region;
    }

    public String getState() {
        return state;
    }

    public String getPinCode() {
        return pinCode;
    }
}
