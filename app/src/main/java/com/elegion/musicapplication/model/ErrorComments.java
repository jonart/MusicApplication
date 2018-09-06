package com.elegion.musicapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorComments {

    @SerializedName("text")
    private List<String> mText;
    @SerializedName("album_id")
    private List<String> mAlbumId;

    public List<String> getText() {
        return mText;
    }

    public void setText(List<String> text) {
        mText = text;
    }

    public List<String> getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(List<String> albumId) {
        mAlbumId = albumId;
    }
}
