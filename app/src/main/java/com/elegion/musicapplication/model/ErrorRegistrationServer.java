package com.elegion.musicapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorRegistrationServer {

    @SerializedName("errors")
    private ErrorsBean mErrors;

    public ErrorsBean getErrors() {
        return mErrors;
    }

    public void setErrors(ErrorsBean errors) {
        mErrors = errors;
    }

    public static class ErrorsBean {

        @SerializedName("email")
        private List<String> mEmail;
        @SerializedName("name")
        private List<String> mName;
        @SerializedName("password")
        private List<String> mPassword;

        public List<String> getEmail() {
            return mEmail;
        }

        public void setEmail(List<String> email) {
            mEmail = email;
        }

        public List<String> getName() {
            return mName;
        }

        public void setName(List<String> name) {
            mName = name;
        }

        public List<String> getPassword() {
            return mPassword;
        }

        public void setPassword(List<String> password) {
            mPassword = password;
        }
    }
}
