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

    protected var connectCallback : Call<Response<SavoryToken>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        this.savoryClient = SavoryClient.get()

        facebookButton.registerCallback(object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Toast.makeText(this@LoginActivity, "We did it!", Toast.LENGTH_SHORT).show()
                savoryClient.connect(result!!.accessToken.token.toString())
                        .enqueue(object : Callback<SavoryToken> {
                            override fun onResponse(call: Call<SavoryToken>?,
                                                    response: Response<SavoryToken>?) {
                                Log.i("LoginActivity", "We did it reddit!")
                                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                finish()
                            }

                            override fun onFailure(call: Call<SavoryToken>?,
                                                   t: Throwable?) {
                                Log.e("LoginActivity", t?.message ?: "Rip")
                            }
                        })
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }
        })
    }

    override fun onStop() {
        super.onStop()
        connectCallback?.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookButton.onFacebookLoginResult(requestCode, resultCode, data)
    }
}
