package com.example.hhfoundation.medicalHistory.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hhfoundation.Helper.*
import com.example.hhfoundation.databinding.ActivityViewMedicalHisBinding
import com.example.hhfoundation.medicalHistory.model.Medicalhistory
import com.example.hhfoundation.medicalHistory.model.ModelMedLIst
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewMedicalHis : AppCompatActivity() {
    private lateinit var binding: ActivityViewMedicalHisBinding
    lateinit var sessionManager: SessionManager
    private var patientId = ""
    private var age = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMedicalHisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@ViewMedicalHis)

        patientId = intent.getStringExtra("patient_id").toString()
        val dob = intent.getStringExtra("birthdate")
        Log.e("Before", dob.toString())


        if (dob != null) {
            try {
            var fDOb=""
            fDOb = if (dob.contains("-",ignoreCase = true)){
                changeDateFormat5(dob)
            } else{
                changeDateFormat6(dob)
            }
           //dd/MM/yyyy
             Log.e("after", dob.toString())

                age = DateUtils.getAgeFromDOB(fDOb)
                Log.e("dob", dob.toString())
                Log.e("Age", age)
                binding.AgeinYears.text=age
                binding.DateOfScreening.text= currentDate

//                if (age.toInt() < 6) {
//                    // binding.btnNext.text = "Submit"
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
            sessionManager.group.toString(),
            patientId
        ).enqueue(object :
            Callback<ModelMedLIst> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelMedLIst>,
                response: Response<ModelMedLIst>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@ViewMedicalHis, "Server Error")
                    } else if (response.code() == 404) {
                        myToast(this@ViewMedicalHis, "Something went wrong")
                    } else if (response.code() == 200) {

                        for (r in response.body()!!.medicalhistory) {
                            binding.AdmissionNumber.text = r.admissionnumber
                            binding.AadharNumber.text = r.aadharnumber
                            binding.tvname.text = r.name
                            binding.FatherName.text = r.f_name
                            binding.BloodGroup.text = r.bloodgroup
                            binding.BirthDate.text = r.birthdate
                            binding.MotherName.text = r.m_name
                            binding.PursuingClass.text = r.education
                            binding.tvPhone.text = r.phone
                            binding.School.text = r.schl
                            binding.SchoolEmail.text = r.schl_email
                            binding.SchoolDistrict.text = r.schl_dist
                            binding.SchoolAddress.text = r.schl_addr
                            binding.Gender.text = r.sex
                            binding.allergie.text = r.allergie
                            binding.anemmla.text = r.anemoio
                            binding.bitotSpot.text = r.bitooo
                            binding.Rickets.text = r.rickss
                            binding.Malnutrition.text = r.motrsds
                            binding.Goitre.text = r.goitrdd
                            binding.skinss.text = r.skinss
                            binding.ottyss.text = r.ottyss
                            binding.occurringinyourbody.text = r.difdsds
                            binding.smokingordrinking.text = r.drnksd
                            binding.depressedMost.text = r.trds
                            binding.MenstrualCycleStarted.text = r.cyclesd
                            binding.PeriodsEverymonths.text = r.periodsdas
                            binding.painds.text = r.painds
                            binding.areads.text = r.areasds
                            binding.feelds.text = r.feelsd
                            binding.binond.text = r.bhhiiosds
                            binding.heartdisease.text = r.heartss
                            binding.Respiratory.text = r.breathasa
                            binding.DentalConditions.text = r.dental
                            binding.Episodes.text = r.episss
                            binding.past.text = r.past
                            binding.heartin.text = r.heartin
                            binding.almedcine.text = r.almedcine
                            binding.ongoing.text = r.ongoing
                            binding.surgical.text = r.surgical
                            binding.hypothyroidin.text = r.hypothyroidin
                            binding.hyperthyroidin.text = r.hyperthyroidin
                            binding.diabetescx.text = r.diabetescx
                            binding.visions.text = r.visions
                            binding.hearrss.text = r.hearrss
                            binding.nerosdss.text = r.nerosdss
                            binding.motrsds.text = r.motrsds
                            binding.cogsa.text = r.cogsa
                            binding.lagsa.text = r.lagsa
                            binding.begaasds.text = r.begaasds
                            binding.learnsds.text = r.learnsds
                            binding.disorsd.text = r.disorsd

                        }

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

            override fun onFailure(call: Call<ModelMedLIst>, t: Throwable) {
                myToast(this@ViewMedicalHis, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
    }

}