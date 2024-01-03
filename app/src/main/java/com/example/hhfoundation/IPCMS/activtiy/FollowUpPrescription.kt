package com.example.hhfoundation.IPCMS.activtiy

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityFollowUpPrescriptionBinding
import com.example.hhfoundation.IPCMS.adapter.AdapterFollowUp
import com.example.hhfoundation.IPCMS.model.ModelFollowUp
import com.example.hhfoundation.labReport.model.ModelUpload
import com.example.hhfoundation.labReport.model.Prescriptiondetail
import com.example.hhfoundation.retrofit.ApiClient

import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowUpPrescription : AppCompatActivity(), AdapterFollowUp.Favourite {
    private lateinit var binding: ActivityFollowUpPrescriptionBinding
    private lateinit var mainData: ArrayList<Prescriptiondetail>
    lateinit var sessionManager: SessionManager
    var countRe=0
    var countaddF=0
    var countRF=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowUpPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@FollowUpPrescription)

        apiCallFollowUpList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            try{
            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.patientname != null && it.patientname!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<Prescriptiondetail>)
            }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun apiCallFollowUpList() {

        AppProgressBar.showLoaderDialog(this@FollowUpPrescription)

        ApiClient.apiService.followUpList(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
        )
            .enqueue(object : Callback<ModelFollowUp> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelFollowUp>, response: Response<ModelFollowUp>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.prescriptiondetails
                        }
                        if (response.code() == 500) {
                            myToast(this@FollowUpPrescription, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.prescriptiondetails.isEmpty()) {
                            myToast(this@FollowUpPrescription, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@FollowUpPrescription, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelFollowUp>, t: Throwable) {
                    countRe++
                    if (countRe <= 2) {
                        Log.e("count", countRe.toString())
                        apiCallFollowUpList()
                    } else {
                        myToast(this@FollowUpPrescription, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Prescriptiondetail>) {
        binding.recyclerView.apply {
            adapter = AdapterFollowUp(this@FollowUpPrescription, data, this@FollowUpPrescription)
            AppProgressBar.hideLoaderDialog()

        }
    }

    override fun addFav(pid: String) {
        AppProgressBar.showLoaderDialog(this@FollowUpPrescription)
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
                            myToast(this@FollowUpPrescription, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@FollowUpPrescription, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@FollowUpPrescription, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            apiCallFollowUpList()
                        }
                    } catch (e: Exception) {
                        myToast(this@FollowUpPrescription, "Something went wrong")
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
                        myToast(this@FollowUpPrescription, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })
    }

    override fun removeFav(pid: String) {
        AppProgressBar.showLoaderDialog(this@FollowUpPrescription)
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
                            myToast(this@FollowUpPrescription, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@FollowUpPrescription, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@FollowUpPrescription, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            apiCallFollowUpList()
                        }
                    } catch (e: Exception) {
                        myToast(this@FollowUpPrescription, "Something went wrong")
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
                        myToast(this@FollowUpPrescription, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }

                }

            })
    }


}