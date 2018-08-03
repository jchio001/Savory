package com.savory.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.savory.R;
import com.savory.api.SavoryClient;
import com.savory.api.models.SavoryToken;
import com.savory.home.HomeActivity;
import com.savory.login.LoginClient.LoginListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.facebook_button) FacebookButton facebookButton;

    private SavoryClient savoryClient;
    private LoginClient loginClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        savoryClient = SavoryClient.get();
        loginClient = new LoginClient(savoryClient);

        loginClient.bindFacebookButton(facebookButton);
        loginClient.listen(new LoginListener() {
            @Override
            public void onSuccessfulLogin(SavoryToken savoryToken) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }

            @Override
            public void onLoginCancelled() {
            }

            @Override
            public void onLoginError(Throwable throwable) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginClient.onLoginResult(requestCode, resultCode, data);
    }
}
