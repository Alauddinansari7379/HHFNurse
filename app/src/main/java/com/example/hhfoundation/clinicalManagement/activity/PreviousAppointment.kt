package com.example.hhfoundation.clinicalManagement.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.adapter.AdapterAppointmentList
import com.example.hhfoundation.clinicalManagement.model.ModelAppointList
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.clinicalManagement.model.PrescriptionList
import com.example.hhfoundation.databinding.ActivityAppointmentBinding
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class PreviousAppointment : AppCompatActivity(),AdapterAppointmentList.Information {
    private lateinit var binding: ActivityAppointmentBinding
    lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
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

        if (sessionManager.group=="Doctor"){
            binding.layWomen.visibility=View.GONE
        }

        apiCallAppointmentList("")

        try {
            binding.edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.patient.contains(str.toString(), ignoreCase = true)
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
            sessionManager.group.toString(),
            status
        )
            .enqueue(object : Callback<ModelAppointList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointList>, response: Response<ModelAppointList>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.appoitmentdetails
                        }
                        if (response.code() == 500) {
                            myToast(this@PreviousAppointment, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.appoitmentdetails.isEmpty()) {
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
            adapter = AdapterAppointmentList(this@PreviousAppointment, data,this@PreviousAppointment)
            AppProgressBar.hideLoaderDialog()

        }
    }



    override fun info(id: String) {
       // popupUpload(id)
        apiCallAppointmentInfo(id)

    }

    private fun apiCallAppointmentInfo(id: String) {

        AppProgressBar.showLoaderDialog(this@PreviousAppointment)


        ApiClient.apiService.appoitmentinfo(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            id
        )
            .enqueue(object : Callback<ModelNewAppoint> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelNewAppoint>, response: Response<ModelNewAppoint>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@PreviousAppointment, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.nurse_id.isEmpty()) {
                            myToast(this@PreviousAppointment, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view = layoutInflater.inflate(R.layout.dialog_appointment_info, null)
                            dialog = Dialog(this@PreviousAppointment)
                            var appointmentTypeDil = view!!.findViewById<TextView>(R.id.AppointmentTypeDil)
                            val studentDil = view!!.findViewById<TextView>(R.id.StudentDil)
                            val departmentDil = view!!.findViewById<TextView>(R.id.DepartmentDil)
                            val bloodPressureDil = view!!.findViewById<TextView>(R.id.BloodPressureDil)
                            val pRDil = view!!.findViewById<TextView>(R.id.PRDil)
                            val temperatureDilil = view!!.findViewById<TextView>(R.id.TemperatureDil)
                            val sPO2Dil = view!!.findViewById<TextView>(R.id.SPO2Dil)
                            val randomBloodSugarDil = view!!.findViewById<TextView>(R.id.RandomBloodSugarDil)
                            val presentComplainsDil = view!!.findViewById<TextView>(R.id.PresentComplainsDil)
                            val sickDateDil = view!!.findViewById<TextView>(R.id.SickDateDil)
                            val remarksDil = view!!.findViewById<TextView>(R.id.RemarksDil)
                            val imgClose = view!!.findViewById<ImageView>(R.id.imgCloseDil)

                            appointmentTypeDil.text=response.body()!!.appotype
                            studentDil.text=response.body()!!.patient
                          //  departmentDil.text=response.body()!!.d
                            bloodPressureDil.text=response.body()!!.bp
                            pRDil.text=response.body()!!.pr
                            temperatureDilil.text=response.body()!!.temp
                            sPO2Dil.text=response.body()!!.satur
                            randomBloodSugarDil.text=response.body()!!.ranbl
                            presentComplainsDil.text=response.body()!!.complain
                            sickDateDil.text=response.body()!!.sdate
                            remarksDil.text=response.body()!!.remarks


                            dialog = Dialog(this@PreviousAppointment)
                            if (view.parent != null) {
                                (view.parent as ViewGroup).removeView(view) // <- fix
                            }
                            dialog!!.setContentView(view)
                            dialog?.setCancelable(true)

                            dialog?.show()

                            imgClose.setOnClickListener {
                                dialog?.dismiss()
                            }

                             AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@PreviousAppointment, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                    myToast(this@PreviousAppointment, t.message.toString())
                     AppProgressBar.hideLoaderDialog()

                }

            })
    }

}