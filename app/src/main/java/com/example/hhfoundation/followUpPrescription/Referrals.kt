package com.example.hhfoundation.followUpPrescription

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.databinding.ActivityReferralsBinding
import com.example.hhfoundation.followUpPrescription.adapter.AdapterReferrals
import com.example.hhfoundation.followUpPrescription.model.ModelRefrreals
import com.example.hhfoundation.followUpPrescription.model.Prescriptiondetail
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Referrals : AppCompatActivity() {
    private lateinit var binding: ActivityReferralsBinding
    private lateinit var mainData: ArrayList<Prescriptiondetail>
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityReferralsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@Referrals)

        apiCallReferralsList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.patientname!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<Prescriptiondetail>)
            }
        }
    }

    private fun apiCallReferralsList() {

        AppProgressBar.showLoaderDialog(this@Referrals)

        ApiClient.apiService.getReferrals(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
        )
            .enqueue(object : Callback<ModelRefrreals> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelRefrreals>, response: Response<ModelRefrreals>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.prescriptiondetails
                        }
                        if (response.code() == 500) {
                            myToast(this@Referrals, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.prescriptiondetails.isEmpty()) {
                            myToast(this@Referrals, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@Referrals, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelRefrreals>, t: Throwable) {
                    myToast(this@Referrals, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<Prescriptiondetail>) {
        binding.recyclerView.apply {
            adapter = AdapterReferrals(this@Referrals, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}