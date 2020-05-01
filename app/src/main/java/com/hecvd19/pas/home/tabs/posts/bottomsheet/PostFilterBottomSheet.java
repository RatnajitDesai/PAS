package com.hecvd19.pas.home.tabs.posts.bottomsheet;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hecvd19.pas.R;
import com.hecvd19.pas.model.ApiResponse;
import com.hecvd19.pas.utilities.ListViewBottomSheet;
import com.hecvd19.pas.utilities.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostFilterBottomSheet extends BottomSheetDialogFragment implements ListViewBottomSheet.SendListener {

    private static final String TAG = "PostFilterBottomSheet";

    public void setSearchWithPinCode(SearchWithPinCode searchWithPinCode) {
        this.searchWithPinCode = searchWithPinCode;
    }

    @Override
    public void sendState(String text) {
        txtState.setText(text);
        if (mDistrict.isChecked()) {
            openBottomSheet(R.string.district, Utils.getDistricts(text));
        }
    }

    @Override
    public void sendDistrict(String text) {
        txtDistrict.setText(text);
    }

    public interface SearchWithPinCode {
        void search(String pinCode);
    }

    public interface SearchWithDistrict {
        void search(String district, String state);
    }

    public interface SearchWithState {
        void search(String state);
    }

    //vars
    private BottomSheetViewModel sheetViewModel;
    private SearchWithPinCode searchWithPinCode;
    private SearchWithDistrict searchWithDistrict;
    private SearchWithState searchWithState;

    public void setSearchWithDistrict(SearchWithDistrict searchWithDistrict) {
        this.searchWithDistrict = searchWithDistrict;
    }

    public void setSearchWithState(SearchWithState searchWithState) {
        this.searchWithState = searchWithState;
    }

    //widgets
    private RadioGroup mRadioGroup;
    private RadioButton mPinCode, mDistrict, mState;
    private TextInputEditText txtPinCode, txtDistrict, txtState;
    private MaterialButton mSubmit;
    private TextInputLayout mPinCodeLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.locationselectionsheet, container, false);
        sheetViewModel = new ViewModelProvider(Objects.requireNonNull(this.getActivity())).get(BottomSheetViewModel.class);
        mRadioGroup = view.findViewById(R.id.radio_group);
        mPinCode = view.findViewById(R.id.rb_pinCode);
        mPinCodeLayout = view.findViewById(R.id.pinCode);
        mDistrict = view.findViewById(R.id.rb_district);
        mState = view.findViewById(R.id.rb_state);
        txtPinCode = view.findViewById(R.id.txtPinCode);
        txtDistrict = view.findViewById(R.id.txtDistrict);
        txtState = view.findViewById(R.id.txtState);
        mSubmit = view.findViewById(R.id.btnSubmit);
        init();

        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_district: {
                    disableViews(txtPinCode);
                    enableViews(txtDistrict, txtState);
                    break;
                }
                case R.id.rb_state: {
                    disableViews(txtDistrict, txtPinCode);
                    enableViews(txtState);
                    break;
                }
                case R.id.rb_pinCode: {
                    disableViews(txtDistrict, txtState);
                    enableViews(txtPinCode);
                }
            }
        });


        mSubmit.setOnClickListener(v -> submitNewQueryParameters());

        mDistrict.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openBottomSheet(R.string.state, Utils.getStates());
            }

        });

        mState.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openBottomSheet(R.string.state, Utils.getStates());
            }

        });

        txtDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtState.getText().toString().isEmpty()) {
                    openBottomSheet(R.string.state, Utils.getStates());
                } else {
                    openBottomSheet(R.string.district, Utils.getDistricts(txtState.getText().toString()));
                }
            }
        });

        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet(R.string.state, Utils.getStates());
            }
        });

        return view;
    }

    private void openBottomSheet(int type, ArrayList<String> list) {
        ListViewBottomSheet sheet = new ListViewBottomSheet(type, list, this);
        sheet.show(getParentFragmentManager(), "ListBottomSheet");
    }

    private void submitNewQueryParameters() {
        Log.d(TAG, "submitNewQueryParameters: " + txtPinCode.getText().toString());
        if (mPinCode.isChecked()) {
            if (txtPinCode.getText().toString().length() == 6) {

                Log.d(TAG, "submitNewQueryParameters: " + txtPinCode.getText().toString());
                sheetViewModel.getApiResponse(txtPinCode.getText().toString()).observe(this.getActivity(), new Observer<List<ApiResponse>>() {
                    @Override
                    public void onChanged(List<ApiResponse> apiResponses) {
                        if (apiResponses != null) {
                            mPinCodeLayout.setHelperText(getString(R.string.pin_validated));
                            mPinCodeLayout.setHelperTextColor(ColorStateList.valueOf(Color.GREEN));
                            searchWithPinCode.search(txtPinCode.getText().toString());// send pincode to postfragment'
                            Log.d(TAG, "onChanged: pin sent." + txtPinCode.getText().toString());

                        } else {
                            mPinCodeLayout.setHelperText(getString(R.string.invalid_pincode));
                            mPinCodeLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            txtPinCode.setError("Please enter correct Pin Code!");
                        }
                        dismiss();
                    }
                });
            }
        }
        if (mState.isChecked()) {
            {
                if (!txtState.getText().toString().isEmpty()) {
                    searchWithState.search(txtState.getText().toString());
                } else {
                    txtState.setError(getString(R.string.click_to_select));
                }
                dismiss();
            }
        }

        if (mDistrict.isChecked()) {
            if (txtState.getText().toString().isEmpty()) {
                txtState.setError(getString(R.string.click_to_select));
            } else if (txtDistrict.getText().toString().isEmpty()) {
                txtDistrict.setError(getString(R.string.click_to_select));
            } else {
                searchWithDistrict.search(txtDistrict.getText().toString(), txtState.getText().toString());
            }
            dismiss();
        }
    }


    private void disableViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    private void enableViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void init() {

        sheetViewModel.getUserInfo().observe(Objects.requireNonNull(getActivity()), citizen -> {
            if (citizen != null) {
                txtPinCode.setText(citizen.getPin_code());
                disableViews(txtState, txtDistrict);
            } else {
                txtPinCode.setError("Required");
                disableViews(txtState, txtDistrict);
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
