package com.example.hhfoundation.medicalHistory.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityAllMedicalHistoryBinding
import com.example.hhfoundation.medicalHistory.adapter.AdapterMedicalHis
import com.example.hhfoundation.medicalHistory.model.Medicalhistory
import com.example.hhfoundation.medicalHistory.model.ModelMedicalHis
import com.example.hhfoundation.registration.model.Patient
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class AllMedicalHistory : AppCompatActivity() {
    private lateinit var binding: ActivityAllMedicalHistoryBinding
    private lateinit var mainData: ArrayList<Medicalhistory>
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllMedicalHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@AllMedicalHistory)
        mainData=ArrayList<Medicalhistory>()

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


        try {
            binding.edtSearch?.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData?.filter {
                    it?.patient_id!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<Medicalhistory>)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun apiCallMedicalList() {

        AppProgressBar.showLoaderDialog(this@AllMedicalHistory)


        ApiClient.apiService.getmedical(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
        )
            .enqueue(object : Callback<ModelMedicalHis> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMedicalHis>, response: Response<ModelMedicalHis>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.medicalhistory
                        }
                        if (response.code() == 500) {
                            myToast(this@AllMedicalHistory, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.medicalhistory.isEmpty()) {
                            myToast(this@AllMedicalHistory, "No Data Found")
                            AppProgressBar.hideLoaderDialog()
//
                        } else {


                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@AllMedicalHistory, "Exception")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelMedicalHis>, t: Throwable) {
                    myToast(this@AllMedicalHistory, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Medicalhistory>) {
        binding.recyclerView.apply {
            adapter = AdapterMedicalHis(this@AllMedicalHistory, data)
            AppProgressBar.hideLoaderDialog()

        }
    }

}