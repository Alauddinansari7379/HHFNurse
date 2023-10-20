package com.example.hhfoundation.studenDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hhfoundation.R
import com.example.hhfoundation.databinding.ActivityStudentDetailsBinding

class StudentDetails : AppCompatActivity() {
    private lateinit var binding:ActivityStudentDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this@StudentDetails, StudentDetailsOne::class.java))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}