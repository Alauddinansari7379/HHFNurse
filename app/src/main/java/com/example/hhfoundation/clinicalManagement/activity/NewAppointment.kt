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
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.hhfoundation.Helper.*
import com.example.hhfoundation.Helper.ImageUploadClass.UploadRequestBody
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.dasboard.Dashboard
import com.example.hhfoundation.databinding.ActivityNewAppointmentBinding
import com.example.hhfoundation.registration.model.ModelPatientList
import com.example.hhfoundation.registration.model.ModelSpinner
import com.example.hhfoundation.registration.model.Patient
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
    var gendar = ""
    var age = ""
    var email = ""
    var phone = ""
    var className = ""
    var choseFile = ""
    var medicalHistory = ""
    var calssName = ""
    private lateinit var sessionManager: SessionManager
    private var selectedImageUri: Uri? = null

    private var parts1: MultipartBody.Part? = null
    private var parts2: MultipartBody.Part? = null
    private var parts3: MultipartBody.Part? = null
    private var parts4: MultipartBody.Part? = null
    private var parts5: MultipartBody.Part? = null

    // var degreeList = ModelPatientList()
    var imageList = kotlin.collections.ArrayList<Uri>()


    private lateinit var binding: ActivityNewAppointmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sessionManager = SessionManager(this@NewAppointment)
        apiCallPatientSpinner()

        sickDate = currentDate


        with(binding) {

            choseFile1.setOnClickListener {
                choseFile = "1"
                openImageChooser()


//                val intent = Intent(this@NewAppointment, AlbumSelectActivity::class.java)
////set limit on number of images that can be selected, default is 10
////set limit on number of images that can be selected, default is 10
//                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5)
//                startActivityForResult(intent, Constants.REQUEST_CODE)
//                MultiImagePicker.with(this@NewAppointment) // `this` refers to activity or fragment
//                    .setSelectionLimit(10)  // The number of max image selection you want from user at a time, MAX is 30
//                    .open()
            }
            // This will open image selection activity to select images

            /*      choseFile1.setOnClickListener {
                      choseFile = "1"
                      openImageChooser()
                  }
                  choseFile2.setOnClickListener {
                      choseFile = "2"
                      openImageChooser()
                  }
                  choseFile3.setOnClickListener {
                      choseFile = "3"
                      openImageChooser()
                  }
                  choseFile4.setOnClickListener {
                      choseFile = "4"
                      openImageChooser()
                  }
                  choseFile5.setOnClickListener {
                      choseFile = "5"
                      openImageChooser()
                  }*/
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
                if (selectedImageUri == null) {
                    apiCallNewAppointmentNew()

                } else {
                    apiCallNewAppointment()

                }

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

        val parts: MutableList<MultipartBody.Part> = ArrayList()

        for ((index, imageUri) in imageList.withIndex()) {
            val file = File(imageUri.path)

            // Convert Uri to File
            val requestFile: RequestBody = create("image/*".toMediaTypeOrNull(), file)

            // Create MultipartBody.Part using file request body
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("img_url$index", file.name, requestFile)

            parts.add(body)
        }
        Log.e("part", parts.toString())
//        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)
//        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
//        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
//        val outputStream = FileOutputStream(file)
//        inputStream.copyTo(outputStream)
//        val body = UploadRequestBody(file, "image", this)

        //Pending Image Uploading
        ApiClient.apiService.addAppointment(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            appointmentType,
            patientId,
            sickDate,
            binding.edtRandomBloodS.text.toString().trim(),
            binding.edtSaturation.text.toString().trim(),
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
            email,
            phone,
            age,
            gendar,
            "",
            binding.edtPR.text.toString().trim(),
            binding.edtBloodPressure.text.toString().trim(),
            binding.edtTepm.text.toString().trim(),
            binding.edtPresentCom.text.toString().trim(),
            "",
            "",

//            MultipartBody.Part.createFormData("img_url2", file.name, body),
//            MultipartBody.Part.createFormData("img_url3", file.name, body),
//            MultipartBody.Part.createFormData("img_url4", file.name, body),
//            MultipartBody.Part.createFormData("img_url5", file.name, body),
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

                    } else if (response.body()!!.message.contentEquals("successful")) {
                        myToast(this@NewAppointment, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        startActivity(Intent(this@NewAppointment, Dashboard::class.java))
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

    private fun apiCallNewAppointmentNew() {
//        if (selectedImageUri == null) {
//            myToast(this@NewAppointment, "Select an Image First")
//            return
//        }
        AppProgressBar.showLoaderDialog(this)
        val parts: MutableList<MultipartBody.Part> = ArrayList()


        Log.e("imageList", imageList.toString())

        for ((index, imageUri) in imageList.withIndex()) {
            //val file = File(imageUri.path)
            val parcelFileDescriptor = contentResolver.openFileDescriptor(imageUri!!, "r", null)
            val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
            val file = File(cacheDir, contentResolver.getFileName(imageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            //val body = UploadRequestBody(file, "image", this)
            // Convert Uri to File
            val requestFile: RequestBody = create("image/*".toMediaTypeOrNull(), file)

            // Create MultipartBody.Part using file request body


            when (index) {
                0 -> {
                    parts1 = MultipartBody.Part.createFormData("img_url", file.name, requestFile)
                    // parts.add(body)
                    Log.e("part0", parts.toString())

                }
                1 -> {
                    parts2 = MultipartBody.Part.createFormData("img_url2", file.name, requestFile)

//                    val body: MultipartBody.Part =
//                        MultipartBody.Part.createFormData("img_url2", file.name, requestFile)
//                    parts.add(body)
//                    Log.e("part1", parts.toString())
                }
                2 -> {
                    parts3 = MultipartBody.Part.createFormData("img_url3", file.name, requestFile)
//                    val body: MultipartBody.Part =
//                        MultipartBody.Part.createFormData("img_url3", file.name, requestFile)
//                    parts.add(body)
//                    Log.e("part2", parts.toString())
                }
                3 -> {
                    parts4 = MultipartBody.Part.createFormData("img_url4", file.name, requestFile)
//                    val body: MultipartBody.Part =
//                        MultipartBody.Part.createFormData("img_url4", file.name, requestFile)
//                    parts.add(body)
//                    Log.e("part3", parts.toString())
                }
                4 -> {
                    parts5 = MultipartBody.Part.createFormData("img_url5", file.name, requestFile)
//                    val body: MultipartBody.Part =
//                        MultipartBody.Part.createFormData("img_url5", file.name, requestFile)
//                    parts.add(body)
//                    Log.e("part4", parts.toString())
                }
                else -> {

                }


            }


        }
        Log.e("partALL", parts.toString())

//        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)
//        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
//        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
//        val outputStream = FileOutputStream(file)
//        inputStream.copyTo(outputStream)
//        val body = UploadRequestBody(file, "image", this)

        //Pending Image Uploading


        ApiClient.apiService.addAppointmentNew(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            appointmentType,
            patientId,
            sickDate,
            binding.edtRandomBloodS.text.toString().trim(),
            binding.edtSaturation.text.toString().trim(),
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
            email,
            phone,
            age,
            gendar,
            "",
            binding.edtPR.text.toString().trim(),
            binding.edtBloodPressure.text.toString().trim(),
            binding.edtTepm.text.toString().trim(),
            binding.edtPresentCom.text.toString().trim(),
            "",
            "",
            parts1,
            parts2,
            parts3,
            parts4,
            parts5,

//            MultipartBody.Part.createFormData("img_url", file.name, body),
//            MultipartBody.Part.createFormData("img_url", file.name, body),
//            MultipartBody.Part.createFormData("img_url", file.name, body),
//            MultipartBody.Part.createFormData("img_url", file.name, body),

        ).enqueue(
            object : Callback<ModelNewAppoint> {
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

                        } else if (response.body()!!.message.contentEquals("successful")) {
                            myToast(this@NewAppointment, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()
                            startActivity(
                                Intent(
                                    this@NewAppointment,
                                    PreviousAppointment::class.java
                                )
                            )
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
                    //  apiCallNewAppointmentNew()
                    myToast(this@NewAppointment, t.message.toString())
                    AppProgressBar.hideLoaderDialog()

                }

            })

    }

    private fun openImageChooser() {

        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE);

//        Intent(Intent.ACTION_PICK).also {
//            it.type = "image/*"
//            (MediaStore.ACTION_IMAGE_CAPTURE)
//            val mimeTypes = arrayOf("image/jpeg", "image/png")
//            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, mimeTypes)
//            startActivityForResult(it, REQUEST_CODE_IMAGE)
//
//        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
//        pdfIntent.type = "application/pdf"
//        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
//        startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        //   }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    Log.e("data?.data", data?.data.toString())
                    // if multiple images are selected
                    if (data?.clipData != null) {
                        selectedImageUri = data?.data
                        var count = data.clipData?.itemCount

                        for (i in 0 until count!!) {
                            var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                            imageList.add(imageUri)
                            Log.e("Mul?.data", imageList.toString())
                            binding.layoutImages.visibility = View.VISIBLE

                        }
                        for ((index, value) in imageList.withIndex()) {
                             when (index) {
                                0 -> {
                                    binding.img1.setImageURI(value)
                                }
                                1 -> {
                                    binding.img2.setImageURI(value)
                                }
                                2 -> {
                                    binding.img3.setImageURI(value)
                                }
                                3 -> {
                                    binding.img4.setImageURI(value)
                                }
                                4 -> {
                                    binding.img5.setImageURI(value)
                                }
                            }
                        }

                        //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews

                    } else if (data?.data != null) {
                        selectedImageUri = data?.data
                        // if single image is selected
                        binding.layoutImages.visibility = View.VISIBLE
                        binding.img1.setImageURI(selectedImageUri)
                        var imageUri: Uri = data.data!!
                        Log.e("Sing?.data", imageUri.toString())

                        //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

                    }


                    when (choseFile) {
                        "1" -> {
                            binding!!.NoFileChosen1.setTextColor(Color.parseColor("#FF4CAF50"))
                            binding!!.NoFileChosen1.text = "Image Selected"
                            if (imageList.size > 5) myToast(
                                this@NewAppointment,
                                "You can select only 5 images"
                            )
                            binding.layoutImages.visibility = View.VISIBLE
                            for (i in imageList) {
                                binding.img1.setImageURI(i)

                            }

                        }
//                        "2" -> {
//                            binding!!.NoFileChosen2.setTextColor(Color.parseColor("#FF4CAF50"))
//                            binding!!.NoFileChosen2.text = "Image Selected"
//
//                        }
//                        "3" -> {
//                            binding!!.NoFileChosen3.setTextColor(Color.parseColor("#FF4CAF50"))
//                            binding!!.NoFileChosen3.text = "Image Selected"
//                        }
//                        "4" -> {
//                            binding!!.NoFileChosen4.setTextColor(Color.parseColor("#FF4CAF50"))
//                            binding!!.NoFileChosen4.text = "Image Selected"
//                        }
//                        "5" -> {
//                            binding!!.NoFileChosen5.setTextColor(Color.parseColor("#FF4CAF50"))
//                            binding!!.NoFileChosen5.text = "Image Selected"
//                        }
                    }

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
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),

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
                                        try {
                                            studentName = patientList!![i].name
                                            patientId = patientList!![i].id
                                            fatherName = patientList!![i].f_name
                                            binding.edtFatherName.setText(fatherName)
                                            admissionNumber = patientList!![i].admissionnumber
                                            binding.edtAdminssionNumber.setText(admissionNumber)
                                            className = patientList!![i].education
                                            gendar = patientList!![i].sex

                                            val dob = patientList!![i].birthdate

                                            if (dob != null) {
                                                try {
                                                    var fDOb = ""
                                                    fDOb =
                                                        if (dob.contains("-", ignoreCase = true)) {
                                                            changeDateFormat5(dob)
                                                        } else {
                                                            changeDateFormat6(dob)
                                                        }
                                                    //dd/MM/yyyy
                                                    Log.e("after", dob.toString())

                                                    age = DateUtils.getAgeFromDOB(fDOb)
                                                    Log.e("dob", dob.toString())
                                                    Log.e("Age", age)
                                                    binding.edtAge.setText(age)

//                if (age.toInt() < 6) {
//                    // binding.btnNext.text = "Submit"
//                }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }
                                            }
                                            phone = patientList!![i].phone
                                            email = patientList!![i].email
                                            binding.edtClass.setText(className)

                                            Log.e("PAtientID", patientId)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }

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
