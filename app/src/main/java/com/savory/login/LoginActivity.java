package com.savory.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.savory.R;
import com.savory.api.clients.savory.SavoryClient;
import com.savory.api.clients.savory.models.SavoryToken;
import com.savory.data.SharedPreferencesClient;
import com.savory.home.HomeActivity;
import com.savory.login.LoginClient.LoginListener;
import com.savory.ui.SimpleBlockingProgressDialog;
import com.savory.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements UserAgreementDialog.Listener {

    @BindView(R.id.facebook_button) FacebookButton facebookButton;

    protected SavoryClient savoryClient;
    private LoginClient loginClient;
    protected SharedPreferencesClient sharedPreferencesClient;

    private UserAgreementDialog userAgreementDialog;
    protected SimpleBlockingProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        userAgreementDialog = new UserAgreementDialog(this, this);
        progressDialog = new SimpleBlockingProgressDialog(this, R.string.logging_you_in);

        savoryClient = SavoryClient.get();
        loginClient = new LoginClient(savoryClient);

        sharedPreferencesClient = new SharedPreferencesClient(this);
        String savoryToken = sharedPreferencesClient.retrieveSavoryToken();
        if (savoryToken != null) {
            savoryClient.setSavoryToken(savoryToken);
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }

        loginClient.setListener(new LoginListener() {
            @Override
            public void onLoginPending() {
                progressDialog.show();
            }

            @Override
            public void onSuccessfulLogin(SavoryToken savoryToken) {
                progressDialog.dismiss();

                String token = savoryToken.getToken();
                savoryClient.setSavoryToken(token);
                sharedPreferencesClient.persistSavoryToken(token);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onLoginCancelled() {}

            @Override
            public void onLoginError() {
                progressDialog.dismiss();
                UIUtils.showLongToast(R.string.login_fail, LoginActivity.this);
            }
        });
    }

    @OnClick(R.id.facebook_button)
    public void onFacebookLoginClicked() {
        userAgreementDialog.show();
    }

    @Override
    public void onUserAgreementAccepted() {
        loginClient.loginWithFacebook(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginClient.onLoginResult(requestCode, resultCode, data);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
    }
}
