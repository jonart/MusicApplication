package com.elegion.musicapplication.albums;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elegion.musicapplication.R;
import com.elegion.musicapplication.model.Album;

class AlbumsHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;
    private TextView mReleaseDate;

    public AlbumsHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.tv_title);
        mReleaseDate = itemView.findViewById(R.id.tv_release_date);
    }

    public void bind(Album item, AlbumsAdapter.OnItemClickListener onItemClickListener){
        mTitle.setText(item.getName());
        mReleaseDate.setText(item.getReleaseDate());
        if (onItemClickListener != null){
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
        }
    }
}
