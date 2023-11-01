package com.example.hhfoundation.followUpPrescription

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.databinding.ActivityReferralsBinding
import com.example.hhfoundation.followUpPrescription.adapter.AdapterReferrals
import com.example.hhfoundation.followUpPrescription.model.ModelRefrreals
import com.example.hhfoundation.labReport.model.Prescriptiondetail
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Referrals : AppCompatActivity(), AdapterReferrals.Information {
    private lateinit var binding: ActivityReferralsBinding
    private lateinit var mainData: ArrayList<Prescriptiondetail>
    lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
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
                    it.doctrname!!.contains(str.toString(), ignoreCase = true)
                } as ArrayList<Prescriptiondetail>)
            }
        }
    }

    private fun apiCallReferralsList() {

        AppProgressBar.showLoaderDialog(this@Referrals)

        ApiClient.apiService.getReferrals(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
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
            adapter = AdapterReferrals(this@Referrals, data, this@Referrals)
            AppProgressBar.hideLoaderDialog()

        }
    }

    override fun info(id: String) {
        // popupUpload(id)
        apiCallAppointmentInfo(id)

    }

    private fun apiCallAppointmentInfo(id: String) {

        AppProgressBar.showLoaderDialog(this@Referrals)


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
                            myToast(this@Referrals, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.nurse_id.isEmpty()) {
                            myToast(this@Referrals, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view =
                                layoutInflater.inflate(R.layout.dialog_appointment_info, null)
                            dialog = Dialog(this@Referrals)
                            var appointmentTypeDil =
                                view!!.findViewById<TextView>(R.id.AppointmentTypeDil)
                            val studentDil = view!!.findViewById<TextView>(R.id.StudentDil)
                            val departmentDil = view!!.findViewById<TextView>(R.id.DepartmentDil)
                            val bloodPressureDil =
                                view!!.findViewById<TextView>(R.id.BloodPressureDil)
                            val pRDil = view!!.findViewById<TextView>(R.id.PRDil)
                            val temperatureDilil =
                                view!!.findViewById<TextView>(R.id.TemperatureDil)
                            val sPO2Dil = view!!.findViewById<TextView>(R.id.SPO2Dil)
                            val randomBloodSugarDil =
                                view!!.findViewById<TextView>(R.id.RandomBloodSugarDil)
                            val presentComplainsDil =
                                view!!.findViewById<TextView>(R.id.PresentComplainsDil)
                            val sickDateDil = view!!.findViewById<TextView>(R.id.SickDateDil)
                            val remarksDil = view!!.findViewById<TextView>(R.id.RemarksDil)
                            val imgClose = view!!.findViewById<ImageView>(R.id.imgCloseDil)

                            appointmentTypeDil.text = response.body()!!.appotype
                            studentDil.text = response.body()!!.patient
                            //  departmentDil.text=response.body()!!.d
                            bloodPressureDil.text = response.body()!!.bp
                            pRDil.text = response.body()!!.pr
                            temperatureDilil.text = response.body()!!.temp
                            sPO2Dil.text = response.body()!!.satur
                            randomBloodSugarDil.text = response.body()!!.ranbl
                            presentComplainsDil.text = response.body()!!.complain
                            sickDateDil.text = response.body()!!.sdate
                            remarksDil.text = response.body()!!.remarks


                            dialog = Dialog(this@Referrals)
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
                        myToast(this@Referrals, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                    myToast(this@Referrals, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }
}