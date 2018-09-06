package com.elegion.musicapplication.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.elegion.musicapplication.model.Album;
import com.elegion.musicapplication.model.Comment;
import com.elegion.musicapplication.model.Song;

import java.util.List;

@Dao
public interface MusicDao {

    //Album
//    ---------------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Query("SELECT * from album")
    List<Album> getAlbums();

    //удалить альбом
    @Delete
    void deleteAlbum(Album album);

    //удалить альбом по id
    @Query("DELETE FROM album where id = :albumId")
    void deleteAlbumById(int albumId);
// ------------------------------------------------------------


    //Song
//    ---------------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Query("SELECT * from song")
    List<Song> getSongs();

    //удалить песню
    @Delete
    void deleteSong(Song song);

    //удалить песню по id
    @Query("DELETE FROM song where id = :songId")
    void deleteSongById(int songId);
// ------------------------------------------------------------


    // ===== region ========AlbumSong
//    ---------------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumSongs(List<AlbumSong> albumSongs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumSong(AlbumSong albumSongs);

    @Query("SELECT * from albumsong")
    List<AlbumSong> getAlbumSong();

    @Query("select * from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumId")
    List<Song> getSongsFromAlbum(int albumId);

    //удалить песню
//    @Delete
//    void deleteAlbumSong(AlbumSong albumSong);

    //удалить песню по id
    @Query("DELETE FROM albumsong where id = :albumSongId")
    void deleteAlbumSongById(int albumSongId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinksAlbumSongs(List<AlbumSong> linksAlbumSongs);

// endregion ------------------------------------------------------------


    // Comment -----------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<Comment> comments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment comment);

    @Query("SELECT * from Comment")
    List<Comment> getComments();

    @Query("SELECT * from Comment WHERE album_id = :albumId")
    List<Comment> getCommentsById(int albumId);

    //удалить коментарий
    @Delete
    void deleteSong(Comment comment);

    //удалить коментарий по id
    @Query("DELETE FROM Comment where id = :commentsId")
    void deleteCommentsById(int commentsId);


    //---------------------------------------------------

}
