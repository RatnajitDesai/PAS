package com.hecvd19.pas.login.models;

import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRepository {

    private static final String TAG = "LoginRepository";
    private FirebaseAuth mAuth;

    LoginRepository() {
        mAuth = FirebaseAuth.getInstance();
    }


    public MutableLiveData<String> loginUser(String email, String password) {

        final MutableLiveData<String> data = new MutableLiveData<>();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null && user.isEmailVerified()) {
                        data.setValue(LoginMessages.LOGIN_SUCCESS);
                    } else {
                        data.setValue(LoginMessages.LOGIN_EMAIL_NOT_VERIFIED);
                    }
                } else {
                    data.setValue(LoginMessages.LOGIN_ERROR);
                }
            }
        });


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
