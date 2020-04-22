package com.hecvd19.pas.register.ui;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hecvd19.pas.R;
import com.hecvd19.pas.model.ApiResponse;
import com.hecvd19.pas.model.CityInfo;
import com.hecvd19.pas.register.models.RegisterFormState;
import com.hecvd19.pas.register.models.RegisterViewModel;
import com.hecvd19.pas.register.models.UserCreationMessages;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    //vars
    private RegisterViewModel registerViewModel;
    private boolean isPinValidated;

    //widgets
    private TextInputEditText mUsername, mEmail, mPinCode, mCity, mDistrict, mState, mPassword;
    private TextInputLayout mPinCodeLayout;
    private MaterialButton mValidate, mRegister;
    private ProgressBar mProgressBar;
    private ImageView mBackBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mUsername = findViewById(R.id.txtUsername);
        mEmail = findViewById(R.id.txtEmail);
        mPinCode = findViewById(R.id.txtPinCode);
        mPinCodeLayout = findViewById(R.id.pinCode);
        mCity = findViewById(R.id.txtCity);
        mDistrict = findViewById(R.id.txtDistrict);
        mState = findViewById(R.id.txtState);
        mPassword = findViewById(R.id.txtPassword);
        mValidate = findViewById(R.id.btnValidatePin);
        mRegister = findViewById(R.id.register);
        mProgressBar = findViewById(R.id.progressBar);
        mBackBtn = findViewById(R.id.btnBack);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRegister.setOnClickListener(this);
        mValidate.setOnClickListener(this);

        mPassword.setEnabled(isPinValidated);         //allows to enter password only after pin code is validated

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                } else {

                    if (registerFormState.getPasswordError() != null) {
                        mPassword.setError(getString(registerFormState.getPasswordError()));
                    }
                    if (registerFormState.getUserNameError() != null) {
                        mUsername.setError(getString(registerFormState.getUserNameError()));
                    }
                    if (registerFormState.getEmailError() != null) {
                        mEmail.setError(getString(registerFormState.getEmailError()));
                    }
                    if (registerFormState.getPinCodeError() != null) {
                        mPinCode.setError(getString(registerFormState.getPinCodeError()));
                    }

                    mValidate.setEnabled(mPinCode.length() == 6);
                    mRegister.setEnabled(isPinValidated && registerFormState.isDataValid());
                }
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registrationDataChanged(mUsername.getText().toString(), mEmail.getText().toString(),
                        mPassword.getText().toString(), mPinCode.getText().toString());

            }
        };

        mUsername.addTextChangedListener(watcher);
        mPassword.addTextChangedListener(watcher);
        mEmail.addTextChangedListener(watcher);

        mPinCode.addTextChangedListener(watcher);

        mCity.setOnClickListener(this);
        mDistrict.setOnClickListener(this);
        mState.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register: {
                /**
                 *  Register users with provided email/password and save data to Citizens collection
                 */
                mProgressBar.setVisibility(View.VISIBLE);
                disableViews(mValidate, mPinCode, mUsername, mEmail, mPassword, mRegister);
                registerViewModel.registerCitizen(mUsername.getText().toString().trim(),
                        mEmail.getText().toString(), mPinCode.getText().toString(), mCity.getText().toString(), mDistrict.getText().toString(), mState.getText().toString(), mPassword.getText().toString()).observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String msg) {
                        switch (msg) {

                            case UserCreationMessages.USER_STORED:
                                finish();
                                mProgressBar.setVisibility(View.GONE);
                                break;

                            case UserCreationMessages.USER_CREATION_ERROR:
                            case UserCreationMessages.USER_EMAIL_VERIFICATION_ERROR:
                            case UserCreationMessages.USER_STORAGE_ERROR:
                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                recreate();
                                mProgressBar.setVisibility(View.GONE);
                                break;

                            default:
                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

                break;
            }
            case R.id.btnValidatePin: {
                /**
                 * Validate Pin with api and if successful retrieve @Params state, district, city etc.
                 */
                disableViews(mValidate, mPinCode, mUsername, mEmail, mRegister);

                registerViewModel.getApiResponse(mPinCode.getText().toString()).observe(this, new Observer<List<ApiResponse>>() {
                    @Override
                    public void onChanged(List<ApiResponse> apiResponses) {
                        if (apiResponses != null) {
                            Log.d(TAG, "onChanged: apiResponse :success");
                            mPinCodeLayout.setHelperText(getString(R.string.pin_validated));
                            mPinCodeLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                            getCityFromDropdown(apiResponses.get(0).getCityInfo());
                            mDistrict.setText(apiResponses.get(0).getCityInfo().get(0).getDistrict());
                            mState.setText(apiResponses.get(0).getCityInfo().get(0).getState());
                            enableViews(mValidate, mPinCode, mUsername, mEmail);
                            isPinValidated = true;
                        } else {

                            mPinCodeLayout.setHelperText(getString(R.string.invalid_pincode));
                            mPinCodeLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            enableViews(mValidate, mPinCode, mUsername, mEmail);
                            isPinValidated = false;
                        }

                        mRegister.setEnabled(isPinValidated && mPassword.getText().toString().length() > 5);
                        mPassword.setEnabled(isPinValidated);
                    }
                });

                break;
            }

        }

    }

    private void getCityFromDropdown(List<CityInfo> cityInfos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_city);
        List<String> cityNames = new ArrayList<>();
        for (CityInfo city : cityInfos) {
            cityNames.add(city.getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice);
        adapter.addAll(cityNames);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selected) {
                mCity.setText(adapter.getItem(selected));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

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
