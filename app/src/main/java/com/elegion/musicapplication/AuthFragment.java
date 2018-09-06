package com.elegion.musicapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elegion.musicapplication.albums.AlbumsActivity;
import com.elegion.musicapplication.model.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView mEmail;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;
    private SharedPreferencesHelper mSharedPreferencesHelper = App.getSharedPreferencesHelper();

    public static AuthFragment newInstance() {

        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @SuppressLint("CheckResult")
        @Override
        public void onClick(View v) {
            if (isEmailValid() && isPasswordValid()) {



                ApiUtils.getBasicAuthClient(mEmail.getText().toString(), mPassword.getText().toString(), true);
                ApiUtils.getApiService(true)
                        .authorization()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((User user) -> {
                            //    User.DataBean dataUser = response.body().getData();
                            mSharedPreferencesHelper.addUser(mEmail.getText().toString(),mPassword.getText().toString());
                            startActivity(new Intent(getActivity(), AlbumsActivity.class));
                            getActivity().finish();
                        }, throwable -> {
                            showMessage(R.string.auth_error);
                            //showMessage(R.string.request_error));
                        });
            } else {
                showMessage(R.string.login_input_error);
            }
        }
    };

    private View.OnClickListener mOnRegisterClickListener = v -> getFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, RegistrationFragment.newInstance())
            .addToBackStack(RegistrationFragment.class.getName())
            .commit();

    private View.OnFocusChangeListener mOnLoginFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (!(Build.VERSION.SDK_INT == Build.VERSION_CODES.O)){
                    mEmail.showDropDown();
                }
            }
        }
    };


    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(mPassword.getText());
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mEmail = v.findViewById(R.id.etLogin);
        mEmail.setOnFocusChangeListener(mOnLoginFocusChangeListener);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.buttonEnter);
        mRegister = v.findViewById(R.id.buttonRegister);


        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
        return v;
    }
}
