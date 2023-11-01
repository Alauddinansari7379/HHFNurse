package com.example.hhfoundation.labReport.activity

 import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
 import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
 import com.example.hhfoundation.databinding.ActivityViewLabReportBinding
import java.io.IOException
import java.io.InputStream
import java.net.URL


class ViewLabReport : AppCompatActivity() {
    private lateinit var binding:ActivityViewLabReportBinding
     var mainHandler = Handler()
    var progressDialog: ProgressDialog? = null
    private var reportData = ""
    var prescription = ""
    var pid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewLabReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        reportData = intent.getStringExtra("image").toString()
        prescription = intent.getStringExtra("prescription").toString()
        pid = intent.getStringExtra("pid").toString()


        if (prescription=="1") {
//            binding.web.loadUrl("https://schoolhms.thedemostore.in/auth/prespdf?id=$pid")
//
//            binding.web.settings.javaScriptEnabled = true
//
//
//            binding.web.webViewClient = WebViewClient()
            startActivity(
                // Use 'launchPdfFromPath' if you want to use assets file (enable "fromAssets" flag) / internal directory
                com.rajat.pdfviewer.PdfViewerActivity.launchPdfFromUrl(           //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
                    this@ViewLabReport,
                    "https://schoolhms.thedemostore.in/auth/prespdf?id=$pid",                                // PDF URL in String format
                    "Report",                        // PDF Name/Title in String format
                    "pdf directory to save",                  // If nothing specific, Put "" it will save to Downloads
                    enableDownload = false                    // This param is true by defualt.
                )
            )
        }
        else {
            val url = "https://schoolhms.thedemostore.in/$reportData"
             FetchImage(url).start()
        }
    }

    internal inner class FetchImage(var URL: String) : Thread() {
        var bitmap: Bitmap? = null
        override fun run() {
            mainHandler.post {
                progressDialog = ProgressDialog(this@ViewLabReport)
                progressDialog!!.setMessage("Getting Report Image....")
                progressDialog!!.setCancelable(true)
                progressDialog!!.show()
            }
            var inputStream: InputStream? = null
            try {
                inputStream = URL(URL).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mainHandler.post {
                if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                binding.imageView2!!.setImageBitmap(bitmap)
            }
        }
    }

}