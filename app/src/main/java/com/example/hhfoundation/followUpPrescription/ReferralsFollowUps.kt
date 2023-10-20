package com.example.hhfoundation.followUpPrescription

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.databinding.ActivityReferralsFollowUpsBinding
import com.example.hhfoundation.followUpPrescription.adapter.AdapterReferrals
import com.example.hhfoundation.followUpPrescription.adapter.AdapterReferralsFollow
import com.example.hhfoundation.followUpPrescription.model.ModelReferralsFollow
import com.example.hhfoundation.followUpPrescription.model.ModelRefrreals
import com.example.hhfoundation.followUpPrescription.model.PrescriptionX
import com.example.hhfoundation.followUpPrescription.model.Prescriptiondetail
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferralsFollowUps : AppCompatActivity() {
    private lateinit var binding:ActivityReferralsFollowUpsBinding
    lateinit var sessionManager:SessionManager
    private lateinit var mainData: ArrayList<PrescriptionX>
   // private lateinit var modelReferralsFollow: ModelReferralsFollow

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityReferralsFollowUpsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@ReferralsFollowUps)


        apiCallReferralsFollowList()
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

//            edtSearch.addTextChangedListener { str ->
//                setRecyclerViewAdapter(mainData.filter {
//                    it.doctorname!!.contains(str.toString(), ignoreCase = true)
//                } as ArrayList<PrescriptionX>)
//            }
       }
    }

    private fun apiCallReferralsFollowList() {

        AppProgressBar.showLoaderDialog(this@ReferralsFollowUps)

        ApiClient.apiService.getReferralsFollow(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
        ).enqueue(object : Callback<ModelReferralsFollow> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelReferralsFollow>,
                response: Response<ModelReferralsFollow>
            ) {
                try {
                    if (response.code() == 200) {
                        mainData =response.body()!!.prescriptions
                    }
                    if (response.code() == 500) {
                        myToast(this@ReferralsFollowUps, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.body()==null) {
                        myToast(this@ReferralsFollowUps, "No Data Found")
                        AppProgressBar.hideLoaderDialog()

                    } else {

                        binding.recyclerView.apply {
                            adapter = AdapterReferralsFollow(this@ReferralsFollowUps,response.body()!!)
                            AppProgressBar.hideLoaderDialog()

                        }
                       // setRecyclerViewAdapter(mainData)
                        AppProgressBar.hideLoaderDialog()


                    }
                } catch (e: Exception) {
                    myToast(this@ReferralsFollowUps, "Something went wrong")
                    e.printStackTrace()
                    AppProgressBar.hideLoaderDialog()

                }
            }


            override fun onFailure(call: Call<ModelReferralsFollow>, t: Throwable) {
                myToast(this@ReferralsFollowUps, t.message.toString())
                AppProgressBar.hideLoaderDialog()

            }

        })
    }

//    private fun setRecyclerViewAdapter(data:ArrayList<PrescriptionX>) {
//        binding.recyclerView.apply {
//            adapter = AdapterReferralsFollow(this@ReferralsFollowUps,data)
//            AppProgressBar.hideLoaderDialog()
//
//        }
 //   }
}