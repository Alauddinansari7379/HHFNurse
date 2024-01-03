package com.example.hhfoundation.doctor.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
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
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.dasboard.Dashboard
import com.example.hhfoundation.databinding.ActivityAddDoctorBinding
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

class AddDoctor : AppCompatActivity(), UploadRequestBody.UploadCallback {
    private lateinit var binding: ActivityAddDoctorBinding
    private lateinit var sessionManager: SessionManager
    private var selectedImageUri: Uri? = null
    var departmentList = ArrayList<ModelSpinner>()
    var department = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@AddDoctor)


        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSelectImage.setOnClickListener {
            openImageChooser()
        }

        binding.btnRegister.setOnClickListener {
            if (binding.Name.text!!.isEmpty()) {
                binding.Name.error = "Enter Name"
                binding.Name.requestFocus()
                return@setOnClickListener
            }
            if (binding.Email.text!!.isEmpty()) {
                binding.Email.error = "Enter Email"
                binding.Email.requestFocus()
                return@setOnClickListener
            }

            if (binding.password.text!!.isEmpty()) {
                binding.password.error = "Enter Password"
                binding.password.requestFocus()
                return@setOnClickListener
            }
            if (binding.Address.text!!.isEmpty()) {
                binding.Address.error = "Enter Address"
                binding.Address.requestFocus()
                return@setOnClickListener
            }

            if (binding.phone.text!!.isEmpty()) {
                binding.phone.error = "Enter phone"
                binding.phone.requestFocus()
                return@setOnClickListener
            }
            apiCallAddDoctor()
        }

        departmentList.add(ModelSpinner("Gynae &amp Obstetrics", "1"))
        departmentList.add(ModelSpinner("Ophthalmology", "2"))
        departmentList.add(ModelSpinner("Dental", "3"))
        departmentList.add(ModelSpinner("Mental Health Disorders", "4"))
        departmentList.add(ModelSpinner("General Medicine", "4"))
        departmentList.add(ModelSpinner("General Medicine -ID", "4"))
        departmentList.add(ModelSpinner("General Medicine -NCD", "4"))
        departmentList.add(ModelSpinner("General Medicines-Trauma", "4"))

        binding.spinnerDepartment!!.adapter = ArrayAdapter<ModelSpinner>(
            this@AddDoctor, R.layout.simple_list_item_1, departmentList
        )

        binding.spinnerDepartment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View?, i: Int, l: Long
                ) {
                    if (departmentList.size > 0) {
                        department = departmentList[i].text

                        Log.e(ContentValues.TAG, "father: $department")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }
    }

    private fun apiCallAddDoctor() {
        if (selectedImageUri == null) {
            myToast(this@AddDoctor, "Select an Image First")
            return
        }
        AppProgressBar.showLoaderDialog(this)
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)

        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file, "image", this)

        ApiClient.apiService.adddoctor(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            binding.Name.text.toString().trim(),
            binding.password.text.toString().trim(),
            binding.Email.text.toString().trim(),
            binding.Address.text.toString().trim(),
            binding.phone.text.toString().trim(),
            department,
            binding.RegisterationNumber.text.toString().trim(),
            binding.Profile.text.toString().trim(),
            MultipartBody.Part.createFormData("img_url", file.name, body),
        ).enqueue(object : Callback<ModelNewAppoint> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelNewAppoint>, response: Response<ModelNewAppoint>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@AddDoctor, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@AddDoctor, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.body()!!.message == "successful") {
                        myToast(this@AddDoctor, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                        startActivity(Intent(this@AddDoctor, Dashboard::class.java))

                    } else if (response.body()!!.message == "email already registered") {
                        myToast(this@AddDoctor, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()

                   }
                    else {
                        myToast(this@AddDoctor, "${response.body()!!.message}")
                        onBackPressed()
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@AddDoctor, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                apiCallAddDoctor()
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
//                    binding.imageViewNew.visibility = View.VISIBLE
//                    imageView?.setImageURI(selectedImageUri)
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