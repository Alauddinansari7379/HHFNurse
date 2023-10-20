package com.example.hhfoundation.clinicalManagement.activity

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.ImageUploadClass.UploadRequestBody
import com.example.hhfoundation.Helper.currentDate
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.databinding.ActivityNewAppointmentBinding
import com.example.hhfoundation.registration.model.ModelPatientList
import com.example.hhfoundation.registration.model.ModelSpinner
import com.example.hhfoundation.registration.model.Patient
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewAppointment : AppCompatActivity(), UploadRequestBody.UploadCallback {
    var appointmentTypeList = ArrayList<ModelSpinner>()
    var departmentList = ArrayList<ModelSpinner>()
    var patientList = ArrayList<Patient>()
    var appointmentType = ""
    var department = ""
    private var bloodPressure = ""
    var prValue = ""
    private var temp = ""
    private val mydilaog: Dialog? = null
    private var saturation = ""
    private var normal = ""
    private var randomBS = ""
    var sickDate = ""
    var patientId = ""
    var studentName = ""
    var fatherName = ""
    var admissionNumber = ""
    var medicalHistory = ""
    var calssName = ""
    private lateinit var sessionManager: SessionManager
    private var selectedImageUri: Uri? = null
    // var degreeList = ModelPatientList()


    private lateinit var binding: ActivityNewAppointmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@NewAppointment)
        apiCallPatientSpinner()


        with(binding) {
            binding.btnSubmit.setOnClickListener {
                if (radioYesMH.isChecked) {
                    medicalHistory = "Yes"
                }
                if (radioNoMH.isChecked) {
                    medicalHistory = "No"

                }

                if (edtClass.text.toString().isEmpty()) {
                    edtClass.error = "Enter Class"
                    edtClass.requestFocus()
                    return@setOnClickListener
                }
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
                apiCallNewAppointment()

            }
            binding.imgBack.setOnClickListener {
                onBackPressed()
            }

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

            edtPresentCom.setOnFocusChangeListener { view, b ->
                randomBS = edtRandomBloodS.text.toString().trim()
                if (edtRandomBloodS.text!!.contains("mg/dl")) {

                } else {
                    edtRandomBloodS.setText("$randomBS mg/dl")

                }

            }



            radioNoPHC.isChecked = true
            radioNoMH.isChecked = true

            radioYesPHC.setOnCheckedChangeListener { _, _ ->
                if (radioYesPHC.isChecked) {
                    imageView.visibility = View.VISIBLE
                }

            }
            radioNoPHC.setOnCheckedChangeListener { _, _ ->

                if (radioNoPHC.isChecked) {
                    imageView.visibility = View.GONE

                }
            }


            mydilaog?.setCanceledOnTouchOutside(false)
            mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val newCalendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this@NewAppointment,
                { _, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    DateFormat.getDateInstance().format(newDate.time)
                    // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                    sickDate =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                    binding.tvSickDate.text =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                    val selectedDate =
                        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)

                    Log.e(ContentValues.TAG, "sickDate:>>$sickDate")
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            //datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;

            binding.tvSickDate.setOnClickListener {
                datePicker.show()
            }
            appointmentTypeList.add(ModelSpinner("General", "1"))
            appointmentTypeList.add(ModelSpinner("Emergency", "2"))


            departmentList.add(ModelSpinner("General Medicine", "1"))
            departmentList.add(ModelSpinner("Gynae & Obstetrics", "2"))
            departmentList.add(ModelSpinner("Ophthalmology", "2"))
            departmentList.add(ModelSpinner("Dental", "3"))
            departmentList.add(ModelSpinner("Mental", "4"))

            binding.spinnerAppointment.adapter = ArrayAdapter<ModelSpinner>(
                this@NewAppointment,
                R.layout.simple_list_item_1,
                appointmentTypeList
            )

            spinnerDepartment.adapter = ArrayAdapter<ModelSpinner>(
                this@NewAppointment, R.layout.simple_list_item_1, departmentList
            )

            binding.spinnerAppointment.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, i: Int, l: Long) {
                        if (appointmentTypeList.size > 0) {
                            appointmentType = appointmentTypeList[i].text
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }

                }
        }
        binding.spinnerDepartment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, i: Int, l: Long) {
                    if (departmentList.size > 0) {
                        department = departmentList[i].text
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }

            }
    }

    private fun apiCallNewAppointment() {
//        if (selectedImageUri == null) {
//            myToast(this@NewAppointment, "Select an Image First")
//            return
//        }
        AppProgressBar.showLoaderDialog(this)
//        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)
//
//        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
//
//        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
//        val outputStream = FileOutputStream(file)
//        inputStream.copyTo(outputStream)
//        val body = UploadRequestBody(file, "image", this)

        //Pending Image Uploading
        ApiClient.apiService.addAppointment(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            appointmentType,
            studentName,
            sickDate,
            binding.edtRandomBloodS.text.toString().trim(),
            "",
            "",
            medicalHistory,
            "",
            "Not Selected",
            "Pending Confirmation",
            "",
            "",
            "",
            "",
            binding.edtRemark.text.toString().trim(),
            currentDate,
            studentName,
            "",
            "",
            "",
            "",
            "",
            binding.edtPR.text.toString().trim(),
            binding.edtBloodPressure.text.toString().trim(),
            binding.edtTepm.text.toString().trim(),
            binding.edtPresentCom.text.toString().trim(),
            "",
            "",
//            MultipartBody.Part.createFormData("img_url", file.name, body),
//            MultipartBody.Part.createFormData("img_url", file.name, body),
//            MultipartBody.Part.createFormData("img_url", file.name, body),
//            MultipartBody.Part.createFormData("img_url", file.name, body),
        ).enqueue(object : Callback<ModelNewAppoint> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelNewAppoint>, response: Response<ModelNewAppoint>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@NewAppointment, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@NewAppointment, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.body()!!.message == "successful") {
                        myToast(this@NewAppointment, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        refresh()
                    } else {
                        myToast(this@NewAppointment, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@NewAppointment, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                apiCallNewAppointment()
                //  myToast(this@ProfileActivity, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)
//
//        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
//        pdfIntent.type = "application/pdf"
//        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
//        startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    Log.e("data?.data", data?.data.toString())
//                    binding!!.tvNoImage.text = "Image Selected"
//                    binding!!.tvNoImage.setTextColor(Color.parseColor("#FF4CAF50"));
                    //  binding.imageViewNew.visibility = View.VISIBLE
                    //   imageView?.setImageURI(selectedImageUri)
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()

        }

        return name
    }

    override fun onProgressUpdate(percentage: Int) {
    }

    private fun apiCallPatientSpinner() {


        //  progressDialog!!.show()

        ApiClient.apiService.patientList(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString()
        )
            .enqueue(object : Callback<ModelPatientList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPatientList>, response: Response<ModelPatientList>
                ) {

                    try {
                        patientList = response.body()!!.patients
                        if (patientList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(patientList!!.size)

                            for (i in patientList!!.indices) {

                                items[i] = patientList!![i].name
                            }
                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@NewAppointment,
                                    R.layout.simple_list_item_1,
                                    items
                                )
                            binding.spinnerStudent.adapter = adapter
                            //  binding.spinnerDegree.setSelection(items.indexOf(sessionManager.qualification.toString()));
                            //   binding.spinnerDegree.setSelection(sessionManager.qualification.toString().toInt())


                            binding.spinnerStudent.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        studentName = patientList!![i].name
                                        patientId = patientList!![i].patient_id
                                        fatherName = patientList!![i].f_name
                                        binding.edtFatherName.setText(fatherName)
                                        admissionNumber = patientList!![i].admissionnumber
                                        binding.edtAdminssionNumber.setText(admissionNumber)


                                        Log.e("PAtientID",patientId)

                                        // calssName = patientList!![i].c
                                        // Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelPatientList>, t: Throwable) {
                    myToast(this@NewAppointment, "Something went wrong")


                }

            })
    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}
