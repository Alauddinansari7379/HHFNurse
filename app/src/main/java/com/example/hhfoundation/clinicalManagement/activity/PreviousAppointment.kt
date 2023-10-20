package com.example.hhfoundation.clinicalManagement.activity

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.adapter.AdapterAppointmentList
import com.example.hhfoundation.clinicalManagement.model.ModelAppointList
import com.example.hhfoundation.clinicalManagement.model.PrescriptionList
import com.example.hhfoundation.databinding.ActivityAppointmentBinding
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreviousAppointment : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentBinding
    lateinit var sessionManager: SessionManager
    private lateinit var mainData: ArrayList<PrescriptionList>
    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@PreviousAppointment)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        apiCallAppointmentList("")

        try {
            binding.edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.doctor.contains(str.toString(), ignoreCase = true)
                } as ArrayList<PrescriptionList>)
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }


        binding.layMen.setOnClickListener {
            apiCallAppointmentList(status)
            binding.layMen.setBackgroundResource(R.drawable.corner_main_color)
            binding.tvMale.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tvFemale.setTextColor(Color.parseColor("#777777"))
            binding.tvOther.setTextColor(Color.parseColor("#777777"))
            binding.tvTreated.setTextColor(Color.parseColor("#777777"))
            binding.layWomen.setBackgroundResource(R.drawable.corner)
            binding.layOther.setBackgroundResource(R.drawable.corner)
            binding.layTreated.setBackgroundResource(R.drawable.corner)


        }
        binding.layWomen.setOnClickListener {
            apiCallAppointmentList("Pending Confirmation")
            binding.layWomen.setBackgroundResource(R.drawable.corner_main_color)
            binding.tvFemale.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tvOther.setTextColor(Color.parseColor("#777777"))
            binding.tvMale.setTextColor(Color.parseColor("#777777"))
            binding.tvTreated.setTextColor(Color.parseColor("#777777"))
            binding.layMen.setBackgroundResource(R.drawable.corner);
            binding.layOther.setBackgroundResource(R.drawable.corner);
            binding.layTreated.setBackgroundResource(R.drawable.corner)


        }
        binding.layOther.setOnClickListener {
            apiCallAppointmentList("Confirmed")
            binding.layOther.setBackgroundResource(R.drawable.corner_main_color)
            binding.tvOther.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tvFemale.setTextColor(Color.parseColor("#777777"))
            binding.tvMale.setTextColor(Color.parseColor("#777777"))
            binding.tvTreated.setTextColor(Color.parseColor("#777777"))
            binding.layMen.setBackgroundResource(R.drawable.corner)
            binding.layWomen.setBackgroundResource(R.drawable.corner)
            binding.layTreated.setBackgroundResource(R.drawable.corner)


        }

        binding.layTreated.setOnClickListener {
            apiCallAppointmentList("Treated")
            binding.layTreated.setBackgroundResource(R.drawable.corner_main_color)
            binding.tvTreated.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tvFemale.setTextColor(Color.parseColor("#777777"))
            binding.tvMale.setTextColor(Color.parseColor("#777777"))
            binding.tvOther.setTextColor(Color.parseColor("#777777"))
            binding.layMen.setBackgroundResource(R.drawable.corner)
            binding.layWomen.setBackgroundResource(R.drawable.corner)
            binding.layOther.setBackgroundResource(R.drawable.corner);


        }
    }

    private fun apiCallAppointmentList(status: String) {

        AppProgressBar.showLoaderDialog(this@PreviousAppointment)


        ApiClient.apiService.previousappoitments(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            status
        )
            .enqueue(object : Callback<ModelAppointList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointList>, response: Response<ModelAppointList>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.prescription
                        }
                        if (response.code() == 500) {
                            myToast(this@PreviousAppointment, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.prescription.isEmpty()) {
                            myToast(this@PreviousAppointment, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@PreviousAppointment, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelAppointList>, t: Throwable) {
                    myToast(this@PreviousAppointment, t.message.toString())
                    apiCallAppointmentList(status)
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<PrescriptionList>) {
        binding.recyclerView.apply {
            adapter = AdapterAppointmentList(this@PreviousAppointment, data)
            AppProgressBar.hideLoaderDialog()

        }
    }
}