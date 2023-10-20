package com.example.hhfoundation.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityProfileBinding
import com.example.hhfoundation.profile.model.ModelProfile
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<ModelProfile>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        sessionManager= SessionManager(this@Profile)
        apiCallGetProfile()
    }

    private fun apiCallGetProfile() {

        AppProgressBar.showLoaderDialog(this@Profile)


        ApiClient.apiService.getProfile(sessionManager.ionId.toString(),sessionManager.idToken.toString())
            .enqueue(object : Callback<ModelProfile> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelProfile>, response: Response<ModelProfile>
                ) {
                    try {
//                        if (response.code() == 200) {
//                            mainData = response.body()!!.patients
//                        }
                        if (response.code() == 500) {
                            myToast(this@Profile, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@Profile, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.tvid.text=response.body()!!.nurse.id
                            binding.tvname.text=response.body()!!.nurse.name
                            binding.tvEmail.text=response.body()!!.nurse.email
                            binding.tvAddress.text=response.body()!!.nurse.address
                            binding.tvPhone.text=response.body()!!.nurse.phone
                            binding.tvHospitalId.text=response.body()!!.nurse.hospital_id
                            binding.tvSchool.text=response.body()!!.nurse.school
                            binding.tvStrength.text=response.body()!!.nurse.school
                            binding.tvPrincipal.text=response.body()!!.nurse.principal
                             AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@Profile, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelProfile>, t: Throwable) {
                    myToast(this@Profile, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                    //apiCallGetProfile()

                }

            })
    }

}