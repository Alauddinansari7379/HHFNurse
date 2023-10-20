package com.example.hhfoundation.medicalHistory.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.dasboard.model.ModelDashboard
import com.example.hhfoundation.databinding.ActivityViewMedicalHisBinding
import com.example.hhfoundation.medicalHistory.model.ModelViewMedical
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewMedicalHis : AppCompatActivity() {
    private lateinit var binding: ActivityViewMedicalHisBinding
    lateinit var sessionManager: SessionManager
    private var patientId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMedicalHisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@ViewMedicalHis)
        patientId = intent.getStringExtra("patient_id").toString()
        binding.btnOk.setOnClickListener {
            onBackPressed()
        }

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallViewMedicalInfo()

    }

    private fun apiCallViewMedicalInfo() {

        AppProgressBar.showLoaderDialog(this@ViewMedicalHis)
        ApiClient.apiService.viewMedicalInfo(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            patientId
        ).enqueue(object :
            Callback<ModelViewMedical> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelViewMedical>,
                response: Response<ModelViewMedical>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@ViewMedicalHis, "Server Error")
                    } else if (response.code() == 404) {
                        myToast(this@ViewMedicalHis, "Something went wrong")
                    } else if (response.code() == 200) {

                        binding.tvid.text = response.body()!!.id.toString()
                        binding.tvname.text = response.body()!!.patient_name.toString()
                        binding.patientAddress.text = response.body()!!.patient_address.toString()
                        binding.tvPhone.text = response.body()!!.patient_phone.toString()
                        binding.tvHospitalId.text = response.body()!!.hospital_id.toString()
                        binding.date.text = response.body()!!.date.toString()
                        binding.height1.text = response.body()!!.height1.toString()
                        binding.weight.text = response.body()!!.weight1.toString()
                        binding.allergie.text = response.body()!!.allergie.toString()
                        binding.anemmla.text = response.body()!!.anemoio.toString()
                        binding.bitotSpot.text = response.body()!!.bitooo.toString()
                        binding.Rickets.text = response.body()!!.rickss.toString()
                        binding.Malnutrition.text = response.body()!!.motrsds.toString()
                        binding.Goitre.text = response.body()!!.goitrdd.toString()
                        binding.Eczema.text = response.body()!!.episss.toString()
                        binding.OtitisMedia.text = response.body()!!.ottyss.toString()
                        binding.heartdisease.text = response.body()!!.heartss.toString()
                        binding.Respiratory.text = response.body()!!.breathasa.toString()
                        binding.DentalConditions.text = response.body()!!.dental.toString()
                        binding.Episodes.text = response.body()!!.episss.toString()

                         AppProgressBar.hideLoaderDialog()
                    } else {
                          AppProgressBar.hideLoaderDialog()


                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@ViewMedicalHis, "Something went wrong")
                     AppProgressBar.hideLoaderDialog()

                }

            }

            override fun onFailure(call: Call<ModelViewMedical>, t: Throwable) {
                myToast(this@ViewMedicalHis, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
    }

}