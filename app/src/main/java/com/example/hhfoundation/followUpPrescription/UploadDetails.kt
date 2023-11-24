package com.example.hhfoundation.followUpPrescription

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
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.ImageUploadClass.UploadRequestBody
import com.example.hhfoundation.Helper.currentDate
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.dasboard.Dashboard
import com.example.hhfoundation.databinding.ActivityUploadDetailsBinding
import com.example.hhfoundation.labReport.model.ModelUpload
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

class UploadDetails : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: ActivityUploadDetailsBinding
    private var context = this@UploadDetails
    private var expiredDate = ""
    private var presId = ""
    var follow = ""
    var followReason = ""
    private var temp = ""
    private var saturation = ""
    private var patientid = ""
    private var date = ""
    private var studentName = ""
    private var healthIssue = ""
    var presc = ""
    var time = ""
    var choseFile = ""
    var departmentName = ""
    var referHospital = ""
    var appoitmentId = ""
    private val mydilaog: Dialog? = null
    private var selectedImageUri: Uri? = null
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@UploadDetails)
        patientid = intent.getStringExtra("patientid").toString()
        studentName = intent.getStringExtra("patientname").toString()
        date = intent.getStringExtra("date").toString()
        time = intent.getStringExtra("created_at").toString()
        presc = intent.getStringExtra("presc").toString()
        presId = intent.getStringExtra("pid").toString()
        healthIssue = intent.getStringExtra("health_issue").toString()
        referHospital = intent.getStringExtra("refer_hospital").toString()
        departmentName = intent.getStringExtra("department_name").toString()
        appoitmentId = intent.getStringExtra("appoitment_id").toString()

        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            tvPresId.text=presId
            PrescDate.text=date
            ReferHos.text=referHospital
            DepartmentName.text=departmentName
            healtissuse.text=healthIssue
            reviewDate.text= currentDate

            choseFile1.setOnClickListener {
                choseFile="1"
                openImageChooser()
            }
            choseFile2.setOnClickListener {
                choseFile="2"
                openImageChooser()
            }
            choseFile1R.setOnClickListener {
                choseFile="3"
                openImageChooser()
            }
            choseFile2R.setOnClickListener {
                choseFile="4"
                openImageChooser()
            }
            btnSubmit.setOnClickListener {
                apiCallUploadDet()
            }

/*

            mydilaog?.setCanceledOnTouchOutside(false)
            mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val newCalendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    DateFormat.getDateInstance().format(newDate.time)
                    // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                    expiredDate =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                    binding.date.text =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)


                    Log.e(ContentValues.TAG, "expiredDate:>>$expiredDate")
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            //datePicker.datePicker.minDate = System.currentTimeMillis() - 1000;

            binding.date.setOnClickListener {
                datePicker.show()
            }*/
        }
    }

    private fun apiCallUploadDet() {
        if (selectedImageUri == null) {
            myToast(context, "Select an Image First")
            return
        }
        AppProgressBar.showLoaderDialog(this)
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)
        ApiClient.apiService.uploadDetails(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            presId,
            expiredDate,
            referHospital,
            departmentName,
            healthIssue,
            appoitmentId,
            currentDate,
            binding.edtPreNote.text.toString().trim(),
            binding.edtReportNote.text.toString().trim(),
            MultipartBody.Part.createFormData("img_url1", file.name, body),
        ).enqueue(object : Callback<ModelUpload> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelUpload>, response: Response<ModelUpload>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(context, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(context, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.body()!!.message.contentEquals("added")) {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        startActivity(Intent(context, Dashboard::class.java))
                    } else {
                        myToast(context, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(context, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
                apiCallUploadDet()
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
                    binding!!.NoFileChosen1.setTextColor(Color.parseColor("#FF4CAF50"))
                    binding!!.NoFileChosen1.text = "Image Selected"

                    when (choseFile) {
                        "1" -> {
                            binding!!.NoFileChosen1.setTextColor(Color.parseColor("#FF4CAF50"))
                            binding!!.NoFileChosen1.text = "Image Selected"
                        }
                        "2" -> {
                            binding!!.NoFileChosen2.setTextColor(Color.parseColor("#FF4CAF50"))
                            binding!!.NoFileChosen2.text = "Image Selected"

                        }
                        "3" -> {
                            binding!!.NoFileChosen1R.setTextColor(Color.parseColor("#FF4CAF50"))
                            binding!!.NoFileChosen1R.text = "Image Selected"
                        }
                        "4" -> {
                            binding!!.NoFileChosen2R.setTextColor(Color.parseColor("#FF4CAF50"))
                            binding!!.NoFileChosen2R.text = "Image Selected"
                        }
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
}
