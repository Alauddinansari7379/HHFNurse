package com.example.hhfoundation.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.dasboard.Dashboard
import com.example.hhfoundation.databinding.ActivityLoginBinding
import com.example.hhfoundation.login.model.ModelLogin
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private val context: Context = this@Login
    private lateinit var binding: ActivityLoginBinding
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@Login)

        if (sessionManager.isLogin) {
            startActivity(Intent(context, Dashboard::class.java))
            finish()
        }
        with(binding) {
            btnLogIn.setOnClickListener {
                if (edtUserName.text!!.isEmpty()) {
                    edtUserName.error = "Enter Username"
                    edtUserName.requestFocus()
                    return@setOnClickListener
                }
                if (edtPassword.text!!.isEmpty()) {
                    edtPassword.error = "Enter Password"
                    edtPassword.requestFocus()
                    return@setOnClickListener
                }
                login()

            }


        }

    }

    private fun login() {

        AppProgressBar.showLoaderDialog(this@Login)
        ApiClient.apiService.login(
            binding.edtUserName.text.toString().trim(),
            binding.edtPassword.text.toString().trim(),
         ).enqueue(object :
            Callback<ModelLogin> {
            @SuppressLint("LogNotTimber", "LongLogTag")
            override fun onResponse(
                call: Call<ModelLogin>,
                response: Response<ModelLogin>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Login, "Server Error")
                    } else if (response.code() == 404) {
                        myToast(this@Login, "Something went wrong")
                    } else if (response.body()!!.message.contentEquals("successful")) {
                        sessionManager.isLogin = true
                        sessionManager.userId = response.body()!!.user_id
                        sessionManager.group = response.body()!!.group
                        sessionManager.usertype = response.body()!!.user_type
                        sessionManager.hospitalId = response.body()!!.hospital_id
                        sessionManager.ionId = response.body()!!.ion_id
                        sessionManager.idToken = response.body()!!.idToken

                        Log.e("sessionManager.idToken",sessionManager.idToken.toString())
                        Log.e("sessionManager.ionID",sessionManager.ionId.toString())
                        Log.e("sessionManager.group",sessionManager.group.toString())
                        if (sessionManager.group=="Receptionist") {
                            Log.e("response.body()!!.user_type", response.body()!!.user_type)
                        }
                        myToast(this@Login, response.body()!!.message)
                        val intent = Intent(applicationContext, Dashboard::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                        AppProgressBar.hideLoaderDialog()
                    } else {
                         myToast(this@Login, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Login, "Try Again")
                    AppProgressBar.hideLoaderDialog()

                }

            }

            override fun onFailure(call: Call<ModelLogin>, t: Throwable) {
                myToast(this@Login, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
    }
}