package com.savory.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.data.SPClient;
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
    protected SPClient spClient;

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        spClient = new SPClient(this);
        if (spClient.retrieveSavoryToken() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        savoryClient = SavoryClient.get();
        loginClient = new LoginClient(savoryClient);

        loginClient.bindFacebookButton(facebookButton);
        loginClient.listen(new LoginListener() {
            @Override
            public void onLoginPending() {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage(getString(R.string.connecting));
                progressDialog.show();
            }

            @Override
            public void onSuccessfulLogin(SavoryToken savoryToken) {
                spClient.persistSavoryToken(savoryToken.getToken());
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onLoginCancelled() {
            }

            @Override
            public void onLoginError(Throwable throwable) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginClient.onLoginResult(requestCode, resultCode, data);
    }
}