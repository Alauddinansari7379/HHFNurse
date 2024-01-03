package com.example.hhfoundation.IPCMS.activtiy

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.IPCMS.adapter.AdapterReferralDocHIs
import com.example.hhfoundation.IPCMS.model.ModelReferralsHis
import com.example.hhfoundation.IPCMS.model.Prescription
import com.example.hhfoundation.clinicalManagement.adapter.AdapterAppointmentList
import com.example.hhfoundation.clinicalManagement.model.ModelAppointList
import com.example.hhfoundation.clinicalManagement.model.PrescriptionList
import com.example.hhfoundation.databinding.ActivityReferralDocHisBinding
import com.example.hhfoundation.labReport.model.Prescriptiondetail
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferralDocHis : AppCompatActivity() {
    var context = this@ReferralDocHis
    val binding by lazy {
        ActivityReferralDocHisBinding.inflate(layoutInflater)
    }
    private lateinit var mainData: ArrayList<Prescription>
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sessionManager = SessionManager(this@ReferralDocHis)

        val ststues = intent.getStringExtra("History").toString()

        Log.e("ststues", ststues)

        if (ststues == "Dic") {
            apiCallDISChargeHis()
            binding.appCompatTextView2.text = "Referrals Discharge History"
        }else{
            apiCallDocHis()
        }

        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.p_hosp != null && it.p_hosp!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<Prescription>)
            }

        }

    }

    private fun apiCallDocHis() {

        AppProgressBar.showLoaderDialog(context)


        ApiClient.apiService.getDoctorHistory(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
        )
            .enqueue(object : Callback<ModelReferralsHis> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelReferralsHis>, response: Response<ModelReferralsHis>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.prescriptions
                        }
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.prescriptions.isEmpty()) {
                            myToast(context, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(context, "No Data Found")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelReferralsHis>, t: Throwable) {
                    myToast(context, "Something went wrong")
                    // apiCallTodayAppointmentList()
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun apiCallDISChargeHis() {

        AppProgressBar.showLoaderDialog(context)


        ApiClient.apiService.disChargehistory(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
        )
            .enqueue(object : Callback<ModelReferralsHis> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelReferralsHis>, response: Response<ModelReferralsHis>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.prescriptions
                        }
                        if (response.code() == 500) {
                            myToast(context, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.prescriptions.isEmpty()) {
                            myToast(context, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(context, "No Data Found")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelReferralsHis>, t: Throwable) {
                    myToast(context, "Something went wrong")
                    // apiCallTodayAppointmentList()
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Prescription>) {
        binding.recyclerView.apply {
            adapter = AdapterReferralDocHIs(context, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}