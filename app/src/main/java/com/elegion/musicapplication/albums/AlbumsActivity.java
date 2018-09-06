package com.elegion.musicapplication.albums;

import android.support.v4.app.Fragment;

import com.elegion.musicapplication.SingleFragmentActivity;

public class AlbumsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return AlbumsFragment.newInstance();
    }
}
