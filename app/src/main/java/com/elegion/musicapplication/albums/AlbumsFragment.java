package com.elegion.musicapplication.albums;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.musicapplication.ApiUtils;
import com.elegion.musicapplication.App;
import com.elegion.musicapplication.R;
import com.elegion.musicapplication.album.DetailAlbumFragment;
import com.elegion.musicapplication.db.MusicDao;
import com.elegion.musicapplication.model.Album;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AlbumsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;

    @NonNull
    private final AlbumsAdapter mAlbumAdapter = new AlbumsAdapter(album -> {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DetailAlbumFragment().newInstance(album))
                .addToBackStack(DetailAlbumFragment.class.getSimpleName())
                .commit();
    });

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mRefresher = view.findViewById(R.id.refresher);
        mRefresher.setOnRefreshListener(this);
        mErrorView = view.findViewById(R.id.errorView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.albums);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAlbumAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        mRefresher.post(this::getAlbums);
    }

    @SuppressLint("CheckResult")
    private void getAlbums() {
        ApiUtils.getApiService(true).getAlbums()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(albums -> getMusicDao().insertAlbums(albums))
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        return getMusicDao().getAlbums();
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mRefresher.setRefreshing(true))
                .doFinally(() -> mRefresher.setRefreshing(false))
                .subscribe(
                        albums -> {
                            mErrorView.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            Collections.sort(albums, (o1, o2) -> Integer.compare(o1.getId(),o2.getId()));
                            mAlbumAdapter.addData(albums,true);
                        });
    }

    private MusicDao getMusicDao() {
        return ((App)getActivity().getApplication()).getDatabase().getMusicDao();
    }
}
