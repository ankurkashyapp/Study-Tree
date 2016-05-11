package com.mounica.studytree.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mounica.studytree.R;
import com.mounica.studytree.api.RestClient;
import com.mounica.studytree.api.response.Products;
import com.mounica.studytree.models.User;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ankur on 5/5/16.
 */
public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button userLogin;
    private Button userRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (User.getLoggedInUserId(this) != -1) {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        } else {
            initViews();
        }
    }

    private void initViews() {
        userLogin = (Button)findViewById(R.id.user_login);
        userRegister = (Button)findViewById(R.id.user_register);
        userLogin.setOnClickListener(this);
        userRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == userLogin) {
            Intent intent = new Intent(this, UserLoginActivity.class);
            intent.putExtra(UserLoginActivity.TAB_POSITION, 0);
            startActivity(intent);
        }
        else if (view == userRegister) {
            Intent intent = new Intent(this, UserLoginActivity.class);
            intent.putExtra(UserLoginActivity.TAB_POSITION, 1);
            startActivity(intent);
        }
    }
}
