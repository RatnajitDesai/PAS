package com.hecvd19.pas.login.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hecvd19.pas.model.Citizen;

public class LoginViewModel extends ViewModel {
    LoginRepository repository;

    public LoginViewModel() {
        repository = new LoginRepository();
    }

    public MutableLiveData<Citizen> loginUser() {
        return repository.loginUser();
    }

    public MutableLiveData<Boolean> verifyUsername(String username) {

        return repository.verifyUsername(username);
    }

    public MutableLiveData<String> verifyPassword(String password) {

        return repository.verifyPassword(password);
    }

    public MutableLiveData<Boolean> validInputs(String username, String password) {
        return repository.validInputs(username, password);
    }

}
