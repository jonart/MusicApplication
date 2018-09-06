package com.elegion.musicapplication.album;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.musicapplication.ApiUtils;
import com.elegion.musicapplication.App;
import com.elegion.musicapplication.R;
import com.elegion.musicapplication.comments.CommentsFragment;
import com.elegion.musicapplication.db.AlbumSong;
import com.elegion.musicapplication.db.MusicDao;
import com.elegion.musicapplication.model.Album;
import com.elegion.musicapplication.model.Song;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DetailAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ALBUM_KEY = "ALBUM_KEY";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;
    private Album mAlbum;

    @NonNull
    private final SongsAdapter mSongsAdapter = new SongsAdapter();

    public static DetailAlbumFragment newInstance(Album album) {
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_KEY, album);

        DetailAlbumFragment fragment = new DetailAlbumFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mRefresher = view.findViewById(R.id.refresher);
        mRefresher.setOnRefreshListener(this);
        mErrorView = view.findViewById(R.id.errorView);
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

        getActivity().setTitle(mAlbum.getName());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mSongsAdapter);

        onRefresh();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.actionComments:
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CommentsFragment().newInstance(mAlbum.getId()))
                        .addToBackStack(CommentsFragment.class.getSimpleName())
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mRefresher.post(() -> {
            mRefresher.setRefreshing(true);
            getAlbum();
        });
    }

    @SuppressLint("CheckResult")
    private void getAlbum() {

        ApiUtils.getApiService(true).getAlbum(mAlbum.getId())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(album -> {
                    getMusicDao().insertSongs(album.getSongs());

                    List<Song> songs = album.getSongs();
                    for (Song song : songs) {
                        //todo: переделать на список
                        getMusicDao().insertAlbumSong(new AlbumSong(mAlbum.getId(), song.getId()));
                    }
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {

                        List<Song> songs = getMusicDao().getSongsFromAlbum(mAlbum.getId());
                        mAlbum.setSongs(songs);
                        return mAlbum;
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mRefresher.setRefreshing(false))
                .subscribe(album -> {
                    mErrorView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mSongsAdapter.addData(album.getSongs(), true);

                });
    }

    private MusicDao getMusicDao() {
        return ((App) getActivity().getApplication()).getDatabase().getMusicDao();
    }

}
