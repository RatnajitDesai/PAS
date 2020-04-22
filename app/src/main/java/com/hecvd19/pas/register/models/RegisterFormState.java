package com.hecvd19.pas.register.models;


public class RegisterFormState {

    private Integer userNameError, emailError, pinCodeError, passwordError, cityError, districtError, stateError;
    private boolean isDataValid;

    public RegisterFormState(Integer userNameError, Integer emailError, Integer pinCodeError, Integer passwordError, Integer cityError, Integer districtError, Integer stateError) {
        this.userNameError = userNameError;
        this.emailError = emailError;
        this.pinCodeError = pinCodeError;
        this.passwordError = passwordError;
        this.cityError = cityError;
        this.districtError = districtError;
        this.stateError = stateError;
        this.isDataValid = false;
    }

    public RegisterFormState(boolean isDataValid) {
        this.userNameError = null;
        this.emailError = null;
        this.pinCodeError = null;
        this.passwordError = null;
        this.cityError = null;
        this.districtError = null;
        this.stateError = null;
        this.isDataValid = isDataValid;
    }

    public Integer getUserNameError() {
        return userNameError;
    }

    public Integer getEmailError() {
        return emailError;
    }

    public Integer getPinCodeError() {
        return pinCodeError;
    }

    public Integer getPasswordError() {
        return passwordError;
    }

    public Integer getCityError() {
        return cityError;
    }

    public Integer getDistrictError() {
        return districtError;
    }

    public Integer getStateError() {
        return stateError;
    }

    public void setUserNameError(Integer userNameError) {
        this.userNameError = userNameError;
    }

    public void setEmailError(Integer emailError) {
        this.emailError = emailError;
    }

    public void setPinCodeError(Integer pinCodeError) {
        this.pinCodeError = pinCodeError;
    }

    public void setPasswordError(Integer passwordError) {
        this.passwordError = passwordError;
    }

    public void setCityError(Integer cityError) {
        this.cityError = cityError;
    }

    public void setDistrictError(Integer districtError) {
        this.districtError = districtError;
    }

    public void setStateError(Integer stateError) {
        this.stateError = stateError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }


}
