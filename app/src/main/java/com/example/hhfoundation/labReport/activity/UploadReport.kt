package com.example.hhfoundation.labReport.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Layout
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hhfoundation.Helper.AppProgressBar
import com.example.hhfoundation.Helper.ImageUploadClass.UploadRequestBody
import com.example.hhfoundation.Helper.myToast
import com.example.hhfoundation.R
import com.example.hhfoundation.databinding.ActivityUploadReportBinding
import com.example.hhfoundation.labReport.model.ModelUpload
import com.example.hhfoundation.registration.model.ModelRegister
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class UploadReport : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: ActivityUploadReportBinding
    lateinit var sessionManager: SessionManager
    private var selectedImageUri: Uri? = null
    var id = ""
    var patientid = ""
    var reportName = ""
    var prescriptionId = ""
    var dialog: Dialog? = null
    private var fileChosser = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@UploadReport)

        id = intent.getStringExtra("id").toString()
        patientid = intent.getStringExtra("patientid").toString()
        reportName = intent.getStringExtra("rname").toString()
        prescriptionId = intent.getStringExtra("prescription_id").toString()

        Log.e("repoaname", reportName)
        Log.e("id", id)
        Log.e("patientid", patientid)
        binding.reportName.text = reportName


        with(binding) {
            btnSelectImage.setOnClickListener {
                popupUpload()
            }
            imgBack.setOnClickListener {
                onBackPressed()
            }

            btnUpload.setOnClickListener {
                if (edtDesc.text.toString().isEmpty()) {
                    edtDesc.error = "Enter Description"
                    edtDesc.requestFocus()
                    return@setOnClickListener
                }
                if (fileChosser == "1") {
                    uploadImageCamera()
                }
                if (fileChosser == "2") {
                    uploadImageGallery()
                }

                if (fileChosser == "3") {
                    uploadImageGallery()
                }
            }

        }


//        binding.layoutCamera.setOnClickListener {
//            //  myToast(this, "Work on Progress")
//            fileChosser = "1"
//            ImagePicker.with(this).cameraOnly()
////                                            .createIntent { intent ->
////                                startForProfileImageResult.launch(intent)
////                            }
//                .start(REQUEST_CODE_IMAGE)
//        }
//
//        binding.layoutGallery.setOnClickListener {
//            fileChosser = "2"
//            openImageChooser()
//        }


    }

    @SuppressLint("WrongViewCast")
    private fun popupUpload() {
        val view = layoutInflater.inflate(R.layout.dialog_upload_report, null)
        dialog = Dialog(this@UploadReport)
        val layoutSnapshot = view!!.findViewById<LinearLayout>(R.id.layoutSnapshot)
        val layoutGallery = view!!.findViewById<LinearLayout>(R.id.layoutGallery)
        val layoutPDF= view!!.findViewById<LinearLayout>(R.id.layoutPDF)
        val imgClose = view!!.findViewById<ImageView>(R.id.imgClose)

        //      val minute = view!!.findViewById<TextView>(R.id.imgClose)
//        val second = view!!.findViewById<TextView>(R.id.tvSecondTime)
        dialog = Dialog(this@UploadReport)

        val currentTime = SimpleDateFormat("yy/MM/dd HH:m:ss", Locale.getDefault()).format(Date())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)
        // val d1 = format.parse("2023/03/29 11:04:00")
//        Log.e("currentDate", currentTime)
//        Log.e("EndTime", startTime)

        dialog?.show()

        imgClose.setOnClickListener {
            dialog?.dismiss()
        }

        layoutGallery.setOnClickListener {
            fileChosser = "2"
            openImageChooser()

        }

        layoutPDF.setOnClickListener {
            fileChosser = "3"
            openImageChooserPDF()

        }

        layoutSnapshot.setOnClickListener {
            fileChosser = "1"
            ImagePicker.with(this).cameraOnly()
//                                            .createIntent { intent ->
//                                startForProfileImageResult.launch(intent)
//                            }
                .start(REQUEST_CODE_IMAGE)
        }
    }

    @SuppressLint("Recycle")
    private fun uploadImageGallery() {
        if (selectedImageUri == null) {
            myToast(this@UploadReport, "Select an Image First")
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
        ApiClient.apiService.addLabReport(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            id,
            prescriptionId,
            patientid,
            binding.reportName.text.toString().trim(),
            binding.edtDesc.text.toString().trim(),
            MultipartBody.Part.createFormData("img_url", file.name, body)
        ).enqueue(object : Callback<ModelUpload> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelUpload>, response: Response<ModelUpload>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@UploadReport, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@UploadReport, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        //myToast(this@UploadReport, "${response.body()!!.message}")
                        val view = layoutInflater.inflate(R.layout.dialog_sucessfull_report, null)
                        dialog = Dialog(this@UploadReport)
                        val btnOkDil = view!!.findViewById<Button>(R.id.btnOkDil)
                         val imgClose = view!!.findViewById<ImageView>(R.id.imgCloseDilS)

                        dialog = Dialog(this@UploadReport)


                        if (view.parent != null) {
                            (view.parent as ViewGroup).removeView(view) // <- fix
                        }
                        dialog!!.setContentView(view)
                        dialog?.setCancelable(true)

                        dialog?.show()

                        imgClose.setOnClickListener {
                            dialog?.dismiss()
                            onBackPressed()
                        }
                        btnOkDil.setOnClickListener {
                            dialog?.dismiss()
                            onBackPressed()
                        }
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@UploadReport, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
                //  myToast(this@ProfileActivity, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
    }

    private fun uploadImageCamera() {

        if (selectedImageUri == null) {
            myToast(this@UploadReport, "Select Report First")
            // binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        val file: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .absolutePath + "/myAppImages/"
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        val file1: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/myAppImages/" + selectedImageUri!!.lastPathSegment
        )

        // val fos = FileOutputStream(file1)

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        // val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file1)
        inputStream.copyTo(outputStream)

        AppProgressBar.showLoaderDialog(this@UploadReport)

        //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file1, "image", this)

        ApiClient.apiService.addLabReport(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            id,
            prescriptionId,
            patientid,
            binding.reportName.text.toString().trim(),
            binding.edtDesc.text.toString().trim(),
            MultipartBody.Part.createFormData("img_url", file.name, body)
        ).enqueue(object :
            Callback<ModelUpload> {
            override fun onResponse(
                call: Call<ModelUpload>,
                response: Response<ModelUpload>
            ) {
                try {
                    response.body()?.let {
                        if (response.code() == 500) {
                            myToast(this@UploadReport, "Server Error")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@UploadReport, response.body()!!.message)
                            onBackPressed()
//                    binding.layoutRoot.snackbar(it.message)
//                    binding.progressBar.progress = 100
                            AppProgressBar.hideLoaderDialog()
                        }

                    }
                } catch (e: java.lang.Exception) {
                    myToast(this@UploadReport, "Something went wrong")
                    e.printStackTrace()
                    AppProgressBar.hideLoaderDialog()

                }
            }


            override fun onFailure(call: Call<ModelUpload>, t: Throwable) {
//                binding.layoutRoot.snackbar(t.message!!)
//                binding.progressBar.progress = 0
                AppProgressBar.hideLoaderDialog()

            }


        })


    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
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

    private fun openImageChooserPDF() {
        Intent(Intent.ACTION_PICK).also {
//            it.type = "image/*"
//            (MediaStore.ACTION_IMAGE_CAPTURE)
//            val mimeTypes = arrayOf("image/jpeg", "image/png")
//            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//            startActivityForResult(it, REQUEST_CODE_IMAGE)
//
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    Log.e("data?.data", data?.data.toString())
                    // binding!!.tvNoImage.text = "Image Selected"
                    binding.tvNoImage.visibility = View.GONE
                    binding!!.tvNoImage.setTextColor(Color.parseColor("#FF4CAF50"));
                    dialog?.dismiss()
                    binding.ImageView.visibility = View.VISIBLE
                    binding.ImageView?.setImageURI(selectedImageUri)
                    if (fileChosser=="3"){
                        binding.tvNoImage.visibility = View.VISIBLE
                        binding!!.tvNoImage.setTextColor(Color.parseColor("#FF4CAF50"))
                        binding.tvNoImage.text= "PDF Selected"
                    }
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