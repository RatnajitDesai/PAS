package com.hecvd19.pas.home.tabs.posts.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hecvd19.pas.model.Citizen;
import com.hecvd19.pas.utilities.Constants;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;
import static com.hecvd19.pas.utilities.Constants.COL_POSTS;
import static com.hecvd19.pas.utilities.Constants.FL_POSTS_POST_DISTRICT;
import static com.hecvd19.pas.utilities.Constants.FL_POSTS_POST_PIN_CODES;
import static com.hecvd19.pas.utilities.Constants.FL_POSTS_POST_STATE;
import static com.hecvd19.pas.utilities.Constants.FL_POSTS_TIMESTAMP;
import static com.hecvd19.pas.utilities.Constants.LIMIT;

public class FirestorePostListRepositoryCallback implements PostViewModel.PostListRepository, PostListLiveData.OnLastVisiblePostCallback,
        PostListLiveData.OnLastPostReachedCallback, PostViewModel.GetUserInfo {

    private static final String TAG = "FirestorePostListReposi";


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = firebaseFirestore.collection(COL_POSTS);
    private Query query;

    private DocumentSnapshot lastVisiblePost;
    private boolean isLastPostReached;


    @Override
    public void setLastVisiblePost(DocumentSnapshot lastVisiblePost) {
        this.lastVisiblePost = lastVisiblePost;

    }

    @Override
    public void setLastPostReached(boolean isLastPostReached) {
        this.isLastPostReached = isLastPostReached;
    }

    @Override
    public PostListLiveData getPostListLiveData(String pinCode, boolean isNewQuery) {
        Log.d(TAG, "getPostListLiveData: " + pinCode);
        query = productsRef.whereArrayContains(FL_POSTS_POST_PIN_CODES, pinCode)
                .orderBy(FL_POSTS_TIMESTAMP, DESCENDING).limit(LIMIT);

        if (isLastPostReached && !isNewQuery) {
            Log.d(TAG, "getPostListLiveData: isLastPostReached");
            return null;
        }
        if (lastVisiblePost != null) {
            Log.d(TAG, "getPostListLiveData: lastvisible : ");
            query = query.startAfter(lastVisiblePost);
        }
        return new PostListLiveData(query, this, this);
    }

    @Override
    public PostListLiveData getPostListLiveDataForState(String state, boolean isNewQuery) {
        Log.d(TAG, "getPostListLiveData: " + state);
        query = productsRef.whereEqualTo(FL_POSTS_POST_STATE, state)
                .orderBy(FL_POSTS_TIMESTAMP, DESCENDING).limit(LIMIT);

        if (isLastPostReached && !isNewQuery) {
            Log.d(TAG, "getPostListLiveData: isLastPostReached");
            return null;
        }
        if (lastVisiblePost != null) {
            Log.d(TAG, "getPostListLiveData: lastvisible : ");
            query = query.startAfter(lastVisiblePost);
        }
        return new PostListLiveData(query, this, this);
    }

    @Override
    public PostListLiveData getPostsWithDistrict(String district, String state, boolean isNewQuery) {
        Log.d(TAG, "getPostsWithDistrict: " + district);
        query = productsRef.whereEqualTo(FL_POSTS_POST_STATE, state);
        query = query.whereEqualTo(FL_POSTS_POST_DISTRICT, district)
                .orderBy(FL_POSTS_TIMESTAMP, DESCENDING).limit(LIMIT);

        if (isLastPostReached && !isNewQuery) {
            Log.d(TAG, "getPostListLiveData: isLastPostReached");
            return null;
        }
        if (lastVisiblePost != null) {
            Log.d(TAG, "getPostListLiveData: lastvisible : ");
            query = query.startAfter(lastVisiblePost);
        }
        return new PostListLiveData(query, this, this);
    }


    @Override
    public MutableLiveData<Citizen> getUserInfo() {

        MutableLiveData<Citizen> citizenMutableLiveData = new MutableLiveData<>();
        String userID = mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection(Constants.COL_CITIZENS)
                .document(userID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot == null) {
                    return;
                } else {

                    Citizen citizen = new Citizen();
                    citizen.setPin_code(documentSnapshot.get(Constants.FL_CITIZENS_PIN_CODE).toString());
                    citizen.setCity(documentSnapshot.get(Constants.FL_CITIZENS_CITY).toString());
                    citizen.setDistrict(documentSnapshot.get(Constants.FL_CITIZENS_DISTRICT).toString());
                    citizen.setState(documentSnapshot.get(Constants.FL_CITIZENS_STATE).toString());
                    citizenMutableLiveData.postValue(citizen);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
                citizenMutableLiveData.postValue(null);
            }
        });
        return citizenMutableLiveData;
    }
}
