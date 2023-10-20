package com.example.hhfoundation.medicalHistory.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityAllMedicalHistoryBinding
import com.example.hhfoundation.medicalHistory.adapter.AdapterMedicalHis
import com.example.hhfoundation.medicalHistory.model.Medical
import com.example.hhfoundation.medicalHistory.model.ModelMedical
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class AllMedicalHistory : AppCompatActivity() {
    private lateinit var binding: ActivityAllMedicalHistoryBinding
    private lateinit var mainData: ArrayList<Medical>
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllMedicalHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager=SessionManager(this@AllMedicalHistory)
//        binding.btnView.setOnClickListener {
//            startActivity(Intent(this@AllMedicalHistory, MedicalHIstory::class.java))
//        }
//
//        binding.btnCase.setOnClickListener {
//            startActivity(Intent(this@AllMedicalHistory, StudentDetails::class.java))
//        }

        apiCallMedicalList()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.patient_name.contains(str.toString(), ignoreCase = true)
            } as ArrayList<Medical>)
        }

    }

    private fun apiCallMedicalList() {

        AppProgressBar.showLoaderDialog(this@AllMedicalHistory)


        ApiClient.apiService.getmedical(sessionManager.ionId.toString(),sessionManager.idToken.toString())
            .enqueue(object : Callback<ModelMedical> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMedical>, response: Response<ModelMedical>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.medical
                        }
                        if (response.code() == 500) {
                            myToast(this@AllMedicalHistory, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.medical.isEmpty()) {
                            myToast(this@AllMedicalHistory, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@AllMedicalHistory, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelMedical>, t: Throwable) {
                    myToast(this@AllMedicalHistory, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Medical>) {
        binding.recyclerView.apply {
            adapter = AdapterMedicalHis(this@AllMedicalHistory, data)
            AppProgressBar.hideLoaderDialog()

        }
    }

}