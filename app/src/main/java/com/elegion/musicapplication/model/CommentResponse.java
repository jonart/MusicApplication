package com.elegion.musicapplication.model;

import com.google.gson.annotations.SerializedName;

public class CommentResponse {

    @SerializedName("id")
    private int mId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
