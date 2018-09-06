package com.elegion.musicapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.elegion.musicapplication.model.User;

public class ProfileActivity extends AppCompatActivity {
    public static String USER_KEY = "USER_KEY";

    private TextView mLogin;
    private TextView mName;
    private User.DataBean mDataBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_profile);

        mLogin = findViewById(R.id.tv_email);
        mName = findViewById(R.id.tv_name);

        Bundle bundle = getIntent().getExtras();
        mDataBean = (User.DataBean) bundle.get(USER_KEY);
        mLogin.setText(mDataBean.getEmail());
        mName.setText(mDataBean.getName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionLogout:
                startActivity(new Intent(this,AuthActivity.class));
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
