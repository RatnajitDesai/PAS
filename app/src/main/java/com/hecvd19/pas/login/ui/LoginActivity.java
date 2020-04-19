package com.hecvd19.pas.login.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hecvd19.pas.R;
import com.hecvd19.pas.login.models.LoginViewModel;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    //vars
    LoginViewModel loginViewModel;

    //widgets
    MaterialButton mLogin, mRegister;
    TextInputEditText mUsername, mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new LoginViewModel();
        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyUsername(s.toString());
                setUI();
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyPassword(s.toString());
                setUI();
            }
        });

    }

    void verifyUsername(String username) {
        loginViewModel.verifyUsername(username).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                if (!result) {
                    mUsername.setError("Invalid Email Id");
                }
            }
        });
    }

    void verifyPassword(String password) {
        loginViewModel.verifyPassword(password).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()) {
                    mPassword.setError(s);
                }
            }
        });
    }


    void setUI() {

        loginViewModel.validInputs(mUsername.getText().toString(), mPassword.getText().toString()).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mLogin.setEnabled(aBoolean);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login: {
                /**
                 * @TODO: Login with firebase auth
                 */

                break;
            }
            case R.id.register: {
                /**
                 * @TODO: Start register activity
                 */

                break;
            }

        }

    }


}
