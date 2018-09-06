package com.elegion.musicapplication;

import com.elegion.musicapplication.model.Album;
import com.elegion.musicapplication.model.Comment;
import com.elegion.musicapplication.model.CommentResponse;
import com.elegion.musicapplication.model.Song;
import com.elegion.musicapplication.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AcademyApi {

    @POST("registration")
    Completable registration(@Body User.DataBean dataBean);

    @GET("user")
    Single<User> authorization();

    //Albums

    @GET("albums")
    Single<List<Album>> getAlbums();

    @GET("albums/{id}")
    Single<Album> getAlbum(@Path("id") int id);

    //Songs

    @GET("songs")
    Call<List<Song>> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);


    // Comments

    @GET("comments")
    Single<List<Comment>> getAllCommentsComments();

    @GET("comments/{id}")
    Single<Comment> getCommentById(@Path("id") int id);

    @GET("albums/{id}/comments")
    Single<List<Comment>> getCommentFromAlbum(@Path("id") int id);

    @POST("comments")
    Single<CommentResponse> sendComment(@Body Comment comment);

}
