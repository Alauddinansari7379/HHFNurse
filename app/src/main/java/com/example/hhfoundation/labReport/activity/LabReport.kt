package com.example.hhfoundation.labReport.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityLabReportBinding
import com.example.hhfoundation.labReport.adapter.AdapterLabReport
import com.example.hhfoundation.labReport.model.Labreport
import com.example.hhfoundation.labReport.model.ModelLabReport
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LabReport : AppCompatActivity() {
    private lateinit var binding:ActivityLabReportBinding
    lateinit var sessionManager:SessionManager
    private lateinit var mainData: ArrayList<Labreport>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLabReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@LabReport)

        apiCallLabReportList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            try {
                edtSearch.addTextChangedListener { str ->
                    setRecyclerViewAdapter(mainData.filter {
                        it.name!!.contains(str.toString(), ignoreCase = true)
                    } as ArrayList<Labreport>)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun apiCallLabReportList() {

        AppProgressBar.showLoaderDialog(this@LabReport)


        ApiClient.apiService.attachlabreport(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
        )
            .enqueue(object : Callback<ModelLabReport> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelLabReport>, response: Response<ModelLabReport>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.labreports
                        }
                        if (response.code() == 500) {
                            myToast(this@LabReport, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.labreports.isEmpty()) {
                            myToast(this@LabReport, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@LabReport, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelLabReport>, t: Throwable) {
                    myToast(this@LabReport, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Labreport>) {
        binding.recyclerView.apply {
            adapter = AdapterLabReport(this@LabReport, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}