package com.elegion.musicapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.elegion.musicapplication.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
//    public static final String USERS_KEY = "USERS_KEY";
    public static final String LOGIN_KEY = "LOGIN_KEY";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";
//    public static final Type USERS_TYPE = new TypeToken<List<User.DataBean>>(){
//
//    }.getType();
    private SharedPreferences mSharedPreferences;
    //private Gson mGson = new Gson();

    public SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

//    public List<User.DataBean> getUsers(){
//        List<User.DataBean> users = mGson.fromJson(mSharedPreferences.getString(USERS_KEY,""), USERS_TYPE);
//        return users == null ? new ArrayList<User.DataBean>() : users;
//    }

//    public boolean addUser(User.DataBean user){
//        List<User.DataBean> users = getUsers();
//        for (User.DataBean u :users){
//            if (u.getEmail().equalsIgnoreCase(user.getEmail())){
//                return false;
//            }
//        }
//        users.add(user);
//        mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
//        return true;
//    }

        //Прочитать ниже

//    public boolean saveOrOverrideUser(User.DataBean user) {
//        List<User.DataBean> users = getUsers();
//        for (User.DataBean u : users) {
//            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
//                users.remove(u);
//                break;
//            }
//        }
//        users.add(user);
//        mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
//        return true;
//    }
//
//    public List<String> getSuccessLogins() {
//        List<String> successLogins = new ArrayList<>();
//        List<User.DataBean> allUsers = getUsers();
//        for (User.DataBean user : allUsers) {
//            if (user.hasSuccessLogin()) {
//                successLogins.add(user.getEmail());
//            }
//        }
//        return successLogins;
//    }
//
//    public User.DataBean login(String login, String password) {
//        List<User.DataBean> users = getUsers();
//        for (User.DataBean u : users) {
//            if (login.equalsIgnoreCase(u.getEmail())
//                    && password.equals(u.getPassword())) {
//                u.setHasSuccessLogin(true);
//                mSharedPreferences.edit().putString(USERS_KEY, mGson.toJson(users, USERS_TYPE)).apply();
//                return u;
//            }
//        }
//        return null;
//    }

    public void addUser(String name, String password){
        mSharedPreferences.edit()
                .putString(LOGIN_KEY, name)
                .putString(PASSWORD_KEY,password)
                .apply();
    }

    public String[] getUser(){
        String login = mSharedPreferences.getString(LOGIN_KEY,"");
        String password = mSharedPreferences.getString(PASSWORD_KEY, "");
        String[] user = {login,password};
        return user;
    }
}
