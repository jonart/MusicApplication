package com.elegion.musicapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elegion.musicapplication.model.ErrorRegistrationServer;
import com.elegion.musicapplication.model.User;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import retrofit2.HttpException;
import retrofit2.Response;

public class RegistrationFragment extends Fragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private EditText mEmail;
    private EditText mName;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mRegistration;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @SuppressLint("CheckResult")
        @Override
        public void onClick(View v) {
            if (isInputValid()) {


                User.DataBean dataBean = new User.DataBean(mEmail.getText().toString(), mName.getText().toString(), mPassword.getText().toString());
                Gson gson = new Gson();

                ApiUtils.getApiService(true)
                        .registration(dataBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            showMessage(R.string.registration_success);
                            getFragmentManager().popBackStack();
                        }, throwable -> {

                            Response<?> response = ((HttpException) throwable).response();

                            switch (response.code()) {
                                case 400:
                                    try {
                                        ErrorRegistrationServer errorRegistrationServer = gson.fromJson(response.errorBody().string(), ErrorRegistrationServer.class);

                                        if (errorRegistrationServer.getErrors().getEmail() != null) {
                                            mEmail.setError(errorRegistrationServer.getErrors().getEmail().toString());
                                        }
                                        if (errorRegistrationServer.getErrors().getName() != null) {
                                            mName.setError(errorRegistrationServer.getErrors().getName().toString());
                                        }
                                        if (errorRegistrationServer.getErrors().getPassword() != null) {
                                            mPassword.setError("Введите пароль, не менее 8 символов");
                                            mPasswordAgain.setError("Повторите пароль, не менее 8 символов");
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    showMessage(R.string.validation_error);
                                    break;
                                case 500:
                                    showMessage(R.string.internal_server_error);
                                    break;
                                default:
                                    showMessage(R.string.request_error);
                                    break;
                            }

                        });
            } else {
                showMessage(R.string.login_input_error);
            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);
        mEmail = view.findViewById(R.id.etLogin);
        mName = view.findViewById(R.id.etName);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordAgain = view.findViewById(R.id.tvPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegistration);

        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEmail.setError(null);
            }
        });
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mName.setError(null);
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPassword.setError(null);
            }
        });
        mPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPasswordAgain.setError(null);
            }
        });

        return view;
    }

    private boolean isInputValid() {
        String email = mEmail.getText().toString();
        if (isEmailValid(email) && isPasswordValid()) {
            return true;
        }
        return false;
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(passwordAgain);
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }
}
