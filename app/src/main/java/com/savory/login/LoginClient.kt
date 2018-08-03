package com.savory.login

import android.content.Intent
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.savory.api.SavoryClient
import com.savory.api.models.SavoryToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginClient(val savoryClient: SavoryClient) {

    interface LoginListener {
        fun onSuccessfulLogin(savoryToken : SavoryToken)
        fun onLoginCancelled()
        fun onLoginError(throwable: Throwable)
    }

    val facebookClient = FacebookClient()
    var listener : LoginListener? = null
    var savoryTokenCall : Call<SavoryToken>? = null

    val facebookLoginCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            val localSavoryTokenCall = savoryClient.connect(result!!.accessToken.token.toString());
            savoryTokenCall = localSavoryTokenCall
            localSavoryTokenCall.enqueue(object : Callback<SavoryToken> {
                override fun onResponse(call: Call<SavoryToken>,
                                        response: Response<SavoryToken>) {
                    if (response.isSuccessful()) {
                        listener?.onSuccessfulLogin(response.body()!!)
                    } else {
                        listener?.onLoginError(Exception("server error"))
                    }
                }

                override fun onFailure(call: Call<SavoryToken>, t: Throwable) {
                    listener?.onLoginError(t)
                }
            })
        }

        override fun onCancel() {
            listener?.onLoginCancelled()
        }

        override fun onError(error: FacebookException) {
            listener?.onLoginError(error)
        }
    }

    init {
        facebookClient.registerCallback(facebookLoginCallback)
    }

    fun bindFacebookButton(facebookButton: FacebookButton) {
        facebookClient.bind(facebookButton)
    }

    fun listen(listener: LoginListener) {
        this.listener = listener
    }

    fun onLoginResult(requestCode: Int, resultCode: Int, data: Intent) {
        facebookClient.onLoginResult(requestCode, resultCode, data)
    }
}