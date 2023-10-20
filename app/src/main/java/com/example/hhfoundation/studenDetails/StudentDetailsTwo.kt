package com.example.hhfoundation.studenDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.currentDate
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.databinding.ActivityStudentDetailsTwoBinding
import com.example.hhfoundation.medicalHistory.activity.AllMedicalHistory
import com.example.hhfoundation.registration.adapter.AdapterPatientList
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentDetailsTwo : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailsTwoBinding
    lateinit var sessionManager: SessionManager
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@StudentDetailsTwo)
        Log.e("AdapterPatientList.studentId",AdapterPatientList.studentId)
        binding.btnOk.setOnClickListener {
            StudentDetailsOne.VisionImpairment = binding.VisionImpairment.text.toString().trim()
            StudentDetailsOne.HearingImpairment = binding.HearingImpairment.text.toString().trim()
            StudentDetailsOne.motorImpairment = binding.motorImpairment.text.toString().trim()
            StudentDetailsOne.MotorDelay = binding.MotorDelay.text.toString().trim()
            StudentDetailsOne.LanguageDelay = binding.LanguageDelay.text.toString().trim()
            StudentDetailsOne.Autism = binding.Autism.text.toString().trim()
            StudentDetailsOne.LearningDisorder = binding.LearningDisorder.text.toString().trim()
            StudentDetailsOne.hyperactivityDisorder =
                binding.hyperactivityDisorder.text.toString().trim()
            StudentDetailsOne.Occurringinyourbody =
                binding.occurringinyourbody.text.toString().trim()
            StudentDetailsOne.Smokingordrinking = binding.smokingordrinking.text.toString().trim()
            StudentDetailsOne.Depressedmost = binding.depressedMost.text.toString().trim()
            StudentDetailsOne.MenstrualCycleStarted =
                binding.MenstrualCycleStarted.text.toString().trim()
            StudentDetailsOne.PeriodsEverymonths = binding.PeriodsEverymonths.text.toString().trim()

            apiCallAddMedicalHistory()
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun apiCallAddMedicalHistory() {
        AppProgressBar.showLoaderDialog(this@StudentDetailsTwo)

        ApiClient.apiService.addPatientMedical(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            AdapterPatientList.studentId,
            currentDate,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            StudentDetailsOne.anemia,
            StudentDetailsOne.BitotSpot,
            StudentDetailsOne.Rickets,
            StudentDetailsOne.Malnutrition,
            StudentDetailsOne.Goitre,
            StudentDetailsOne.Eczema,
            StudentDetailsOne.OtitisMedia,
            StudentDetailsOne.heartDisease,
            StudentDetailsOne.Respiratory,
            StudentDetailsOne.DentalConditions,
            StudentDetailsOne.Episodes,
            StudentDetailsOne.VisionImpairment,
            StudentDetailsOne.HearingImpairment,
            StudentDetailsOne.motorImpairment,
            StudentDetailsOne.MotorDelay,
            "",
            StudentDetailsOne.LanguageDelay,
            StudentDetailsOne.Autism,
            StudentDetailsOne.LearningDisorder,
            StudentDetailsOne.hyperactivityDisorder,
            StudentDetailsOne.Occurringinyourbody,
            StudentDetailsOne.Smokingordrinking,
            StudentDetailsOne.Depressedmost,
            StudentDetailsOne.MenstrualCycleStarted,
            StudentDetailsOne.PeriodsEverymonths,
            "",
            "",
            "",
            "",
        ).enqueue(object : Callback<ModelNewAppoint> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelNewAppoint>, response: Response<ModelNewAppoint>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@StudentDetailsTwo, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@StudentDetailsTwo, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.body()!!.message == "successful") {
                        myToast(this@StudentDetailsTwo, "${response.body()!!.message}")
                        startActivity(Intent(this@StudentDetailsTwo, AllMedicalHistory::class.java))
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@StudentDetailsTwo, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@StudentDetailsTwo, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                apiCallAddMedicalHistory()
                //  myToast(this@ProfileActivity, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
    }

}