package com.elegion.musicapplication.comments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elegion.musicapplication.ApiUtils;
import com.elegion.musicapplication.App;
import com.elegion.musicapplication.R;
import com.elegion.musicapplication.db.MusicDao;
import com.elegion.musicapplication.model.Comment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;

public class CommentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String COMMENTS_KEY = "COMMENTS_KEY";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;
    private EditText mEditText;
    private Button mButton;
    private int position;
    private boolean wasOnce = false;

    @NonNull
    private final CommentsAdapter mCommentsAdapter = new CommentsAdapter();

    public static CommentsFragment newInstance(int incomePosition) {
        Bundle args = new Bundle();
        args.putInt(COMMENTS_KEY, incomePosition);

        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler_comment);
        mRefresher = view.findViewById(R.id.refresher_comment);
        mRefresher.setOnRefreshListener(this);
        mEditText = view.findViewById(R.id.et_comment);
        mButton = view.findViewById(R.id.btn_sendComment);


        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (!mEditText.getText().toString().isEmpty()) {
                    sendComment(mEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Нет текста для отправки", Toast.LENGTH_LONG).show();
                }
            }
            return false;
        });

        mButton.setOnClickListener(v -> {

                    if (!mEditText.getText().toString().isEmpty()) {
                        sendComment(mEditText.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "Нет текста для отправки", Toast.LENGTH_LONG).show();
                    }
                }
        );

        mErrorView = view.findViewById(R.id.errorView_comment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        position = getArguments().getInt(COMMENTS_KEY);
//
        //getActivity().setTitle(mComment.getAlbumId());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mCommentsAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mRefresher.post(() -> {
            mRefresher.setRefreshing(true);
            getComments();
        });
    }

    @SuppressLint("CheckResult")
    private void getComments() {
        ApiUtils.getApiService(true).getCommentFromAlbum(position)
                .subscribeOn(Schedulers.io())
                .doOnSuccess(comments -> getMusicDao().insertComments(comments))
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        return getMusicDao().getCommentsById(position);
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mRefresher.setRefreshing(true))
                .doFinally(() -> {
                    mRefresher.setRefreshing(false);
                    mRecyclerView.scrollToPosition(mCommentsAdapter.getItemCount() - 1);
                })
                .subscribe(
                        comments -> {
                            if (isNetworkAvailable()) {
                                boolean isChanged = isDataChanged(comments);
                                if (wasOnce) {
                                    if (isChanged) {
                                        Toast.makeText(getActivity(), "Комментарии обновлены", Toast.LENGTH_SHORT).show();
                                        mRecyclerView.scrollToPosition(mCommentsAdapter.getItemCount() - 1);
                                    }
                                    if (!isChanged) {
                                        Toast.makeText(getActivity(), "Новых комментариев нет", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getActivity(), "Подключение к интернет отсутствует", Toast.LENGTH_SHORT).show();
                            }

                            if (comments.isEmpty()) {
                                mErrorView.setVisibility(View.VISIBLE);
                                mRefresher.setRefreshing(true);
                                mRecyclerView.setVisibility(View.GONE);
                            } else {
                                mErrorView.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mCommentsAdapter.addCommentsData(comments, true);
                                wasOnce = true;
                            }
                        });
    }


    @SuppressLint("CheckResult")
    private void sendComment(String textComment) {
        Comment comment = new Comment(textComment, position);

        ApiUtils.getApiService(false).sendComment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((commentResponse) -> {
                            mRefresher.setRefreshing(true);
                            ApiUtils.getApiService(false).getCommentById(commentResponse.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSuccess(comment12 -> {
                                        mCommentsAdapter.addCommentData(comment12);
                                        getMusicDao().insertComment(comment12);
                                    })
                                    .doFinally(() -> {
                                        onRefresh();
                                    })
                                    .subscribe(comment1 -> {
                                    }, throwable -> {
                                    });
                            mEditText.setText("");
                        }, throwable -> {
                            Response<?> response = ((HttpException) throwable).response();
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), R.string.validation_error, Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), R.string.not_auth, Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), R.string.internal_server_error, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                );
    }


    private boolean isDataChanged(List<Comment> comments) {
        List<Comment> list = mCommentsAdapter.getComments();
        return !comments.equals(list);
    }

    private MusicDao getMusicDao() {
        return ((App) getActivity().getApplication()).getDatabase().getMusicDao();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) return true;
        else return false;
    }

}