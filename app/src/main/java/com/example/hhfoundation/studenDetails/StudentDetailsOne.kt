package com.example.hhfoundation.studenDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hhfoundation.R
import com.example.hhfoundation.databinding.ActivityStudentDetailsOneBinding
import com.example.hhfoundation.databinding.ActivityStudentDetailsTwoBinding

class StudentDetailsOne : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailsOneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNext.setOnClickListener {
            anemia=binding.anemmla.text.toString().trim()
            BitotSpot=binding.bitotSpot.text.toString().trim()
            Rickets=binding.Rickets.text.toString().trim()
            Malnutrition=binding.Malnutrition.text.toString().trim()
            Goitre=binding.Goitre.text.toString().trim()
            Eczema=binding.Eczema.text.toString().trim()
            OtitisMedia=binding.OtitisMedia.text.toString().trim()
            heartDisease=binding.heartdisease.text.toString().trim()
            Respiratory=binding.Respiratory.text.toString().trim()
            DentalConditions=binding.DentalConditions.text.toString().trim()

            startActivity(Intent(this@StudentDetailsOne, StudentDetailsTwo::class.java))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        var anemia = ""
        var BitotSpot = ""
        var Rickets = ""
        var Malnutrition = ""
        var Goitre = ""
        var Eczema = ""
        var OtitisMedia = ""
        var heartDisease = ""
        var Respiratory = ""
        var DentalConditions = ""
        var Episodes = ""
        var VisionImpairment = ""
        var HearingImpairment = ""
        var motorImpairment = ""
        var MotorDelay = ""
        var LanguageDelay = ""
        var Autism = ""
        var LearningDisorder = ""
        var hyperactivityDisorder = ""
        var Occurringinyourbody = ""
        var Smokingordrinking = ""
        var Depressedmost = ""
        var MenstrualCycleStarted = ""
        var PeriodsEverymonths = ""


    }
}