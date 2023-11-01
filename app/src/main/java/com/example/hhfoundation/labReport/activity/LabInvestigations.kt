package com.example.hhfoundation.labReport.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityLabInvestigationsBinding
import com.example.hhfoundation.labReport.adapter.AdapterLabInv
import com.example.hhfoundation.labReport.adapter.AdapterLabReport
import com.example.hhfoundation.labReport.model.Labadvidetail
import com.example.hhfoundation.labReport.model.Labreport
import com.example.hhfoundation.labReport.model.ModelLabInv
import com.example.hhfoundation.labReport.model.ModelLabReport
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LabInvestigations : AppCompatActivity() {
    private lateinit var binding:ActivityLabInvestigationsBinding
    lateinit var sessionManager:SessionManager
    private lateinit var mainData: ArrayList<Labadvidetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLabInvestigationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@LabInvestigations)

        apiCallLabInvReportList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            try {
                edtSearch.addTextChangedListener { str ->
                    setRecyclerViewAdapter(mainData.filter {
                        it.patient!!.contains(str.toString(), ignoreCase = true)
                    } as ArrayList<Labadvidetail>)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun apiCallLabInvReportList() {

        AppProgressBar.showLoaderDialog(this@LabInvestigations)


        ApiClient.apiService.adviseLabDetails(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            )
            .enqueue(object : Callback<ModelLabInv> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelLabInv>, response: Response<ModelLabInv>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.labadvidetails
                        }
                        if (response.code() == 500) {
                            myToast(this@LabInvestigations, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.labadvidetails.isEmpty()) {
                            myToast(this@LabInvestigations, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@LabInvestigations, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelLabInv>, t: Throwable) {
                    myToast(this@LabInvestigations, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Labadvidetail>) {
        binding.recyclerView.apply {
            adapter = AdapterLabInv(this@LabInvestigations, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}