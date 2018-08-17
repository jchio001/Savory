package com.savory.login;

import android.content.Intent;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.savory.api.clients.savory.SavoryClient;
import com.savory.api.clients.savory.models.SavoryToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginClient {

    interface LoginListener {
        void onLoginPending();
        void onSuccessfulLogin(SavoryToken savoryToken);
        void onLoginCancelled();
        void onLoginError(Throwable throwable);
    }

    protected SavoryClient savoryClient;

    protected Call<SavoryToken> savoryTokenCall;

    protected LoginListener listener;

    private FacebookClient facebookClient = new FacebookClient();
    private FacebookCallback<LoginResult> fbLoginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            if (listener != null) {
                listener.onLoginPending();
                savoryTokenCall = savoryClient.connect(
                    loginResult.getAccessToken().getToken());
                savoryTokenCall.enqueue(new Callback<SavoryToken>() {
                    @Override
                    public void onResponse(Call<SavoryToken> call,
                                           Response<SavoryToken> response) {
                        if (listener != null) {
                            if (response.isSuccessful()) {
                                listener.onSuccessfulLogin(response.body());
                            } else {
                                listener.onLoginError(new Exception("server error"));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SavoryToken> call, Throwable t) {

                    }
                });
            }
        }

        @Override
        public void onCancel() {
            if (listener != null) {
                listener.onLoginCancelled();
            }
        }

        @Override
        public void onError(FacebookException error) {
            if (listener != null) {
                listener.onLoginError(error);
            }
        }
    };

    public LoginClient(SavoryClient savoryClient) {
        this.savoryClient = savoryClient;
        facebookClient.registerCallback(fbLoginCallback);
    }

    void bindFacebookButton(FacebookButton facebookButton) {
        facebookClient.bind(facebookButton);
    }

    void listen(LoginListener listener) {
        this.listener = listener;
    }

    void onLoginResult(int requestCode, int resultCode, Intent data) {
        facebookClient.onLoginResult(requestCode, resultCode, data);
    }
}
