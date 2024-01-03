package com.example.hhfoundation.IPCMS.activtiy

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityDailyVitalUpdateBinding
import com.example.hhfoundation.IPCMS.adapter.AdapterDailyVital
import com.example.hhfoundation.IPCMS.model.ModelVitalList
import com.example.hhfoundation.IPCMS.model.Vitalddetail
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyVitalUpdate : AppCompatActivity() {
    private lateinit var binding:ActivityDailyVitalUpdateBinding
    lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<Vitalddetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDailyVitalUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager=SessionManager(this@DailyVitalUpdate)


        apiCallLabReportList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            try {
                edtSearch.addTextChangedListener { str ->
                    setRecyclerViewAdapter(mainData.filter {
                        it.patientname != null && it.patientname!!.contains(str.toString(), ignoreCase = true)
                    } as ArrayList<Vitalddetail>)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun apiCallLabReportList() {

        AppProgressBar.showLoaderDialog(this@DailyVitalUpdate)


        ApiClient.apiService.vitaldetail(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            )
            .enqueue(object : Callback<ModelVitalList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelVitalList>, response: Response<ModelVitalList>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.vitalddetails
                        }
                        if (response.code() == 500) {
                            myToast(this@DailyVitalUpdate, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.vitalddetails.isEmpty()) {
                            myToast(this@DailyVitalUpdate, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(this@DailyVitalUpdate, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }
                override fun onFailure(call: Call<ModelVitalList>, t: Throwable) {
                    myToast(this@DailyVitalUpdate, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Vitalddetail>) {
        binding.recyclerView.apply {
            adapter = AdapterDailyVital(this@DailyVitalUpdate, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}