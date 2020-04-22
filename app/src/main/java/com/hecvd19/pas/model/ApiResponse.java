package com.hecvd19.pas.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {


    @SerializedName("PostOffice")
    private List<CityInfo> cityInfo;

    @SerializedName("Message")
    private String message;

    @SerializedName("Status")
    private String status;

    public ApiResponse(List<CityInfo> cityInfo, String message, String status) {
        this.cityInfo = cityInfo;
        this.message = message;
        this.status = status;
    }

    public List<CityInfo> getCityInfo() {
        return cityInfo;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
