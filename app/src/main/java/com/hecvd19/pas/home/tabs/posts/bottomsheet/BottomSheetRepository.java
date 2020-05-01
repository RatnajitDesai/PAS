package com.hecvd19.pas.home.tabs.posts.bottomsheet;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hecvd19.pas.model.ApiResponse;
import com.hecvd19.pas.model.Citizen;
import com.hecvd19.pas.restApi.JsonPinCodeApi;
import com.hecvd19.pas.utilities.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetRepository {

    private static final String TAG = "BottomSheetRepository";
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    BottomSheetRepository() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Get Citizen Info
     *
     * @return
     */
    MutableLiveData<Citizen> getCitizenInfo() {

        MutableLiveData<Citizen> citizenMutableLiveData = new MutableLiveData<>();
        String userID = mAuth.getCurrentUser().getUid();

        firestore.collection(Constants.COL_CITIZENS)
                .document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    Citizen cityInfo = new Citizen();
                    cityInfo.setPin_code(task.getResult().get(Constants.FL_CITIZENS_PIN_CODE).toString());
                    cityInfo.setCity(task.getResult().get(Constants.FL_CITIZENS_CITY).toString());
                    cityInfo.setDistrict(task.getResult().get(Constants.FL_CITIZENS_DISTRICT).toString());
                    cityInfo.setState(task.getResult().get(Constants.FL_CITIZENS_STATE).toString());

                    citizenMutableLiveData.postValue(cityInfo);

                } else {
                    citizenMutableLiveData.postValue(null);
                }

            }
        });


        return citizenMutableLiveData;

    }


    /**
     * @param pinCode Get CityInfo for pincode
     * @return list of CityInfo
     */
    MutableLiveData<List<ApiResponse>> getApiResponse(String pinCode) {

        Log.d(TAG, "getApiResponse: " + pinCode);
        final MutableLiveData<List<ApiResponse>> data = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.postalpincode.in/pincode/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPinCodeApi api = retrofit.create(JsonPinCodeApi.class);

        Log.d(TAG, "getApiResponse: api: " + api.toString());

        Call<List<ApiResponse>> call = api.getApiResponse(pinCode);

        Log.d(TAG, "getApiResponse: call : " + call.toString());
        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                if (!response.isSuccessful()) {
                    data.setValue(null);
                    Log.d(TAG, "onResponse: " + response.code());
                    return;
                }

                List<ApiResponse> apiResponse = response.body();
                if (apiResponse != null && apiResponse.get(0).getStatus().equals("Success")) {
                    Log.d(TAG, "onResponse: success" + apiResponse.get(0).getMessage());
                    data.setValue(apiResponse);
                } else {
                    Log.d(TAG, "onResponse: failed" + apiResponse.get(0).getMessage());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                Log.d(TAG, "onResponse: " + t.getMessage());
                data.setValue(null);
                Log.d(TAG, "onFailure: Call failed." + t.getMessage());
            }
        });

        return data;
    }


}
