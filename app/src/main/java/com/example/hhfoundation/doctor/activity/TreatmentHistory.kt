package com.example.hhfoundation.doctor.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
 import com.example.hhfoundation.databinding.ActivityTreatmentHistoryBinding

import com.example.hhfoundation.doctor.adapter.AdapterDoctorList
import com.example.hhfoundation.doctor.adapter.AdapterTreatmentList
import com.example.hhfoundation.doctor.model.*
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TreatmentHistory : AppCompatActivity() {
    private lateinit var binding:ActivityTreatmentHistoryBinding
    lateinit var sessionManager:SessionManager
    private lateinit var mainData: ArrayList<Appointment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTreatmentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@TreatmentHistory)
        mainData = ArrayList<Appointment>()
        apiCallDoctorList()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.doctor_name.contains(str.toString(), ignoreCase = true)
            } as ArrayList<Appointment>)
        }

        binding.btnAddNew.setOnClickListener {
            startActivity(Intent(this@TreatmentHistory, AddDoctor::class.java))
        }

    }

    private fun apiCallDoctorList() {

        AppProgressBar.showLoaderDialog(this@TreatmentHistory)
        ApiClient.apiService.treatmentReport(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            "",
            ""

        )
            .enqueue(object : Callback<ModelTretment> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelTretment>, response: Response<ModelTretment>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.appoitdetails
                        }
                        if (response.code() == 500) {
                            myToast(this@TreatmentHistory, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.appoitdetails.isEmpty()) {
                            myToast(this@TreatmentHistory, "No Data Found")
                            AppProgressBar.hideLoaderDialog()
//
                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(this@TreatmentHistory, "Exception")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelTretment>, t: Throwable) {
                    myToast(this@TreatmentHistory, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Appointment>) {
        binding.recyclerView.apply {
            adapter = AdapterTreatmentList(this@TreatmentHistory, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}