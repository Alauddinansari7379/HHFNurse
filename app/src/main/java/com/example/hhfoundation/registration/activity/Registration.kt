package com.example.hhfoundation.registration.activity

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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.ImageUploadClass.UploadRequestBody
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.databinding.ActivityRegistrationBinding
import com.example.hhfoundation.registration.model.ModelRegister
import com.example.hhfoundation.registration.model.ModelSpinner
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat

import java.util.*
import kotlin.collections.ArrayList

class Registration : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: ActivityRegistrationBinding
    var fatherList = ArrayList<ModelSpinner>()
    var motherList = ArrayList<ModelSpinner>()
    var studentList = ArrayList<ModelSpinner>()
    var bloodList = ArrayList<ModelSpinner>()
    var pursuingCList = ArrayList<ModelSpinner>()
    var sectionList = ArrayList<ModelSpinner>()
    var districList = ArrayList<ModelSpinner>()
    var father = ""
    var mother = ""
    var student = ""
    var section = ""
    var blood = ""
    var pursuing = ""
    var dOB = ""
    var gender = ""
    var distric = ""
    var mydilaog: Dialog? = null
    private lateinit var sessionManager: SessionManager
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        sessionManager= SessionManager(this@Registration)
        binding.btnSelectImage.setOnClickListener {
            openImageChooser()
        }

        binding.btnRegister.setOnClickListener {
            if (binding.edtAdmissionNumber.text!!.isEmpty()) {
                binding.edtAdmissionNumber.error = "Enter Admission Number"
                binding.edtAdmissionNumber.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtAadhaarNumber.text!!.isEmpty()) {
                binding.edtAadhaarNumber.error = "Enter Aadhar Number"
                binding.edtAadhaarNumber.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtStudentName.text!!.isEmpty()) {
                binding.edtStudentName.error = "Enter Student Number"
                binding.edtStudentName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtFatherName.text!!.isEmpty()) {
                binding.edtFatherName.error = "Enter Father Name"
                binding.edtFatherName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtMotherName.text!!.isEmpty()) {
                binding.edtMotherName.error = "Enter Mother Name"
                binding.edtMotherName.requestFocus()
                return@setOnClickListener
            }
            if (binding.tvDOB.text!!.isEmpty()) {
                myToast(this@Registration, "Select Date Of Birth")
                return@setOnClickListener
            }
            if (pursuing=="Select Class/Course") {
                myToast(this@Registration, "Select Class")
                return@setOnClickListener
            }
            if (binding.edtSchoolName.text!!.isEmpty()) {
                binding.edtSchoolName.error = "Enter School Name"
                binding.edtSchoolName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtEmail.text!!.isEmpty()) {
                binding.edtEmail.error = "Enter Email"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtCollageAddress.text!!.isEmpty()) {
                binding.edtCollageAddress.error = "Enter Address"
                binding.edtCollageAddress.requestFocus()
                return@setOnClickListener
            }

            if (binding.edtMobileNumber.text!!.isEmpty()) {
                binding.edtMobileNumber.error = "Enter Mobile Number"
                binding.edtMobileNumber.requestFocus()
                return@setOnClickListener
            }

            if (binding.radioMale.isChecked) {
                gender = "Male"
            }
            if (binding.radioFemale.isChecked) {
                gender = "Female"
            }
            apiCallRegistration()

        }


        fatherList.add(ModelSpinner("Mr.", "1"))
        fatherList.add(ModelSpinner("Dr.", "2"))

        sectionList.add(ModelSpinner("A", "1"))
        sectionList.add(ModelSpinner("B", "2"))
        sectionList.add(ModelSpinner("C", "3"))



        motherList.add(ModelSpinner("Mrs.", "1"))
        motherList.add(ModelSpinner("Dr.", "2"))

        studentList.add(ModelSpinner("Mr.", "1"))
        studentList.add(ModelSpinner("Ms.", "2"))
        studentList.add(ModelSpinner("Masters.", "3"))
        studentList.add(ModelSpinner("Baby", "4"))

        bloodList.add(ModelSpinner("A+", "1"))
        bloodList.add(ModelSpinner("A-", "2"))
        bloodList.add(ModelSpinner("B+", "3"))
        bloodList.add(ModelSpinner("AB+", "4"))
        bloodList.add(ModelSpinner("AB-", "5"))
        bloodList.add(ModelSpinner("O+", "6"))
        bloodList.add(ModelSpinner("O-", "7"))

        pursuingCList.add(ModelSpinner("Select Class/Course", "1"))
        pursuingCList.add(ModelSpinner("Class 5", "2"))
        pursuingCList.add(ModelSpinner("Class 6", "2"))
        pursuingCList.add(ModelSpinner("Class 7", "2"))
        pursuingCList.add(ModelSpinner("Class 8", "2"))
        pursuingCList.add(ModelSpinner("Class 9", "2"))
        pursuingCList.add(ModelSpinner("Class 10", "2"))


        districList.add(ModelSpinner("AsifNagar", "1"))
        districList.add(ModelSpinner("ADILABAD", "1"))
        districList.add(ModelSpinner("BHADRADRI KOTHAGUDEM", "1"))
        districList.add(ModelSpinner("HANUMAKONDA", "1"))
        districList.add(ModelSpinner("HYDERABAD", "1"))
        districList.add(ModelSpinner("JAGTIAL", "1"))
        districList.add(ModelSpinner("JANGOAN", "1"))
        districList.add(ModelSpinner("JAYASHANKAR BHOOPALPALLY", "1"))
        districList.add(ModelSpinner("JOGULAMBA GADWAL", "1"))
        districList.add(ModelSpinner("KAMAREDDY", "1"))
        districList.add(ModelSpinner("KARIMNAGAR", "1"))
        districList.add(ModelSpinner("KHAMMAM", "1"))
        districList.add(ModelSpinner("KOMARAM BHEEM ASIFABAD", "1"))
        districList.add(ModelSpinner("MAHABUBABAD", "1"))
        districList.add(ModelSpinner("MAHABUBNAGAR", "1"))
        districList.add(ModelSpinner("MANCHERIAL", "1"))
        districList.add(ModelSpinner("MEDAK", "1"))
        districList.add(ModelSpinner("MEDCHAL-MALKAJGIRI", "1"))
        districList.add(ModelSpinner("MULUG", "1"))
        districList.add(ModelSpinner("NAGARKURNOOL", "1"))
        districList.add(ModelSpinner("NALGONDA", "1"))
        districList.add(ModelSpinner("NARAYANPET", "1"))
        districList.add(ModelSpinner("NIRMAL", "1"))
        districList.add(ModelSpinner("NIZAMABAD", "1"))
        districList.add(ModelSpinner("PEDDAPALLI", "1"))
        districList.add(ModelSpinner("RAJANNA SIRCILLA", "1"))
        districList.add(ModelSpinner("RANGAREDDY", "1"))
        districList.add(ModelSpinner("SANGAREDDY", "1"))
        districList.add(ModelSpinner("SIDDIPET", "1"))
        districList.add(ModelSpinner("SURYAPET", "1"))
        districList.add(ModelSpinner("VIKARABAD", "1"))
        districList.add(ModelSpinner("WANAPARTHY", "1"))
        districList.add(ModelSpinner("WARANGAL", "1"))
        districList.add(ModelSpinner("YADADRI BHUVANAGIRI", "1"))



        binding.spinnerFatherName!!.adapter = ArrayAdapter<ModelSpinner>(
            this@Registration,
            R.layout.simple_list_item_1,
            fatherList
        )

        binding.spinnerMotherName!!.adapter = ArrayAdapter<ModelSpinner>(
            this@Registration,
            R.layout.simple_list_item_1,
            motherList
        )

        binding.spinnerStudentName!!.adapter = ArrayAdapter<ModelSpinner>(
            this@Registration,
            R.layout.simple_list_item_1,
            studentList
        )

        binding.spinnerBloodG!!.adapter = ArrayAdapter<ModelSpinner>(
            this@Registration,
            R.layout.simple_list_item_1,
            bloodList
        )

        binding.spinnerPursuingC!!.adapter = ArrayAdapter<ModelSpinner>(
            this@Registration,
            R.layout.simple_list_item_1,
            pursuingCList
        )

        binding.spinnerSection!!.adapter = ArrayAdapter<ModelSpinner>(
            this@Registration,
            R.layout.simple_list_item_1,
            sectionList
        )

        binding.spinnerSchoolDistric!!.adapter = ArrayAdapter<ModelSpinner>(
            this@Registration,
            R.layout.simple_list_item_1,
            districList
        )

        binding.spinnerFatherName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (fatherList.size > 0) {
                        father = fatherList[i].text

                        Log.e(ContentValues.TAG, "father: $father")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }

        binding.spinnerMotherName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (motherList.size > 0) {
                        mother = motherList[i].text

                        Log.e(ContentValues.TAG, "father: $mother")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        binding.spinnerStudentName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (studentList.size > 0) {
                        student = studentList[i].text

                        Log.e(ContentValues.TAG, "student: $student")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        binding.spinnerSection.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (sectionList.size > 0) {
                        section = sectionList[i].text

                        Log.e(ContentValues.TAG, "section: $section")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        binding.spinnerBloodG.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (bloodList.size > 0) {
                        blood = bloodList[i].text

                        Log.e(ContentValues.TAG, "blood: $blood")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        binding.spinnerPursuingC.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (pursuingCList.size > 0) {
                        pursuing = pursuingCList[i].text

                        Log.e(ContentValues.TAG, "pursuing: $pursuing")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        binding.spinnerSchoolDistric.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (districList.size > 0) {
                        distric = districList[i].text

                        Log.e(ContentValues.TAG, "pursuing: $pursuing")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this@Registration,
            { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                DateFormat.getDateInstance().format(newDate.time)
                // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                dOB = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDOB.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                val selectedDate =
                    SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)

                Log.e(ContentValues.TAG, "dOB:>>$dOB")
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )
        //datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;

        binding.tvDOB.setOnClickListener {
            datePicker.show()
        }
    }


    @SuppressLint("Recycle")
    private fun apiCallRegistration() {
        if (selectedImageUri == null) {
            myToast(this@Registration, "Select an Image First")
            return
        }
        AppProgressBar.showLoaderDialog(this)
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image", this)
        ApiClient.apiService.registerPatient(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            binding.edtStudentName.text.toString().trim(),
            "",
            "",
            "",
            binding.edtMobileNumber.text.toString().trim(),
            gender,
            binding.edtSchoolName.text.toString().trim(),
            binding.edtEmail.text.toString().trim(),
            binding.edtCollageAddress.text.toString().trim(),
            binding.edtMobileNumber.text.toString().trim(),
            "",
            "",
            dOB,
            blood,
            section,
            "",
            "",
            "",
            "",
            binding.edtAdmissionNumber.text.toString().trim(),
            binding.edtAadhaarNumber.text.toString().trim(),
            distric,
            "",
            MultipartBody.Part.createFormData("img_url", file.name, body)
        ).enqueue(object : Callback<ModelRegister> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelRegister>, response: Response<ModelRegister>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@Registration, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@Registration, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if(response.body()!!.message=="successful") {
                        myToast(this@Registration, "${response.body()!!.message}")
                        onBackPressed()
                        AppProgressBar.hideLoaderDialog()
                    } else{
                        myToast(this@Registration, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@Registration, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelRegister>, t: Throwable) {
                apiCallRegistration()
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
                    binding!!.tvNoImage.text = "Image Selected"
                    binding!!.tvNoImage.setTextColor(Color.parseColor("#FF4CAF50"));
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
}