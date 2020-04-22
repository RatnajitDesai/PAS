package com.hecvd19.pas.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hecvd19.pas.R;
import com.hecvd19.pas.home.MainActivity;
import com.hecvd19.pas.login.models.LoginMessages;
import com.hecvd19.pas.login.models.LoginViewModel;
import com.hecvd19.pas.register.ui.RegisterActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    //vars
    LoginViewModel loginViewModel;

    //widgets
    MaterialButton mLogin, mRegister;
    ProgressBar mProgressBar;
    TextInputEditText mEmail, mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);
        mEmail = findViewById(R.id.txtEmail);
        mPassword = findViewById(R.id.txtPassword);
        mProgressBar = findViewById(R.id.progressBar);

        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        mEmail.addTextChangedListener(new TextWatcher() {
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
                    mEmail.setError("Invalid Email Id");
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

        loginViewModel.validInputs(mEmail.getText().toString(), mPassword.getText().toString()).observe(this, new Observer<Boolean>() {
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
                 * Login with firebase auth
                 */
                mProgressBar.setVisibility(View.VISIBLE);
                disableViews(mEmail, mPassword, mLogin, mRegister);
                loginViewModel.loginUser(mEmail.getText().toString(), mPassword.getText().toString())
                        .observe(this, new Observer<String>() {
                            @Override
                            public void onChanged(String msg) {
                                switch (msg) {
                                    case LoginMessages.LOGIN_SUCCESS:
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        enableViews(mEmail, mPassword, mLogin, mRegister);
                                        mProgressBar.setVisibility(View.GONE);
                                    default:
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        enableViews(mEmail, mPassword, mLogin, mRegister);
                                        mProgressBar.setVisibility(View.GONE);
                                }
                            }
                        });

                break;
            }
            case R.id.register: {
                /**
                 * Start register activity
                 */
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            }

        }

    }

    void enableViews(View... views) {
        for (View view : views) {

            view.setEnabled(true);

        }
    }


    void disableViews(View... views) {
        for (View view : views) {

            view.setEnabled(false);

        }
    }



}
