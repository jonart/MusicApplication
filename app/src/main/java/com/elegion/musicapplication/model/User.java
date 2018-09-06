package com.elegion.musicapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User {

    @SerializedName("data")
    private DataBean mData;

    public DataBean getData() {
        return mData;
    }

    public void setData(DataBean data) {
        mData = data;
    }

    public static class DataBean implements Serializable {

        @SerializedName("id")
        private int mId;
        @SerializedName("name")
        private String mName;
        @SerializedName("password")
        private String mPassword;
        @SerializedName("email")
        private String mEmail;

        private boolean mHasSuccessLogin;

        public DataBean(String email, String name, String password) {
            mEmail = email;
            mName = name;
            mPassword = password;
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String email) {
            mEmail = email;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        // прочитать ниже
        public boolean hasSuccessLogin() {
            return mHasSuccessLogin;
        }

        public void setHasSuccessLogin(boolean hasSuccessLogin) {
            mHasSuccessLogin = hasSuccessLogin;
        }
    }
}
