package com.example.hhfoundation.followUpPrescription

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityDailyVitalUpdateBinding
import com.example.hhfoundation.followUpPrescription.adapter.AdapterDailyVital
import com.example.hhfoundation.followUpPrescription.model.ModelVital
import com.example.hhfoundation.followUpPrescription.model.Vitald
import com.example.hhfoundation.labReport.adapter.AdapterLabReport
import com.example.hhfoundation.labReport.model.Labreport
import com.example.hhfoundation.labReport.model.ModelLabReport
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyVitalUpdate : AppCompatActivity() {
    private lateinit var binding:ActivityDailyVitalUpdateBinding
    lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<Vitald>

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

//            try {
//                edtSearch.addTextChangedListener { str ->
//                    setRecyclerViewAdapter(mainData.filter {
//                        it.user_id!!.contains(str.toString(), ignoreCase = true)
//                    } as ArrayList<Vitald>)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//
//            }
        }
    }

    private fun apiCallLabReportList() {

        AppProgressBar.showLoaderDialog(this@DailyVitalUpdate)


        ApiClient.apiService.vitaldetail(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
        )
            .enqueue(object : Callback<ModelVital> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelVital>, response: Response<ModelVital>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.vitald
                        }
                        if (response.code() == 500) {
                            myToast(this@DailyVitalUpdate, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.vitald.isEmpty()) {
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


                override fun onFailure(call: Call<ModelVital>, t: Throwable) {
                    myToast(this@DailyVitalUpdate, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Vitald>) {
        binding.recyclerView.apply {
            adapter = AdapterDailyVital(this@DailyVitalUpdate, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}