package com.example.hhfoundation.clinicalManagement.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.adapter.AdapterAppointmentList
import com.example.hhfoundation.clinicalManagement.model.ModelAppointList
import com.example.hhfoundation.clinicalManagement.model.PrescriptionList
import com.example.hhfoundation.databinding.ActivityTodayAppointmentBinding
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodayAppointment : AppCompatActivity() {
    private lateinit var binding:ActivityTodayAppointmentBinding
    lateinit var sessionManager:SessionManager
    private lateinit var mainData: ArrayList<PrescriptionList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTodayAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@TodayAppointment)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallTodayAppointmentList()

        try {
            binding.edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.doctor.contains(str.toString(), ignoreCase = true)
                } as ArrayList<PrescriptionList>)
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
    private fun apiCallTodayAppointmentList() {

        AppProgressBar.showLoaderDialog(this@TodayAppointment)


        ApiClient.apiService.todayAppointments(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
         )
            .enqueue(object : Callback<ModelAppointList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointList>, response: Response<ModelAppointList>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.prescription
                        }
                        if (response.code() == 500) {
                            myToast(this@TodayAppointment, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.prescription.isEmpty()) {
                            myToast(this@TodayAppointment, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@TodayAppointment, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelAppointList>, t: Throwable) {
                    myToast(this@TodayAppointment, t.message.toString())
                    apiCallTodayAppointmentList()
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<PrescriptionList>) {
        binding.recyclerView.apply {
            adapter = AdapterAppointmentList(this@TodayAppointment, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}