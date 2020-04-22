package com.hecvd19.pas.login.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private LoginRepository repository;

    public LoginViewModel() {
        repository = new LoginRepository();
    }

    public MutableLiveData<String> loginUser(String email, String password) {
        return repository.loginUser(email, password);
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
