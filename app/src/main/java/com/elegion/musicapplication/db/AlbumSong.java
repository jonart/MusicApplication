package com.elegion.musicapplication.db;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.elegion.musicapplication.model.Album;
import com.elegion.musicapplication.model.Song;

@Entity(indices = {@Index(value = {"album_id", "song_id"}, unique = true)},
        foreignKeys = {
                @ForeignKey(entity = Album.class, parentColumns = "id",childColumns = "album_id"),
                @ForeignKey(entity = Song.class,parentColumns = "id",childColumns = "song_id")
})

public class AlbumSong {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "album_id")
    public int albumId;

    @ColumnInfo(name = "song_id")
    public int songId;


    public AlbumSong() {
    }

    public AlbumSong(int albumId, int songId) {
        this.albumId = albumId;
        this.songId = songId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
