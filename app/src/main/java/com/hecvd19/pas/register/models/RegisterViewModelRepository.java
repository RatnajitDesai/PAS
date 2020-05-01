package com.hecvd19.pas.register.models;

import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hecvd19.pas.R;
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

class RegisterViewModelRepository {

    private static final String TAG = "RegisterViewModelReposi";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    RegisterViewModelRepository() {

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

    }

    /**
     * @param pinCode Get CityInfo for pincode
     * @return list of CityInfo
     */
    MutableLiveData<List<ApiResponse>> getApiResponse(String pinCode) {

        final MutableLiveData<List<ApiResponse>> data = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.postalpincode.in/pincode/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPinCodeApi api = retrofit.create(JsonPinCodeApi.class);

        Call<List<ApiResponse>> call = api.getApiResponse(pinCode);

        call.enqueue(new Callback<List<ApiResponse>>() {
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {

                if (!response.isSuccessful()) {
                    data.setValue(null);
                    Log.d(TAG, "onResponse: " + response.code());
                    return;
                }

                List<ApiResponse> apiResponse = response.body();
                if (apiResponse != null && apiResponse.get(0).getStatus().equals("Success")) {
                    data.setValue(apiResponse);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                data.setValue(null);
                Log.d(TAG, "onFailure: Call failed." + t.getMessage());
            }
        });

        return data;
    }


    /**
     * Provide changes to registration form
     *
     * @param username
     * @param email
     * @param password
     * @param pinCode
     * @return
     */
    RegisterFormState registrationDataChanged(String username, String email, String password, String pinCode) {

        if (!isUsernameValid(username)) {
            return new RegisterFormState(R.string.invalid_username, null, null, null, null, null, null);
        } else if (!isEmailValid(email)) {
            return new RegisterFormState(null, R.string.invalid_email, null, null, null, null, null);
        } else if (!isPinCodeValid(pinCode)) {
            return new RegisterFormState(null, null, R.string.invalid_pincode, null, null, null, null);
        } else if (!isPasswordValid(password)) {
            return new RegisterFormState(null, null, null, R.string.invalid_password, null, null, null);
        } else {
            return new RegisterFormState(true);
        }
    }

    private boolean isPinCodeValid(String pinCode) {

        return pinCode.length() == 6;
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 5;
    }

    private boolean isUsernameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.trim().isEmpty() || username.trim().length() < 4) {
            return false;
        }
        return username.matches("[A-Za-z0-9_]+");
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        } else if (email.trim().isEmpty()) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Register Citizen and save data
     *
     * @param username
     * @param email
     * @param pinCode
     * @param city
     * @param district
     * @param state
     * @param password
     * @return
     */
    MutableLiveData<String> registerCitizen(final String username, final String email, final String pinCode, final String city, final String district, final String state, String password) {

        final MutableLiveData<String> liveData = new MutableLiveData<>();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    liveData.postValue(UserCreationMessages.USER_ACCOUNT_CREATED);
                    Log.d(TAG, "onComplete: new user registered.");
                    final FirebaseUser user = mAuth.getCurrentUser();

                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    liveData.postValue(UserCreationMessages.USER_EMAIL_LINK_SENT);

//                                    //Create Map for citizen data
//                                    Map<String, Object> citizen_data = new HashMap<>();
//                                    citizen_data.put(Constants.FL_CITIZENS_USER_ID, user.getUid());
//                                    citizen_data.put(Constants.FL_CITIZENS_USER_NAME, username);
//                                    citizen_data.put(Constants.FL_CITIZENS_EMAIL, email);
//                                    citizen_data.put(Constants.FL_CITIZENS_PIN_CODE, pinCode);
//                                    citizen_data.put(Constants.FL_CITIZENS_CITY, city);
//                                    citizen_data.put(Constants.FL_CITIZENS_DISTRICT, district);
//                                    citizen_data.put(Constants.FL_CITIZENS_STATE, state);
//                                    citizen_data.put(Constants.FL_CITIZENS_TIMESTAMP, new Timestamp(new Date()));

                                    Citizen citizen = new Citizen(username, pinCode, city, district, state, email, user.getUid());

                                    //store user to cloud document
                                    firestore.collection(Constants.COL_CITIZENS)
                                            .document(user.getUid())
                                            .set(citizen).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                liveData.postValue(UserCreationMessages.USER_STORED);
                                            } else {
                                                liveData.postValue(UserCreationMessages.USER_STORAGE_ERROR);
                                                user.delete();
                                            }
                                        }
                                    });

                                } else {
                                    liveData.postValue(UserCreationMessages.USER_EMAIL_VERIFICATION_ERROR);
                                    user.delete();
                                }
                            }
                        });
                    }
                } else {
                    liveData.postValue(UserCreationMessages.USER_CREATION_ERROR);
                }
            }
        });
        return liveData;
    }
}
