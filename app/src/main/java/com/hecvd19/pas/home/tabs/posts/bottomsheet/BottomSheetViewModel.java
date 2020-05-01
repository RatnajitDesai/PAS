package com.hecvd19.pas.home.tabs.posts.bottomsheet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hecvd19.pas.model.ApiResponse;
import com.hecvd19.pas.model.Citizen;

import java.util.List;

public class BottomSheetViewModel extends ViewModel {

    BottomSheetRepository repository;

    public BottomSheetViewModel() {
        repository = new BottomSheetRepository();
    }

    public LiveData<Citizen> getUserInfo() {
        return repository.getCitizenInfo();
    }

    public LiveData<List<ApiResponse>> getApiResponse(String pinCode) {
        return repository.getApiResponse(pinCode);
    }

}
