package com.example.hhfoundation.registration.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityStudentBonoFileldBinding
import com.example.hhfoundation.registration.adapter.AdapterPatientList
import com.example.hhfoundation.registration.model.ModelPatientList
import com.example.hhfoundation.registration.model.Patient
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class StudentBonoFileld : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBonoFileldBinding
    private lateinit var mainData: ArrayList<Patient>
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStudentBonoFileldBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager= SessionManager(this@StudentBonoFileld)
        with(binding){
            imgBack.setOnClickListener {
                onBackPressed()
            }

            btnAddNew.setOnClickListener {
                startActivity(Intent(this@StudentBonoFileld,Registration::class.java))
            }
        }



        apiCallPatientList()
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<Patient>)
        }

    }
    private fun apiCallPatientList() {

        AppProgressBar.showLoaderDialog(this@StudentBonoFileld)


        ApiClient.apiService.patientList(sessionManager.ionId.toString(),sessionManager.idToken.toString())
            .enqueue(object : Callback<ModelPatientList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPatientList>, response: Response<ModelPatientList>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.patients
                        }
                        if (response.code() == 500) {
                            myToast(this@StudentBonoFileld, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.patients.isEmpty()) {
                             myToast(this@StudentBonoFileld,"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@StudentBonoFileld, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelPatientList>, t: Throwable) {
                    myToast(this@StudentBonoFileld, "Something went wrong")
                     AppProgressBar.hideLoaderDialog()

                }

            })
    }
    private fun setRecyclerViewAdapter(data: ArrayList<Patient>) {
        binding.recyclerView.apply {
             adapter = AdapterPatientList(this@StudentBonoFileld, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}