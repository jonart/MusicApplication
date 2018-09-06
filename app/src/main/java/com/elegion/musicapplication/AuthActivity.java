package com.elegion.musicapplication;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class AuthActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return AuthFragment.newInstance();
    }
}
