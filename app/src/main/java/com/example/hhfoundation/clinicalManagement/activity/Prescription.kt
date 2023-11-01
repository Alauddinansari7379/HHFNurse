package com.example.hhfoundation.clinicalManagement.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityPrescriptionBinding
import com.example.hhfoundation.clinicalManagement.adapter.AdapterPrescription
import com.example.hhfoundation.labReport.model.ModelPrescription
import com.example.hhfoundation.labReport.model.Prescriptiondetail
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Prescription : AppCompatActivity() {
    private lateinit var binding: ActivityPrescriptionBinding
    private lateinit var mainData: ArrayList<Prescriptiondetail>
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@Prescription)

        apiCallPrescriptionList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            try {
                edtSearch.addTextChangedListener { str ->
                    setRecyclerViewAdapter(mainData.filter {
                        it.patientname!!.contains(str.toString(), ignoreCase = true)
                    } as ArrayList<Prescriptiondetail>)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun apiCallPrescriptionList() {

        AppProgressBar.showLoaderDialog(this@Prescription)


        ApiClient.apiService.prescriptionlist(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),

            )
            .enqueue(object : Callback<ModelPrescription> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrescription>, response: Response<ModelPrescription>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.prescriptiondetails
                        }
                        if (response.code() == 500) {
                            myToast(this@Prescription, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.prescriptiondetails.isEmpty()) {
                            myToast(this@Prescription, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@Prescription, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelPrescription>, t: Throwable) {
                    myToast(this@Prescription, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Prescriptiondetail>) {
        binding.recyclerView.apply {
            adapter = AdapterPrescription(this@Prescription, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}