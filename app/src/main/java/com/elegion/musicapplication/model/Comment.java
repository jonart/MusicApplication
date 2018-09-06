package com.elegion.musicapplication.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Comment implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int mId;

    @ColumnInfo(name = "album_id")
    @SerializedName("album_id")
    private int mAlbumId;

    @ColumnInfo(name = "text")
    @SerializedName("text")
    private String mText;

    @ColumnInfo(name = "author")
    @SerializedName("author")
    private String mAuthor;

    @ColumnInfo(name = "timestamp")
    @SerializedName("timestamp")
    private String mTimestamp;


    public Comment(String text, int albumId) {
        mAlbumId = albumId;
        mText = text;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(int albumId) {
        mAlbumId = albumId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

    @Override
    public boolean equals(Object obj) {

        Comment comment = (Comment) obj;

        return (mId == comment.getId()
                && mAlbumId == comment.getAlbumId()
                && mText.equals(comment.getText())
                && mAuthor.equals(comment.getAuthor())
                && mTimestamp.equals(comment.getTimestamp()));
    }
}
