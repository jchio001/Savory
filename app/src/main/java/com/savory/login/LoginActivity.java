package com.savory.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.savory.R;
import com.savory.api.clients.savory.SavoryClient;
import com.savory.api.clients.savory.models.SavoryToken;
import com.savory.data.SharedPreferencesClient;
import com.savory.home.HomeActivity;
import com.savory.login.LoginClient.LoginListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.facebook_button) FacebookButton facebookButton;

    private SavoryClient savoryClient;
    private LoginClient loginClient;
    protected SharedPreferencesClient SharedPreferencesClient;

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        SharedPreferencesClient = new SharedPreferencesClient(this);
        if (SharedPreferencesClient.retrieveSavoryToken() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }

        savoryClient = SavoryClient.get();
        loginClient = new LoginClient(savoryClient);

        loginClient.bindFacebookButton(facebookButton);
        loginClient.listen(new LoginListener() {
            @Override
            public void onLoginPending() {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage(getString(R.string.connecting));
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onSuccessfulLogin(SavoryToken savoryToken) {
                progressDialog.dismiss();
                SharedPreferencesClient.persistSavoryToken(savoryToken.getToken());
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
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginClient.onLoginResult(requestCode, resultCode, data);
    }
}
