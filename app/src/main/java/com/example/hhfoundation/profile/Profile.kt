package com.example.hhfoundation.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        sessionManager = SessionManager(this@Profile)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        if (sessionManager.group == "Doctor") {
            binding.layoutDoctor.visibility = View.VISIBLE
        }

        if (sessionManager.group == "Nurse") {
            binding.layoutNurse.visibility = View.VISIBLE
        }

        if (sessionManager.group == "Pharmacist") {
            binding.layoutHosId.visibility = View.GONE
            binding.no.text="No"
            binding.name.text="Name"
            binding.email.text="Email"
            binding.mobile.text="Mobile Number"
        }

        apiCallGetProfile()
    }

    private fun apiCallGetProfile() {

        AppProgressBar.showLoaderDialog(this@Profile)


        ApiClient.apiService.getProfile(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
        )
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
                            binding.tvid.text = response.body()!!.nurse.id
                            binding.tvname.text = response.body()!!.nurse.name
                            binding.tvEmail.text = response.body()!!.nurse.email
                            binding.tvAddress.text = response.body()!!.nurse.address
                            binding.tvPhone.text = response.body()!!.nurse.phone
                            binding.tvHospitalId.text = response.body()!!.nurse.hospital_id
                            binding.tvSchool.text = response.body()!!.nurse.school
                            binding.tvStrength.text = response.body()!!.nurse.school
                            binding.tvPrincipal.text = response.body()!!.nurse.principal
                            binding.tvDepartment.text = response.body()!!.nurse.department
                            binding.tvRegNo.text = response.body()!!.nurse.reg_no
                            binding.tvProfile.text = response.body()!!.nurse.profile



                            if (sessionManager.group == "Receptionist") {
                                binding.tvnameSp.text = response.body()!!.nurse.name
                                binding.tvEmailSp.text = response.body()!!.nurse.email
                                binding.tvAddressSP.text = response.body()!!.nurse.address
                                binding.tvPhoneSp.text = response.body()!!.nurse.phone
                                binding.tvCouncelorTypeSP.text = response.body()!!.nurse.u_type
                                binding.CouncelorSchool.text = response.body()!!.nurse.school
                                binding.layoutReceptionist.visibility = View.VISIBLE
                                binding.layoutDefalut.visibility = View.GONE
                            }


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