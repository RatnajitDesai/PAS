package com.hecvd19.pas.register.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hecvd19.pas.model.ApiResponse;

import java.util.List;

public class RegisterViewModel extends ViewModel {

    private static final String TAG = "RegisterViewModel";
    private RegisterViewModelRepository repository;
    private MutableLiveData<RegisterFormState> formStateMutableLiveData = new MutableLiveData<>();
    private LiveData<List<ApiResponse>> apiResponse = new MutableLiveData<>();

    public RegisterViewModel() {
        repository = new RegisterViewModelRepository();
    }

    public MutableLiveData<RegisterFormState> getRegisterFormState() {

        return formStateMutableLiveData;
    }

    public void registrationDataChanged(String username, String email, String password, String pinCode) {

        formStateMutableLiveData.postValue(repository.registrationDataChanged(username, email, password, pinCode));
    }

    public MutableLiveData<List<ApiResponse>> getApiResponse(String pinCode) {
        return repository.getApiResponse(pinCode);
    }


    public MutableLiveData<String> registerCitizen(String username, String email, String pinCode, String city, String district, String state, String password) {

        return repository.registerCitizen(username, email, pinCode, city, district, state, password);
    }

}
