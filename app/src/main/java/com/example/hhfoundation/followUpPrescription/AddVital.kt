package com.example.hhfoundation.followUpPrescription

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.convertTo12Hour
import com.example.hhfoundation.Helper.currentDate
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.databinding.ActivityAddVitalBinding
import com.example.hhfoundation.followUpPrescription.model.ModelFollowUp
import com.example.hhfoundation.registration.model.ModelRegister
import com.example.hhfoundation.registration.model.ModelSpinner
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddVital : AppCompatActivity() {
    private lateinit var binding: ActivityAddVitalBinding
    var reasonList = ArrayList<ModelSpinner>()
    lateinit var sessionManager: SessionManager
    var reason = ""
    private var bloodPressure = ""
    var prValue = ""
    var prd = "No"
    var follow = ""
    var followReason = ""
    private var temp = ""
    private var saturation = ""
    private var patientid = ""
    private var date = ""
    var studentName = ""
    var time = ""
    private var pid = ""
    var presc = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVitalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@AddVital)

        pid = intent.getStringExtra("pid").toString()
        patientid = intent.getStringExtra("patientid").toString()
        studentName = intent.getStringExtra("patientname").toString()
        date = intent.getStringExtra("date").toString()
        time = intent.getStringExtra("created_at").toString()
        presc = intent.getStringExtra("presc").toString()


        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }
            tvStudent.text = studentName
            tvDate.text = time.subSequence(0,10)
            tvTime.text = convertTo12Hour( time.substring(10))


            edtPR.setOnFocusChangeListener { view, b ->
                bloodPressure = edtBloodPressure.text.toString().trim()
                if (edtBloodPressure.text!!.contains("mmhh")) {

                } else {
                    edtBloodPressure.setText("$bloodPressure mmhh")

                }

            }
            edtTepm.setOnFocusChangeListener { view, b ->
                prValue = edtPR.text.toString().trim()
                if (edtPR.text!!.contains("BMP")) {

                } else {
                    edtPR.setText("$prValue BMP")

                }

            }

            edtSaturation.setOnFocusChangeListener { view, b ->
                temp = edtTepm.text.toString().trim()
                if (edtTepm.text!!.contains("°F")) {

                } else {
                    edtTepm.setText("$temp °F")

                }

            }
            edtRandomBloodS.setOnFocusChangeListener { view, b ->
                saturation = edtSaturation.text.toString().trim()
                if (edtSaturation.text!!.contains("%")) {

                } else {
                    edtSaturation.setText("$saturation %")

                }

            }

            reasonList.add(ModelSpinner("Not Willing", "1"))
            reasonList.add(ModelSpinner("Not Responding", "2"))
            reasonList.add(ModelSpinner("Refused", "3"))
            reasonList.add(ModelSpinner("Following treatment elsewhere", "4"))

            spinnerReason.adapter = ArrayAdapter<ModelSpinner>(
                this@AddVital, android.R.layout.simple_list_item_1, reasonList
            )

            spinnerReason.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, i: Int, l: Long) {
                        if (reasonList.size > 0) {
                            reason = reasonList[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }

                }

            radioStable.setOnCheckedChangeListener { _, _ ->
                if (radioStable.isChecked) {
                    followReason="Stable"
                    layoutPatientR.visibility = View.GONE
                 }

            }

            radioCured.setOnCheckedChangeListener { _, _ ->
                if (radioCured.isChecked) {
                    followReason="Cured"
                    layoutPatientR.visibility = View.GONE
                 }

            }
           // radioUnstable.isChecked = true

            radioUnstable.setOnCheckedChangeListener { _, _ ->
                if (radioUnstable.isChecked) {
                    followReason="Unstable"
                    layoutPatientR.visibility = View.VISIBLE
                }
                if (radioYesPR.isChecked) {
                    prd = "Yes"
                }
                if (radioNoPR.isChecked) {
                    prd = "No"
                }

            }

            radioYesPR.setOnCheckedChangeListener { _, _ ->
                if (radioYesPR.isChecked) {
                    prd = "Yes"
                }
                if (radioNoPR.isChecked) {
                    prd = "No"
                }

            }

            radioNoPR.setOnCheckedChangeListener { _, _ ->
                if (radioNoPR.isChecked) {
                    prd = "No"
                }

            }


//            radioNotFollowing.isChecked = true
//            radioYesPR.isChecked = true

            radioFollowing.setOnCheckedChangeListener { _, _ ->
                if (radioFollowing.isChecked) {
                    follow=""
                    layoutTrea.visibility = View.VISIBLE
                    layoutNotTreated.visibility = View.GONE
                    layoutNotTreated.visibility = View.GONE

                }

            }


            radioNotFollowing.setOnCheckedChangeListener { _, _ ->
                if (radioNotFollowing.isChecked) {
                    follow= "Not Following the Treatment"
                    layoutNotTreated.visibility = View.VISIBLE
                    layoutTrea.visibility = View.GONE
                    layoutNotTreated.visibility = View.VISIBLE

                }
            }

            btnSubmit.setOnClickListener {

                if (radioFollowing.isChecked) {
                    reason = ""
                    if (edtBloodPressure.text.toString().isEmpty()) {
                        edtBloodPressure.error = "Enter Blood Pressure"
                        edtBloodPressure.requestFocus()
                        return@setOnClickListener
                    }
                    if (edtPR.text.toString().isEmpty()) {
                        edtPR.error = "Enter PR"
                        edtPR.requestFocus()
                        return@setOnClickListener
                    }
                    if (edtTepm.text.toString().isEmpty()) {
                        edtTepm.error = "Enter Temperature"
                        edtTepm.requestFocus()
                        return@setOnClickListener
                    }
                    if (edtSaturation.text.toString().isEmpty()) {
                        edtSaturation.error = "Enter Saturation"
                        edtSaturation.requestFocus()
                        return@setOnClickListener
                    }

                    if (edtRandomBloodS.text.toString().isEmpty()) {
                        edtRandomBloodS.error = "Enter Random Blood Sugar"
                        edtRandomBloodS.requestFocus()
                        return@setOnClickListener
                    }
                    apiCallAddVital()

                } else {

                    if (noteReason.text.toString().isEmpty()) {
                        noteReason.error = "Enter Note"
                        noteReason.requestFocus()
                        return@setOnClickListener
                    }
                    apiCallAddVital()
                }
            }
        }
    }

    private fun apiCallAddVital() {

        AppProgressBar.showLoaderDialog(this@AddVital)

        ApiClient.apiService.addNewvita(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            patientid,
            currentDate,
            bloodPressure,
            prValue,
            temp,
            saturation,
            binding.edtRandomBloodS.text.toString().trim(),
            "",
            "",
            reason,
            binding.noteReason.text.toString().trim(),
            follow,
            followReason,
            prd,
            "",
                    pid,
            "",
        )
            .enqueue(object : Callback<ModelRegister> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelRegister>, response: Response<ModelRegister>
                ) {
                    try {

                        if (response.code() == 500) {
                            myToast(this@AddVital, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@AddVital, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@AddVital, response.body()!!.message)
                            startActivity(Intent(this@AddVital,FollowUpPrescription::class.java))
                             AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@AddVital, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelRegister>, t: Throwable) {
                    myToast(this@AddVital, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

}