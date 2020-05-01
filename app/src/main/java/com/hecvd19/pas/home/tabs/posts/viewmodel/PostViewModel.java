package com.hecvd19.pas.home.tabs.posts.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hecvd19.pas.model.Citizen;

public class PostViewModel extends ViewModel {

    private PostListRepository postListRepository = new FirestorePostListRepositoryCallback();
    private GetUserInfo getUserInfo = new FirestorePostListRepositoryCallback();

    public PostListLiveData getPostListLiveData(String pinCode, boolean isNewQuery) {
        return postListRepository.getPostListLiveData(pinCode, isNewQuery);
    }

    public LiveData<Citizen> getUserInfo() {
        return getUserInfo.getUserInfo();
    }

    public PostListLiveData getPostListLiveDataForState(String state, boolean isNewQuery) {
        return postListRepository.getPostListLiveDataForState(state, isNewQuery);
    }

    public PostListLiveData getPostsWithDistrict(String district, String state, boolean isNewQuery) {
        return postListRepository.getPostsWithDistrict(district, state, isNewQuery);
    }

    interface PostListRepository {
        PostListLiveData getPostListLiveData(String pinCode, boolean isNewQuery);

        PostListLiveData getPostListLiveDataForState(String state, boolean isNewQuery);

        PostListLiveData getPostsWithDistrict(String district, String state, boolean isNewQuery);
    }

    interface GetUserInfo {
        LiveData<Citizen> getUserInfo();
    }

}
