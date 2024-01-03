package com.example.hhfoundation.clinicalManagement.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityPrescriptionBinding
import com.example.hhfoundation.clinicalManagement.adapter.AdapterPrescription
import com.example.hhfoundation.labReport.model.ModelPrescription
import com.example.hhfoundation.labReport.model.ModelUpload
import com.example.hhfoundation.labReport.model.Prescriptiondetail
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Prescription : AppCompatActivity(),AdapterPrescription.Favourite {
    private lateinit var binding: ActivityPrescriptionBinding
    private lateinit var mainData: ArrayList<Prescriptiondetail>
    private lateinit var sessionManager: SessionManager
    var countRF=0
    var countaddF=0
    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainData=ArrayList<Prescriptiondetail>()

        sessionManager = SessionManager(this@Prescription)

        apiCallPrescriptionList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }


                 edtSearch.addTextChangedListener { str ->
                    setRecyclerViewAdapter(mainData.filter {
                        it.patientname != null && it.patientname!!.contains(str.toString(), ignoreCase = true)
                    } as ArrayList<Prescriptiondetail>)
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
                    count++
                    if (count <= 2) {
                        Log.e("count", count.toString())
                        apiCallPrescriptionList()
                    } else {
                        myToast(this@Prescription, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Prescriptiondetail>) {
        binding.recyclerView.apply {
            adapter = AdapterPrescription(this@Prescription, data,this@Prescription)
            AppProgressBar.hideLoaderDialog()

        }
    }

    override fun addFav(pid: String) {
        AppProgressBar.showLoaderDialog(this@Prescription)
        ApiClient.apiService.addFav(
            pid,
        )
            .enqueue(object : Callback<ModelUpload> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelUpload>, response: Response<ModelUpload>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@Prescription, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@Prescription, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@Prescription, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            apiCallPrescriptionList()
                        }
                    } catch (e: Exception) {
                        myToast(this@Prescription, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
                    countaddF++
                    if (countaddF <= 2) {
                        Log.e("count", countaddF.toString())
                        addFav(pid)
                    } else {
                        myToast(this@Prescription, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })    }

    override fun removeFav(pid: String) {
        AppProgressBar.showLoaderDialog(this@Prescription)
        ApiClient.apiService.removeFav(
            pid,
        )
            .enqueue(object : Callback<ModelUpload> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelUpload>, response: Response<ModelUpload>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@Prescription, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@Prescription, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@Prescription, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            apiCallPrescriptionList()
                        }
                    } catch (e: Exception) {
                        myToast(this@Prescription, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
                    countRF++
                    if (countRF <= 2) {
                        Log.e("count", countRF.toString())
                        addFav(pid)
                    } else {
                        myToast(this@Prescription, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })

    }
}