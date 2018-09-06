package com.elegion.musicapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.elegion.musicapplication.model.Album;
import com.elegion.musicapplication.model.Comment;
import com.elegion.musicapplication.model.Song;

@Database(entities = {Album.class, Song.class, AlbumSong.class, Comment.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract MusicDao getMusicDao();
}
