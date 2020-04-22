package com.hecvd19.pas.restApi;

import com.hecvd19.pas.model.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface JsonPinCodeApi {

    @Headers("Content-Type:application/json")
    @GET("")
    Call<List<ApiResponse>> getApiResponse(@Url String pinCode);


}
