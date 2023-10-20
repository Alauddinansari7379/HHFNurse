package com.example.hhfoundation.medicalHistory.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hhfoundation.R
import com.example.hhfoundation.databinding.ActivityMedicalHistoryBinding

class MedicalHIstory : AppCompatActivity() {
    private lateinit var binding:ActivityMedicalHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMedicalHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}