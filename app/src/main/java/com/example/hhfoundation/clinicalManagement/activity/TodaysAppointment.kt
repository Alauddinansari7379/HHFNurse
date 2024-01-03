package com.example.hhfoundation.clinicalManagement.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.adapter.AdapterAppointmentList
import com.example.hhfoundation.clinicalManagement.model.ModelAppointList
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.clinicalManagement.model.PrescriptionList
import com.example.hhfoundation.databinding.ActivityAppointmentBinding
import com.example.hhfoundation.doctor.model.Doctor
import com.example.hhfoundation.doctor.model.ModelDoctorList
import com.example.hhfoundation.labReport.activity.ViewLabReport
import com.example.hhfoundation.labReport.model.ModelUpload
import com.example.hhfoundation.registration.model.ModelSpinner
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodaysAppointment : AppCompatActivity(), AdapterAppointmentList.Information {
    private lateinit var binding: ActivityAppointmentBinding
    lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
    private lateinit var mainData: ArrayList<PrescriptionList>
    var status = ""
    var date = ""
    var doctorId = ""
    private var context: Context = this@TodaysAppointment
    var statusChange = ""
    private val mydilaog: Dialog? = null
    var doctorList = ArrayList<Doctor>()
    var statuesList = ArrayList<ModelSpinner>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@TodaysAppointment)
        mainData = ArrayList<PrescriptionList>()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        statuesList.add(ModelSpinner("Pending Confirmation", "1"))
        if (sessionManager.group != "Doctor") {
            statuesList.add(ModelSpinner("Confirmed", "2"))
        }
        if (sessionManager.group != "Receptionist") {
            statuesList.add(ModelSpinner("Treated", "3"))
        }
        Log.e("gruop", sessionManager.group.toString())

        if (sessionManager.group != "Doctor" && sessionManager.group != "Pharmacist") {
            apiCallDoctorList()
        }
        if (sessionManager.group == "Doctor") {
            binding.layWomen.visibility = View.GONE
        }

        apiCallAppointmentList("")

        try {
            binding.edtSearch.addTextChangedListener { str ->
                setRecyclerViewAdapter(mainData.filter {
                    it.patientname != null && it.patientname!!.contains(
                        str.toString(),
                        ignoreCase = true
                    )
                } as ArrayList<PrescriptionList>)
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }


        binding.layMen.setOnClickListener {
            binding.edtSearch.text.clear()
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
            binding.edtSearch.text.clear()
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
            binding.edtSearch.text.clear()
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
            binding.edtSearch.text.clear()
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
        binding.edtSearch.text.clear()
        AppProgressBar.showLoaderDialog(this@TodaysAppointment)


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
                            myToast(this@TodaysAppointment, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.appoitmentdetails.isEmpty()) {
                            myToast(this@TodaysAppointment, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@TodaysAppointment, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelAppointList>, t: Throwable) {
                    myToast(this@TodaysAppointment, t.message.toString())
                    // apiCallAppointmentList(status)
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun apiCallChangeStatus(appoinId: String) {

        AppProgressBar.showLoaderDialog(this@TodaysAppointment)


        ApiClient.apiService.updatestausdoctor(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            appoinId,
            statusChange,
            doctorId,
            date
        )
            .enqueue(object : Callback<ModelUpload> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelUpload>, response: Response<ModelUpload>
                ) {
                    try {

                        if (response.code() == 500) {
                            myToast(this@TodaysAppointment, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 400) {
                            myToast(this@TodaysAppointment, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@TodaysAppointment, response.body()!!.message)
                            if (sessionManager.group == "Receptionist") {
                                apiCallAppointmentList("Pending Confirmation")

                            } else {
                                apiCallAppointmentList("Confirmed")
                            }

                        }
                    } catch (e: Exception) {
                        myToast(this@TodaysAppointment, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
                    myToast(this@TodaysAppointment, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<PrescriptionList>) {
        binding.recyclerView.apply {
            adapter = AdapterAppointmentList(this@TodaysAppointment, data, this@TodaysAppointment)
            AppProgressBar.hideLoaderDialog()

        }
    }


    override fun info(id: String) {
        // popupUpload(id)
        apiCallAppointmentInfo(id)

    }

    override fun statusChange(id: String) {
        apiCallChangeStatusDilog(id)
    }

    private fun apiCallAppointmentInfo(id: String) {

        AppProgressBar.showLoaderDialog(this@TodaysAppointment)


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
                            myToast(this@TodaysAppointment, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 405) {
                            myToast(this@TodaysAppointment, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view =
                                layoutInflater.inflate(R.layout.dialog_appointment_info, null)
                            dialog = Dialog(this@TodaysAppointment)
                            var appointmentTypeDil =
                                view!!.findViewById<TextView>(R.id.AppointmentTypeDil)
                            val studentDil = view!!.findViewById<TextView>(R.id.StudentDil)
                            //  val departmentDil = view!!.findViewById<TextView>(R.id.DepartmentDil)
                            val dateApp = view!!.findViewById<TextView>(R.id.dateApp)
                            val appointmentStatus =
                                view!!.findViewById<TextView>(R.id.AppointmentStatus)
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

                            val img1 = view!!.findViewById<ImageView>(R.id.img1)
                            val img2 = view!!.findViewById<ImageView>(R.id.img2)
                            val img3 = view!!.findViewById<ImageView>(R.id.img3)
                            val img4 = view!!.findViewById<ImageView>(R.id.img4)
                            val img5 = view!!.findViewById<ImageView>(R.id.img5)

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
                            appointmentStatus.text = response.body()!!.status
                            dateApp.text = response.body()!!.date

                            val img1d = response.body()!!.img_url1
                            val img2d = response.body()!!.img_url2
                            val img3d = response.body()!!.img_url3
                            val img4d = response.body()!!.img_url4
                            val img5d = response.body()!!.img_url5

                            if (response.body()!!.img_url1 != null) {
                                Glide
                                    .with(this@TodaysAppointment)
                                    .load("https://schoolhms.thedemostore.in/" + response.body()!!.img_url1)
                                    .into(img1)
                            }
                            if (response.body()!!.img_url2 != null) {
                                Glide
                                    .with(this@TodaysAppointment)
                                    .load("https://schoolhms.thedemostore.in/" + response.body()!!.img_url2)
                                    .into(img2)
                            }
                            if (response.body()!!.img_url3 != null) {
                                Glide
                                    .with(this@TodaysAppointment)
                                    .load("https://schoolhms.thedemostore.in/" + response.body()!!.img_url3)
                                    .into(img3)
                            }
                            if (response.body()!!.img_url4 != null) {
                                Glide
                                    .with(this@TodaysAppointment)
                                    .load("https://schoolhms.thedemostore.in/" + response.body()!!.img_url4)
                                    .into(img4)
                            }
                            if (response.body()!!.img_url5 != null) {
                                Glide
                                    .with(this@TodaysAppointment)
                                    .load("https://schoolhms.thedemostore.in/" + response.body()!!.img_url5)
                                    .into(img5)
                            }



                            dialog = Dialog(this@TodaysAppointment)
                            if (view.parent != null) {
                                (view.parent as ViewGroup).removeView(view) // <- fix
                            }
                            dialog!!.setContentView(view)
                            dialog?.setCancelable(true)

                            dialog?.show()

                            imgClose.setOnClickListener {
                                dialog?.dismiss()
                            }
                            img1.setOnClickListener {
                                if (img1d.toString().isNotEmpty()) {
                                    val intent =
                                        Intent(context as Activity, ViewLabReport::class.java)
                                            .putExtra("image", img1d.toString())
                                    context.startActivity(intent)
                                }
                            }

                            img2.setOnClickListener {
                                if (img2d.toString().isNotEmpty()) {
                                    val intent =
                                        Intent(context as Activity, ViewLabReport::class.java)
                                            .putExtra("image", img2d.toString())
                                    context.startActivity(intent)
                                }
                            }

                            img3.setOnClickListener {
                                if (img3d.toString().isNotEmpty()) {
                                    val intent = Intent(
                                        context as Activity,
                                        ViewLabReport::class.java
                                    )
                                        .putExtra("image", img3d.toString())
                                    context.startActivity(intent)
                                }
                            }

                            img4.setOnClickListener {
                                if (img4d.toString().isNotEmpty()) {
                                    val intent = Intent(
                                        context as Activity,
                                        ViewLabReport::class.java
                                    )
                                        .putExtra("image", img4d.toString())
                                    context.startActivity(intent)
                                }
                            }
                            img5.setOnClickListener {
                                if (img5d.toString().isNotEmpty()) {
                                    val intent = Intent(
                                        context as Activity,
                                        ViewLabReport::class.java
                                    )
                                        .putExtra("image", img5d.toString())
                                    context.startActivity(intent)
                                }
                            }
                        }

                        AppProgressBar.hideLoaderDialog()
                    } catch (e: Exception) {
                        myToast(this@TodaysAppointment, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                    myToast(this@TodaysAppointment, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }


    private fun apiCallDoctorList() {

        AppProgressBar.showLoaderDialog(this@TodaysAppointment)
        ApiClient.apiService.doctorlist(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
        )
            .enqueue(object : Callback<ModelDoctorList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDoctorList>,
                    response: Response<ModelDoctorList>
                ) {

                    try {
                        doctorList = response.body()!!.doctors
                        if (doctorList != null) {
                            val spinnerDoctor =
                                findViewById<Spinner>(R.id.spinnerDoctorSC)

                            //spinner code start
                            val items = arrayOfNulls<String>(doctorList!!.size)

                            for (i in doctorList!!.indices) {

                                items[i] = doctorList!![i].name
                            }
/*
                            spinnerDoctor.adapter = ArrayAdapter<String>(
                                this@ConsaltationRequest,
                                android.R.layout.simple_list_item_1,
                                items
                            )


                            spinnerDoctor.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        p0: AdapterView<*>?,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {
                                        if (statuesList.size > 0) {
                                            statusChange = statuesList[i].text
                                        }
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                                    }

                                }*/

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


                override fun onFailure(
                    call: Call<ModelDoctorList>,
                    t: Throwable
                ) {
                    myToast(this@TodaysAppointment, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun apiCallChangeStatusDilog(id: String) {

        AppProgressBar.showLoaderDialog(this@TodaysAppointment)


        ApiClient.apiService.appoitmentinfo(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            id
        )
            .enqueue(object : Callback<ModelNewAppoint> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelNewAppoint>,
                    response: Response<ModelNewAppoint>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@TodaysAppointment, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@TodaysAppointment, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view = layoutInflater.inflate(
                                R.layout.dialog_appointment_change_staus,
                                null
                            )
                            dialog = Dialog(this@TodaysAppointment)
                            var appointmentTypeDil =
                                view!!.findViewById<TextView>(R.id.AppointmentTypeDilSC)
                            val studentDil =
                                view!!.findViewById<TextView>(R.id.StudentDilSC)
                            // val departmentDil = view!!.findViewById<TextView>(R.id.DepartmentDilSC)
                            val appointmentStatusC =
                                view!!.findViewById<TextView>(R.id.AppointmentStatusC)

                            val bloodPressureDil =
                                view!!.findViewById<TextView>(R.id.BloodPressureDilSC)
                            val pRDil =
                                view!!.findViewById<TextView>(R.id.PRDilSC)
                            val temperatureDilil =
                                view!!.findViewById<TextView>(R.id.TemperatureDilSC)
                            val sPO2Dil =
                                view!!.findViewById<TextView>(R.id.SPO2DilSC)
                            val randomBloodSugarDil =
                                view!!.findViewById<TextView>(R.id.RandomBloodSugarDilSC)
                            val presentComplainsDil =
                                view!!.findViewById<TextView>(R.id.PresentComplainsDilSC)
                            val sickDateDil =
                                view!!.findViewById<TextView>(R.id.SickDateDilSC)
                            val remarksDil =
                                view!!.findViewById<TextView>(R.id.RemarksDilSC)
                            val patientIdSC =
                                view!!.findViewById<TextView>(R.id.PatientIdSC)
                            // val doctorSC = view!!.findViewById<TextView>(R.id.DoctorSC)
                            val dateSC =
                                view!!.findViewById<TextView>(R.id.DateSC)
                            val spinnerDoctor =
                                view!!.findViewById<Spinner>(R.id.spinnerDoctorSC)
                            val doctorName =
                                view!!.findViewById<TextView>(R.id.DoctorName)

                            val appointmentStatusSC =
                                view!!.findViewById<Spinner>(R.id.AppointmentStatusSC)


                            if (sessionManager.group == "Doctor") {
                                doctorName.visibility = View.VISIBLE
                                spinnerDoctor.visibility = View.GONE
                                doctorName.text =
                                    sessionManager.ionId.toString()
                            }

                            mydilaog?.setCanceledOnTouchOutside(false)
                            mydilaog?.window?.setBackgroundDrawable(
                                ColorDrawable(Color.TRANSPARENT)
                            )
                            val newCalendar = Calendar.getInstance()
                            val datePicker = DatePickerDialog(
                                this@TodaysAppointment,
                                { _, year, monthOfYear, dayOfMonth ->
                                    val newDate = Calendar.getInstance()
                                    newDate[year, monthOfYear] = dayOfMonth
                                    DateFormat.getDateInstance()
                                        .format(newDate.time)
                                    // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                                    date =
                                        SimpleDateFormat(
                                            "dd/MM/yyyy",
                                            Locale.getDefault()
                                        ).format(
                                            newDate.time
                                        )
                                    dateSC.text =
                                        SimpleDateFormat(
                                            "dd/MM/yyyy",
                                            Locale.getDefault()
                                        ).format(
                                            newDate.time
                                        )


                                    Log.e(
                                        ContentValues.TAG,
                                        "expiredDate:>>$date"
                                    )
                                },
                                newCalendar[Calendar.YEAR],
                                newCalendar[Calendar.MONTH],
                                newCalendar[Calendar.DAY_OF_MONTH]
                            )
                            datePicker.datePicker.minDate =
                                System.currentTimeMillis() - 1000;

                            dateSC.setOnClickListener {
                                datePicker.show()
                            }
                            appointmentStatusSC.adapter =
                                ArrayAdapter<ModelSpinner>(
                                    this@TodaysAppointment,
                                    android.R.layout.simple_list_item_1,
                                    statuesList
                                )

                            appointmentStatusSC.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        p0: AdapterView<*>?,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {
                                        if (statuesList.size > 0) {
                                            statusChange = statuesList[i].text
                                        }
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                                    }

                                }

                            val items = arrayOfNulls<String>(doctorList!!.size)

                            //spinner code start

                            for (i in doctorList!!.indices) {

                                items[i] = doctorList!![i].name
                            }

                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@TodaysAppointment,
                                    android.R.layout.simple_list_item_1,
                                    items
                                )
                            spinnerDoctor.adapter = adapter


                            spinnerDoctor.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        p0: AdapterView<*>?,
                                        view: View?,
                                        i: Int,
                                        l: Long
                                    ) {
                                        if (doctorList.size > 0) {
                                            doctorId = doctorList[i].id
                                        }
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                                    }

                                }
                            val imgClose =
                                view!!.findViewById<ImageView>(R.id.imgCloseDilSC)
                            val btnInfoAppSC =
                                view!!.findViewById<Button>(R.id.btnInfoAppSC)

                            appointmentTypeDil.text = response.body()!!.appotype
                            studentDil.text = response.body()!!.patient
                            //  departmentDil.text=response.body()!!.d
                            bloodPressureDil.text = response.body()!!.bp
                            pRDil.text = response.body()!!.pr
                            temperatureDilil.text = response.body()!!.temp
                            sPO2Dil.text = response.body()!!.satur
                            randomBloodSugarDil.text = response.body()!!.ranbl
                            presentComplainsDil.text =
                                response.body()!!.complain
                            sickDateDil.text = response.body()!!.sdate
                            remarksDil.text = response.body()!!.remarks

                            patientIdSC.text = id
                            //  doctorSC.text=response.body()!!.doctor
                            dateSC.text = response.body()!!.date
                            appointmentStatusC.text = response.body()!!.status


                            dialog = Dialog(this@TodaysAppointment)
                            if (view.parent != null) {
                                (view.parent as ViewGroup).removeView(view) // <- fix
                            }
                            dialog!!.setContentView(view)
                            dialog?.setCancelable(true)

                            dialog?.show()

                            imgClose.setOnClickListener {
                                dialog?.dismiss()
                            }

                            btnInfoAppSC.setOnClickListener {
                                dialog?.dismiss()
                                apiCallChangeStatus(id)
                            }

                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@TodaysAppointment, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(
                    call: Call<ModelNewAppoint>,
                    t: Throwable
                ) {
                    myToast(this@TodaysAppointment, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }


}