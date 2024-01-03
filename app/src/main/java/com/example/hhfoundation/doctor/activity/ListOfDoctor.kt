package com.example.hhfoundation.doctor.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityListOfDoctorBinding
import com.example.hhfoundation.doctor.adapter.AdapterDoctorList
import com.example.hhfoundation.doctor.model.Doctor
import com.example.hhfoundation.doctor.model.ModelDoctorList
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ListOfDoctor : AppCompatActivity() {
    private lateinit var binding: ActivityListOfDoctorBinding
    private lateinit var mainData: ArrayList<Doctor>
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@ListOfDoctor)
        mainData = ArrayList<Doctor>()
        apiCallDoctorList()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.name != null && it.name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<Doctor>)
        }

        binding.btnAddNew.setOnClickListener {
            startActivity(Intent(this@ListOfDoctor, AddDoctor::class.java))
        }

    }

    private fun apiCallDoctorList() {

        AppProgressBar.showLoaderDialog(this@ListOfDoctor)
        ApiClient.apiService.doctorlist(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
        )
            .enqueue(object : Callback<ModelDoctorList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDoctorList>, response: Response<ModelDoctorList>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.doctors
                        }
                        if (response.code() == 500) {
                            myToast(this@ListOfDoctor, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.doctors.isEmpty()) {
                            myToast(this@ListOfDoctor, "No Data Found")
                            AppProgressBar.hideLoaderDialog()
//
                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@ListOfDoctor, "Exception")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelDoctorList>, t: Throwable) {
                    myToast(this@ListOfDoctor, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Doctor>) {
        binding.recyclerView.apply {
            adapter = AdapterDoctorList(this@ListOfDoctor, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}