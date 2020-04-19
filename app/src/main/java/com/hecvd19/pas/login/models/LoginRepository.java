package com.hecvd19.pas.login.models;

import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;

import com.hecvd19.pas.model.Citizen;

public class LoginRepository {

    private static final String TAG = "LoginRepository";


    public MutableLiveData<Citizen> loginUser() {

        MutableLiveData<Citizen> data = new MutableLiveData<>();
        return data;
    }


    /**
     * Verify username inputs
     *
     * @param username
     * @return
     */
    public MutableLiveData<Boolean> verifyUsername(String username) {

        MutableLiveData<Boolean> data = new MutableLiveData<>();

        if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            data.postValue(true);
        } else {
            data.postValue(false);
        }
        return data;
    }

    /**
     * Verify password inputs
     *
     * @param password
     * @return
     */
    public MutableLiveData<String> verifyPassword(String password) {

        MutableLiveData<String> data = new MutableLiveData<>();

        if (password.length() < 6) {
            data.postValue("Must be 6 characters long");
        } else {
            data.postValue("");
        }


        return data;
    }

    public MutableLiveData<Boolean> validInputs(String username, String password) {

        MutableLiveData<Boolean> data = new MutableLiveData<>();
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches() && password.length() > 5) {
            data.postValue(true);
        } else {
            data.postValue(false);
        }
        return data;
    }
}
