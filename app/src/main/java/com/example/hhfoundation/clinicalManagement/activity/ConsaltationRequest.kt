package com.example.hhfoundation.clinicalManagement.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
 import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.adapter.AdapterAppointmentList
import com.example.hhfoundation.clinicalManagement.model.ModelAppointList
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.clinicalManagement.model.PrescriptionList
import com.example.hhfoundation.databinding.ActivityConsaltationRequestBinding
import com.example.hhfoundation.doctor.model.Doctor
import com.example.hhfoundation.doctor.model.ModelDoctorList
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

class ConsaltationRequest : AppCompatActivity(),AdapterAppointmentList.Information {
    private lateinit var binding:ActivityConsaltationRequestBinding
    lateinit var sessionManager:SessionManager
     var dialog: Dialog? = null
    private lateinit var mainData: ArrayList<PrescriptionList>
    var status = ""
    var doctorId = ""
    var date = ""
    private val mydilaog: Dialog? = null
    var statusChange = ""
    var doctorList = ArrayList<Doctor>()
    var statuesList = ArrayList<ModelSpinner>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityConsaltationRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager= SessionManager(this@ConsaltationRequest)
        apiCallAppointmentList("Pending Confirmation")

        statuesList.add(ModelSpinner("Confirmed", "1"))
        statuesList.add(ModelSpinner("Pending Confirmation", "2"))
        if (sessionManager.group!="Receptionist") {
            statuesList.add(ModelSpinner("Treated", "3"))
        }
        apiCallDoctorList()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }
    private fun apiCallAppointmentList(status: String) {
        binding.edtSearch.text.clear()
        AppProgressBar.showLoaderDialog(this@ConsaltationRequest)


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
                            myToast(this@ConsaltationRequest, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.appoitmentdetails.isEmpty()) {
                            myToast(this@ConsaltationRequest, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@ConsaltationRequest, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelAppointList>, t: Throwable) {
                    myToast(this@ConsaltationRequest, t.message.toString())
                    // apiCallAppointmentList(status)
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }
    private fun apiCallChangeStatus(appoinId: String) {

        AppProgressBar.showLoaderDialog(this@ConsaltationRequest)
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
                            myToast(this@ConsaltationRequest, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 400) {
                            myToast(this@ConsaltationRequest, "No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@ConsaltationRequest, response.body()!!.message)
                            if (sessionManager.group=="Receptionist"){
                                apiCallAppointmentList("Pending Confirmation")
                            }else{
                                apiCallAppointmentList("Confirmed")
                            }

                        }
                    } catch (e: Exception) {
                        myToast(this@ConsaltationRequest, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
                    myToast(this@ConsaltationRequest, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<PrescriptionList>) {
        binding.recyclerView.apply {
            adapter = AdapterAppointmentList(this@ConsaltationRequest, data,this@ConsaltationRequest)
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

        AppProgressBar.showLoaderDialog(this@ConsaltationRequest)


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
                            myToast(this@ConsaltationRequest, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@ConsaltationRequest, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view = layoutInflater.inflate(R.layout.dialog_appointment_info, null)
                            dialog = Dialog(this@ConsaltationRequest)
                            var appointmentTypeDil = view!!.findViewById<TextView>(R.id.AppointmentTypeDil)
                            val studentDil = view!!.findViewById<TextView>(R.id.StudentDil)
                            val dateApp = view!!.findViewById<TextView>(R.id.dateApp)
                            val appointmentStatus = view!!.findViewById<TextView>(R.id.AppointmentStatus)
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
                            appointmentStatus.text=response.body()!!.status
                            dateApp.text=response.body()!!.date


                            dialog = Dialog(this@ConsaltationRequest)
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
                        myToast(this@ConsaltationRequest, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                    myToast(this@ConsaltationRequest, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun apiCallDoctorList() {

        AppProgressBar.showLoaderDialog(this@ConsaltationRequest)
        ApiClient.apiService.doctorlist(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
        )
            .enqueue(object : Callback<ModelDoctorList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDoctorList>, response: Response<ModelDoctorList>
                ) {

                    try {
                        doctorList = response.body()!!.doctors
                        if (doctorList != null) {
                            val spinnerDoctor = findViewById<Spinner>(R.id.spinnerDoctorSC)

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


                override fun onFailure(call: Call<ModelDoctorList>, t: Throwable) {
                    myToast(this@ConsaltationRequest, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }


    private fun apiCallChangeStatusDilog(id: String) {

        AppProgressBar.showLoaderDialog(this@ConsaltationRequest)


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
                            myToast(this@ConsaltationRequest, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 404) {
                            myToast(this@ConsaltationRequest, "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            val view = layoutInflater.inflate(
                                R.layout.dialog_appointment_change_staus,
                                null
                            )
                            dialog = Dialog(this@ConsaltationRequest)
                            var appointmentTypeDil =
                                view!!.findViewById<TextView>(R.id.AppointmentTypeDilSC)
                            val studentDil = view!!.findViewById<TextView>(R.id.StudentDilSC)
                          //  val departmentDil = view!!.findViewById<TextView>(R.id.DepartmentDilSC)
                            val appointmentStatusC = view!!.findViewById<TextView>(R.id.AppointmentStatusC)
                            val bloodPressureDil =
                                view!!.findViewById<TextView>(R.id.BloodPressureDilSC)
                            val pRDil = view!!.findViewById<TextView>(R.id.PRDilSC)
                            val temperatureDilil =
                                view!!.findViewById<TextView>(R.id.TemperatureDilSC)
                            val sPO2Dil = view!!.findViewById<TextView>(R.id.SPO2DilSC)
                            val randomBloodSugarDil =
                                view!!.findViewById<TextView>(R.id.RandomBloodSugarDilSC)
                            val presentComplainsDil =
                                view!!.findViewById<TextView>(R.id.PresentComplainsDilSC)
                            val sickDateDil = view!!.findViewById<TextView>(R.id.SickDateDilSC)
                            val remarksDil = view!!.findViewById<TextView>(R.id.RemarksDilSC)
                            val patientIdSC = view!!.findViewById<TextView>(R.id.PatientIdSC)
                           // val doctorSC = view!!.findViewById<TextView>(R.id.DoctorSC)
                            val dateSC = view!!.findViewById<TextView>(R.id.DateSC)
                            val spinnerDoctor = view!!.findViewById<Spinner>(R.id.spinnerDoctorSC)

                            val appointmentStatusSC =
                                view!!.findViewById<Spinner>(R.id.AppointmentStatusSC)

                            appointmentStatusSC.adapter = ArrayAdapter<ModelSpinner>(
                                this@ConsaltationRequest,
                                android.R.layout.simple_list_item_1,
                                statuesList
                            )


            mydilaog?.setCanceledOnTouchOutside(false)
            mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val newCalendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this@ConsaltationRequest,
                { _, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    DateFormat.getDateInstance().format(newDate.time)
                    // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                    date =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                    dateSC.text =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)


                    Log.e(ContentValues.TAG, "expiredDate:>>$date")
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;

            dateSC.setOnClickListener {
                datePicker.show()
            }
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
                                    this@ConsaltationRequest,
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

                            val imgClose = view!!.findViewById<ImageView>(R.id.imgCloseDilSC)
                            val btnInfoAppSC = view!!.findViewById<Button>(R.id.btnInfoAppSC)

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

                            patientIdSC.text=id
                          //  doctorSC.text=response.body()!!.doctor
                            dateSC.text=response.body()!!.date
                            appointmentStatusC.text=response.body()!!.status


                            dialog = Dialog(this@ConsaltationRequest)
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
                        myToast(this@ConsaltationRequest, "Something went wrong")
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                    myToast(this@ConsaltationRequest, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }
}