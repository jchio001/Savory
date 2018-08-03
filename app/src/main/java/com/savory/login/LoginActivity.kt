package com.savory.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.savory.R
import com.savory.api.SavoryClient
import com.savory.api.models.SavoryToken
import com.savory.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.facebook_button)
    lateinit var facebookButton : FacebookButton

    protected lateinit var savoryClient : SavoryClient

    protected val loginClient = LoginClient(SavoryClient.get())
    protected var connectCallback : Call<Response<SavoryToken>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        this.savoryClient = SavoryClient.get()

        loginClient.bindFacebookButton(facebookButton)
        loginClient.listen(object : LoginClient.LoginListener {
            override fun onSuccessfulLogin(savoryToken: SavoryToken) {
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            }

            override fun onLoginCancelled() {
            }

            override fun onLoginError(throwable: Throwable) {
            }
        })
    }

    override fun onStop() {
        super.onStop()
        connectCallback?.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        loginClient.onLoginResult(requestCode, resultCode, data)
    }
}
