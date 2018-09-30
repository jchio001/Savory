package com.savory.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

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
    @BindView(R.id.agreement_text) TextView agreementText;

    protected SavoryClient savoryClient;
    private LoginClient loginClient;
    protected SharedPreferencesClient sharedPreferencesClient;

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        agreementText.setMovementMethod(LinkMovementMethod.getInstance());
        agreementText.setText(Html.fromHtml(getString(R.string.agreement_terms)));

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

                String token = savoryToken.getToken();
                savoryClient.setSavoryToken(token);
                sharedPreferencesClient.persistSavoryToken(token);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onLoginCancelled() {}

            @Override
            public void onLoginError(Throwable throwable) {
                progressDialog.dismiss();

                // Proceed to home anyways since login is currently broken
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });
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
