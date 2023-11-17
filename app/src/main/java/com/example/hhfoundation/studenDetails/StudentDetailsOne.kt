package com.example.hhfoundation.studenDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.hhfoundation.Helper.*
import com.example.hhfoundation.Helper.DateUtils.getAgeFromDOB
import com.example.hhfoundation.R
import com.example.hhfoundation.clinicalManagement.model.ModelNewAppoint
import com.example.hhfoundation.databinding.ActivityStudentDetailsOneBinding
import com.example.hhfoundation.databinding.ActivityStudentDetailsTwoBinding
import com.example.hhfoundation.medicalHistory.activity.AllMedicalHistory
import com.example.hhfoundation.registration.adapter.AdapterPatientList
import com.example.hhfoundation.retrofit.ApiClient
import com.example.hhfoundation.sharedpreferences.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatterBuilder

class StudentDetailsOne : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailsOneBinding
    var age = ""
    var numberOfInputWords = 0
    lateinit var sessionManager: SessionManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        // yyyy-MM-dd

        val dob = intent.getStringExtra("birthdate")
        Log.e("Before", dob.toString())

        //  Age < 6 the not show (1-developmental delays and Disabilities. 2-Adolescent Specific questionnaire.)
        if (dob != null) {
            try {
                var fDOb = ""
                fDOb = if (dob.contains("-", ignoreCase = true)) {
                    changeDateFormat5(dob)
                } else {
                    changeDateFormat6(dob)
                }
                //dd/MM/yyyy
                Log.e("after", dob.toString())

                age = getAgeFromDOB(fDOb)
                Log.e("dob", dob.toString())
                Log.e("Age", age)
                val cAge = age.substring(0, 2)
                val cAgeNew = age.replace("[^0-9]".toRegex(), "")
                Log.e("cAgeNew", cAgeNew)

//                var string = "vsdhfnmsdbvfuf121535435aewr"
//                var res = string.replace("[^0-9]".toRegex(), "")
//                println(res)

                if (cAgeNew.toInt() < 6) {
                    binding.layoutDisabilities.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



        with(binding) {

            btnSubmit.setOnClickListener {
                apiCallAddMedicalHistory()

            }
            imgBack.setOnClickListener {
                onBackPressed()
            }

            layoutDefcinencies.visibility=View.VISIBLE
            DeficienciesCard.setOnClickListener {
                DeficienciesCard.setBackgroundResource(R.color.main_color)
                ChildhoodCard.setBackgroundResource(R.color.sky_color)
                DevelopmentalCard.setBackgroundResource(R.color.sky_color)
                AdolescentCard.setBackgroundResource(R.color.sky_color)

                layoutDefcinencies.visibility=View.VISIBLE
                layoutChildhood.visibility=View.GONE
                layoutDisabilities.visibility=View.GONE
                layoutAdolescentSpecific.visibility=View.GONE
                layoutknowCaseOf.visibility=View.GONE
            }

            ChildhoodCard.setOnClickListener {
                DeficienciesCard.setBackgroundResource(R.color.sky_color)
                ChildhoodCard.setBackgroundResource(R.color.main_color)
                DevelopmentalCard.setBackgroundResource(R.color.sky_color)
                AdolescentCard.setBackgroundResource(R.color.sky_color)

                layoutDefcinencies.visibility=View.GONE
                layoutChildhood.visibility=View.VISIBLE
                layoutDisabilities.visibility=View.GONE
                layoutAdolescentSpecific.visibility=View.GONE
                layoutknowCaseOf.visibility=View.GONE
            }
            DevelopmentalCard.setOnClickListener {
                DeficienciesCard.setBackgroundResource(R.color.sky_color)
                ChildhoodCard.setBackgroundResource(R.color.sky_color)
                DevelopmentalCard.setBackgroundResource(R.color.main_color)
                AdolescentCard.setBackgroundResource(R.color.sky_color)

                layoutDefcinencies.visibility=View.GONE
                layoutChildhood.visibility=View.GONE
                layoutDisabilities.visibility=View.VISIBLE
                layoutAdolescentSpecific.visibility=View.GONE
                layoutknowCaseOf.visibility=View.GONE
            }
            AdolescentCard.setOnClickListener {
                DeficienciesCard.setBackgroundResource(R.color.sky_color)
                ChildhoodCard.setBackgroundResource(R.color.sky_color)
                DevelopmentalCard.setBackgroundResource(R.color.sky_color)
                AdolescentCard.setBackgroundResource(R.color.main_color)

                layoutDefcinencies.visibility=View.GONE
                layoutChildhood.visibility=View.GONE
                layoutDisabilities.visibility=View.GONE
                layoutAdolescentSpecific.visibility=View.VISIBLE
                layoutknowCaseOf.visibility=View.GONE
            }
            KnowCard.setOnClickListener {
                DeficienciesCard.setBackgroundResource(R.color.sky_color)
                ChildhoodCard.setBackgroundResource(R.color.sky_color)
                DevelopmentalCard.setBackgroundResource(R.color.sky_color)
                AdolescentCard.setBackgroundResource(R.color.sky_color)

                layoutDefcinencies.visibility=View.GONE
                layoutChildhood.visibility=View.GONE
                layoutDisabilities.visibility=View.GONE
                layoutAdolescentSpecific.visibility=View.GONE
                layoutknowCaseOf.visibility=View.VISIBLE
            }



            anemmlaNo.isChecked = true
            anemmlaNo.setOnCheckedChangeListener { _, _ ->
                if (anemmlaNo.isChecked) {
                    anemmla.visibility = View.GONE
                    anemmla.text!!.clear()
                }
            }

            anemmlaYes.setOnCheckedChangeListener { _, _ ->
                if (anemmlaYes.isChecked) {
                    anemmla.visibility = View.VISIBLE
                }
            }

            bitotSpotNo.isChecked = true
            bitotSpotNo.setOnCheckedChangeListener { _, _ ->
                if (bitotSpotNo.isChecked) {
                    bitotSpot.visibility = View.GONE
                    bitotSpot.text!!.clear()
                }
            }

            bitotSpotYes.setOnCheckedChangeListener { _, _ ->
                if (bitotSpotYes.isChecked) {
                    bitotSpot.visibility = View.VISIBLE
                }
            }

            RicketsNo.isChecked = true
            RicketsNo.setOnCheckedChangeListener { _, _ ->
                if (RicketsNo.isChecked) {
                    Rickets.visibility = View.GONE
                    Rickets.text!!.clear()
                }
            }

            RicketsYes.setOnCheckedChangeListener { _, _ ->
                if (RicketsYes.isChecked) {
                    Rickets.visibility = View.VISIBLE
                }
            }

            MalnutritionNo.isChecked = true
            MalnutritionNo.setOnCheckedChangeListener { _, _ ->
                if (MalnutritionNo.isChecked) {
                    Malnutrition.visibility = View.GONE
                    Malnutrition.text!!.clear()
                }
            }

            MalnutritionYes.setOnCheckedChangeListener { _, _ ->
                if (MalnutritionYes.isChecked) {
                    Malnutrition.visibility = View.VISIBLE
                }
            }

            GoitreNo.isChecked = true
            GoitreNo.setOnCheckedChangeListener { _, _ ->
                if (GoitreNo.isChecked) {
                    Goitre.visibility = View.GONE
                    Goitre.text!!.clear()
                }
            }

            GoitreYes.setOnCheckedChangeListener { _, _ ->
                if (GoitreYes.isChecked) {
                    Goitre.visibility = View.VISIBLE
                }
            }

            skinssNo.isChecked = true
            skinssNo.setOnCheckedChangeListener { _, _ ->
                if (skinssNo.isChecked) {
                    skinss.visibility = View.GONE
                    skinss.text!!.clear()
                }
            }

            skinssYes.setOnCheckedChangeListener { _, _ ->
                if (skinssYes.isChecked) {
                    skinss.visibility = View.VISIBLE
                }
            }

            ottyssNo.isChecked = true
            ottyssNo.setOnCheckedChangeListener { _, _ ->
                if (ottyssNo.isChecked) {
                    ottyss.visibility = View.GONE
                    ottyss.text!!.clear()
                }
            }

            ottyssYes.setOnCheckedChangeListener { _, _ ->
                if (ottyssYes.isChecked) {
                    ottyss.visibility = View.VISIBLE
                }
            }

            heartdiseaseNo.isChecked = true
            heartdiseaseNo.setOnCheckedChangeListener { _, _ ->
                if (heartdiseaseNo.isChecked) {
                    heartdisease.visibility = View.GONE
                    heartdisease.text!!.clear()
                }
            }

            heartdiseaseYes.setOnCheckedChangeListener { _, _ ->
                if (heartdiseaseYes.isChecked) {
                    heartdisease.visibility = View.VISIBLE
                }
            }

            RespiratoryNo.isChecked = true
            RespiratoryNo.setOnCheckedChangeListener { _, _ ->
                if (RespiratoryNo.isChecked) {
                    Respiratory.visibility = View.GONE
                    Respiratory.text!!.clear()
                }
            }

            RespiratoryYes.setOnCheckedChangeListener { _, _ ->
                if (RespiratoryYes.isChecked) {
                    Respiratory.visibility = View.VISIBLE
                }
            }

            DentalConditionsNo.isChecked = true
            DentalConditionsNo.setOnCheckedChangeListener { _, _ ->
                if (DentalConditionsNo.isChecked) {
                    DentalConditions.visibility = View.GONE
                    DentalConditions.text!!.clear()
                }
            }

            DentalConditionsYes.setOnCheckedChangeListener { _, _ ->
                if (DentalConditionsYes.isChecked) {
                    DentalConditions.visibility = View.VISIBLE
                }
            }

            EpisodesNo.isChecked = true
            EpisodesNo.setOnCheckedChangeListener { _, _ ->
                if (EpisodesNo.isChecked) {
                    Episodes.visibility = View.GONE
                    Episodes.text!!.clear()
                }
            }

            EpisodesYes.setOnCheckedChangeListener { _, _ ->
                if (EpisodesYes.isChecked) {
                    Episodes.visibility = View.VISIBLE
                }
            }

            visionsNo.isChecked = true
            visionsNo.setOnCheckedChangeListener { _, _ ->
                if (visionsNo.isChecked) {
                    visions.visibility = View.GONE
                    visions.text!!.clear()
                }
            }

            visionsYes.setOnCheckedChangeListener { _, _ ->
                if (visionsYes.isChecked) {
                    visions.visibility = View.VISIBLE
                }
            }

            hearrssNo.isChecked = true
            hearrssNo.setOnCheckedChangeListener { _, _ ->
                if (hearrssNo.isChecked) {
                    hearrss.visibility = View.GONE
                    hearrss.text!!.clear()
                }
            }

            hearrssYes.setOnCheckedChangeListener { _, _ ->
                if (hearrssYes.isChecked) {
                    hearrss.visibility = View.VISIBLE
                }
            }

            nerosdssNo.isChecked = true
            nerosdssNo.setOnCheckedChangeListener { _, _ ->
                if (nerosdssNo.isChecked) {
                    nerosdss.visibility = View.GONE
                    nerosdss.text!!.clear()
                }
            }

            nerosdssYes.setOnCheckedChangeListener { _, _ ->
                if (nerosdssYes.isChecked) {
                    nerosdss.visibility = View.VISIBLE
                }
            }

            motrsdsNo.isChecked = true
            motrsdsNo.setOnCheckedChangeListener { _, _ ->
                if (motrsdsNo.isChecked) {
                    motrsds.visibility = View.GONE
                    motrsds.text!!.clear()
                }
            }

            motrsdsYes.setOnCheckedChangeListener { _, _ ->
                if (motrsdsYes.isChecked) {
                    motrsds.visibility = View.VISIBLE
                }
            }

            cogsaNo.isChecked = true
            cogsaNo.setOnCheckedChangeListener { _, _ ->
                if (cogsaNo.isChecked) {
                    cogsaNo.visibility = View.GONE
                    cogsa.text!!.clear()
                }
            }

            cogsaYes.setOnCheckedChangeListener { _, _ ->
                if (cogsaYes.isChecked) {
                    cogsa.visibility = View.VISIBLE
                }
            }

            lagsaNo.isChecked = true
            lagsaNo.setOnCheckedChangeListener { _, _ ->
                if (lagsaNo.isChecked) {
                    lagsa.visibility = View.GONE
                    lagsa.text!!.clear()
                }
            }

            lagsaYes.setOnCheckedChangeListener { _, _ ->
                if (lagsaYes.isChecked) {
                    lagsa.visibility = View.VISIBLE
                }
            }

            begaasdsNo.isChecked = true
            begaasdsNo.setOnCheckedChangeListener { _, _ ->
                if (begaasdsNo.isChecked) {
                    begaasds.visibility = View.GONE
                    begaasds.text!!.clear()
                }
            }

            begaasdsYes.setOnCheckedChangeListener { _, _ ->
                if (begaasdsYes.isChecked) {
                    begaasds.visibility = View.VISIBLE
                }
            }

            learnsdsNo.isChecked = true
            learnsdsNo.setOnCheckedChangeListener { _, _ ->
                if (learnsdsNo.isChecked) {
                    learnsds.visibility = View.GONE
                    learnsds.text!!.clear()
                }
            }

            learnsdsYes.setOnCheckedChangeListener { _, _ ->
                if (learnsdsYes.isChecked) {
                    learnsds.visibility = View.VISIBLE
                }
            }

            disorsdNo.isChecked = true
            disorsdNo.setOnCheckedChangeListener { _, _ ->
                if (disorsdNo.isChecked) {
                    disorsd.visibility = View.GONE
                    disorsd.text!!.clear()
                }
            }

            disorsdYes.setOnCheckedChangeListener { _, _ ->
                if (disorsdYes.isChecked) {
                    disorsd.visibility = View.VISIBLE
                }
            }

            occurringinyourbodyNo.isChecked = true
            occurringinyourbodyNo.setOnCheckedChangeListener { _, _ ->
                if (occurringinyourbodyNo.isChecked) {
                    occurringinyourbody.visibility = View.GONE
                    occurringinyourbody.text!!.clear()
                }
            }

            occurringinyourbodyYes.setOnCheckedChangeListener { _, _ ->
                if (occurringinyourbodyYes.isChecked) {
                    occurringinyourbody.visibility = View.VISIBLE
                }
            }

            smokingordrinkingNo.isChecked = true
            smokingordrinkingNo.setOnCheckedChangeListener { _, _ ->
                if (smokingordrinkingNo.isChecked) {
                    smokingordrinking.visibility = View.GONE
                    smokingordrinking.text!!.clear()
                }
            }

            smokingordrinkingYes.setOnCheckedChangeListener { _, _ ->
                if (smokingordrinkingYes.isChecked) {
                    smokingordrinking.visibility = View.VISIBLE
                }
            }

            depressedMostNo.isChecked = true
            depressedMostNo.setOnCheckedChangeListener { _, _ ->
                if (depressedMostNo.isChecked) {
                    depressedMost.visibility = View.GONE
                    depressedMost.text!!.clear()
                }
            }

            depressedMostYes.setOnCheckedChangeListener { _, _ ->
                if (depressedMostYes.isChecked) {
                    depressedMost.visibility = View.VISIBLE
                }
            }

            MenstrualCycleStartedNo.isChecked = true
            MenstrualCycleStartedNo.setOnCheckedChangeListener { _, _ ->
                if (MenstrualCycleStartedNo.isChecked) {
                    MenstrualCycleStarted.visibility = View.GONE
                    MenstrualCycleStarted.text!!.clear()
                }
            }

            MenstrualCycleStartedYes.setOnCheckedChangeListener { _, _ ->
                if (MenstrualCycleStartedYes.isChecked) {
                    MenstrualCycleStarted.visibility = View.VISIBLE
                }
            }

            PeriodsEverymonthsNo.isChecked = true
            PeriodsEverymonthsNo.setOnCheckedChangeListener { _, _ ->
                if (PeriodsEverymonthsNo.isChecked) {
                    PeriodsEverymonths.visibility = View.GONE
                    PeriodsEverymonths.text!!.clear()
                }
            }

            PeriodsEverymonthsYes.setOnCheckedChangeListener { _, _ ->
                if (PeriodsEverymonthsYes.isChecked) {
                    PeriodsEverymonths.visibility = View.VISIBLE
                }
            }

            paindsNo.isChecked = true
            paindsNo.setOnCheckedChangeListener { _, _ ->
                if (paindsNo.isChecked) {
                    painds.visibility = View.GONE
                    painds.text!!.clear()
                }
            }

            paindsYes.setOnCheckedChangeListener { _, _ ->
                if (paindsYes.isChecked) {
                    painds.visibility = View.VISIBLE
                }
            }

            areadsNo.isChecked = true
            areadsNo.setOnCheckedChangeListener { _, _ ->
                if (areadsNo.isChecked) {
                    areads.visibility = View.GONE
                    areads.text!!.clear()
                }
            }

            areadsYes.setOnCheckedChangeListener { _, _ ->
                if (areadsYes.isChecked) {
                    areads.visibility = View.VISIBLE
                }
            }

            feeldsNo.isChecked = true
            feeldsNo.setOnCheckedChangeListener { _, _ ->
                if (feeldsNo.isChecked) {
                    feelds.visibility = View.GONE
                    feelds.text!!.clear()
                }
            }

            feeldsYes.setOnCheckedChangeListener { _, _ ->
                if (feeldsYes.isChecked) {
                    feelds.visibility = View.VISIBLE
                }
            }

            binondNo.isChecked = true
            binondNo.setOnCheckedChangeListener { _, _ ->
                if (binondNo.isChecked) {
                    binond.visibility = View.GONE
                    binond.text!!.clear()
                }
            }

            binondYes.setOnCheckedChangeListener { _, _ ->
                if (binondYes.isChecked) {
                    binond.visibility = View.VISIBLE
                }
            }

            hyperthyroidinNo.isChecked = true
            hyperthyroidinNo.setOnCheckedChangeListener { _, _ ->
                if (hyperthyroidinNo.isChecked) {
                    hyperthyroidin.visibility = View.GONE
                    hyperthyroidin.text!!.clear()
                }
            }

            hyperthyroidinYes.setOnCheckedChangeListener { _, _ ->
                if (hyperthyroidinYes.isChecked) {
                    hyperthyroidin.visibility = View.VISIBLE
                }
            }

            diabetescxNo.isChecked = true
            diabetescxNo.setOnCheckedChangeListener { _, _ ->
                if (diabetescxNo.isChecked) {
                    diabetescx.visibility = View.GONE
                    diabetescx.text!!.clear()
                }
            }

            diabetescxYes.setOnCheckedChangeListener { _, _ ->
                if (diabetescxYes.isChecked) {
                    diabetescx.visibility = View.VISIBLE
                }
            }

            hypothyroidinNo.isChecked = true
            hypothyroidinNo.setOnCheckedChangeListener { _, _ ->
                if (hypothyroidinNo.isChecked) {
                    hypothyroidin.visibility = View.GONE
                    hypothyroidin.text!!.clear()
                }
            }

            hypothyroidinYes.setOnCheckedChangeListener { _, _ ->
                if (hypothyroidinYes.isChecked) {
                    hypothyroidin.visibility = View.VISIBLE
                }
            }

            allergieNo.isChecked = true
            allergieNo.setOnCheckedChangeListener { _, _ ->
                if (allergieNo.isChecked) {
                    allergie.visibility = View.GONE
                    allergie.text!!.clear()
                }
            }

            allergieYes.setOnCheckedChangeListener { _, _ ->
                if (allergieYes.isChecked) {
                    allergie.visibility = View.VISIBLE
                }
            }

            heartinNo.isChecked = true
            heartinNo.setOnCheckedChangeListener { _, _ ->
                if (heartinNo.isChecked) {
                    heartin.visibility = View.GONE
                    heartin.text!!.clear()
                }
            }

            heartinYes.setOnCheckedChangeListener { _, _ ->
                if (heartinYes.isChecked) {
                    heartin.visibility = View.VISIBLE
                }
            }

            almedcineNo.isChecked = true
            almedcineNo.setOnCheckedChangeListener { _, _ ->
                if (almedcineNo.isChecked) {
                    almedcine.visibility = View.GONE
                    almedcine.text!!.clear()
                }
            }

            almedcineYes.setOnCheckedChangeListener { _, _ ->
                if (almedcineYes.isChecked) {
                    almedcine.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun apiCallAddMedicalHistory() {
        AppProgressBar.showLoaderDialog(this)

        var almedcine = ""
        var past = ""
        var ongoing = ""
        var surgical = ""
        var heartin = ""
        var hyperthyroidin = ""
        var hypothyroidin = ""
        var anemmla = ""
        var bitotSpot = ""
        var Rickets = ""
        var Malnutrition = ""
        var Goitre = ""
        var skinss = ""
        var ottyss = ""
        var heartdisease = ""
        var Respiratory = ""
        var DentalConditions = ""
        var Episodes = ""
        var visions = ""
        var hearrss = ""
        var nerosdss = ""
        var motrsds = ""
        var cogsa = ""
        var lagsa = ""
        var begaasds = ""
        var learnsds = ""
        var disorsd = ""
        var occurringinyourbody = ""
        var smokingordrinking = ""
        var depressedMost = ""
        var MenstrualCycleStarted = ""
        var PeriodsEverymonths = ""
        var painds = ""
        var areads = ""
        var feelds = ""
        var binond = ""

        almedcine = if (binding.almedcine.text!!.isNotEmpty()) {
            binding.almedcine.text.toString().trim()
        } else {
            "No"
        }

        past = if (binding.past.text!!.isNotEmpty()) {
            binding.past.text.toString().trim()
        } else {
            "No"
        }

        ongoing = if (binding.ongoing.text!!.isNotEmpty()) {
            binding.ongoing.text.toString().trim()
        } else {
            "No"
        }

        surgical = if (binding.surgical.text!!.isNotEmpty()) {
            binding.surgical.text.toString().trim()
        } else {
            "No"
        }

        heartin = if (binding.heartin.text!!.isNotEmpty()) {
            binding.heartin.text.toString().trim()
        } else {
            "No"
        }

        hyperthyroidin = if (binding.hyperthyroidin.text!!.isNotEmpty()) {
            binding.hyperthyroidin.text.toString().trim()
        } else {
            "No"
        }

        hypothyroidin = if (binding.hypothyroidin.text!!.isNotEmpty()) {
            binding.hypothyroidin.text.toString().trim()
        } else {
            "No"
        }

        anemmla = if (binding.anemmla.text!!.isNotEmpty()) {
            binding.anemmla.text.toString().trim()
        } else {
            "No"
        }

        bitotSpot = if (binding.bitotSpot.text!!.isNotEmpty()) {
            binding.bitotSpot.text.toString().trim()
        } else {
            "No"
        }

        Rickets = if (binding.Rickets.text!!.isNotEmpty()) {
            binding.Rickets.text.toString().trim()
        } else {
            "No"
        }
        Malnutrition = if (binding.Malnutrition.text!!.isNotEmpty()) {
            binding.Malnutrition.text.toString().trim()
        } else {
            "No"
        }

        Goitre = if (binding.Goitre.text!!.isNotEmpty()) {
            binding.Goitre.text.toString().trim()
        } else {
            "No"
        }
        skinss = if (binding.skinss.text!!.isNotEmpty()) {
            binding.skinss.text.toString().trim()
        } else {
            "No"
        }

        ottyss = if (binding.ottyss.text!!.isNotEmpty()) {
            binding.ottyss.text.toString().trim()
        } else {
            "No"
        }

        heartdisease = if (binding.heartdisease.text!!.isNotEmpty()) {
            binding.heartdisease.text.toString().trim()
        } else {
            "No"
        }

        Respiratory = if (binding.Respiratory.text!!.isNotEmpty()) {
            binding.Respiratory.text.toString().trim()
        } else {
            "No"
        }
        DentalConditions = if (binding.DentalConditions.text!!.isNotEmpty()) {
            binding.DentalConditions.text.toString().trim()
        } else {
            "No"
        }

        Episodes = if (binding.Episodes.text!!.isNotEmpty()) {
            binding.Episodes.text.toString().trim()
        } else {
            "No"
        }
        visions = if (binding.visions.text!!.isNotEmpty()) {
            binding.visions.text.toString().trim()
        } else {
            "No"
        }

        hearrss = if (binding.hearrss.text!!.isNotEmpty()) {
            binding.hearrss.text.toString().trim()
        } else {
            "No"
        }

        nerosdss = if (binding.nerosdss.text!!.isNotEmpty()) {
            binding.hearrss.text.toString().trim()
        } else {
            "No"
        }
        motrsds = if (binding.motrsds.text!!.isNotEmpty()) {
            binding.motrsds.text.toString().trim()
        } else {
            "No"
        }

        cogsa = if (binding.cogsa.text!!.isNotEmpty()) {
            binding.cogsa.text.toString().trim()
        } else {
            "No"
        }
        lagsa = if (binding.lagsa.text!!.isNotEmpty()) {
            binding.lagsa.text.toString().trim()
        } else {
            "No"
        }

        begaasds = if (binding.begaasds.text!!.isNotEmpty()) {
            binding.begaasds.text.toString().trim()
        } else {
            "No"
        }
        learnsds = if (binding.learnsds.text!!.isNotEmpty()) {
            binding.learnsds.text.toString().trim()
        } else {
            "No"
        }

        disorsd = if (binding.disorsd.text!!.isNotEmpty()) {
            binding.disorsd.text.toString().trim()
        } else {
            "No"
        }
        occurringinyourbody = if (binding.occurringinyourbody.text!!.isNotEmpty()) {
            binding.occurringinyourbody.text.toString().trim()
        } else {
            "No"
        }

        smokingordrinking = if (binding.smokingordrinking.text!!.isNotEmpty()) {
            binding.smokingordrinking.text.toString().trim()
        } else {
            "No"
        }
        depressedMost = if (binding.depressedMost.text!!.isNotEmpty()) {
            binding.depressedMost.text.toString().trim()
        } else {
            "No"
        }

        MenstrualCycleStarted = if (binding.MenstrualCycleStarted.text!!.isNotEmpty()) {
            binding.MenstrualCycleStarted.text.toString().trim()
        } else {
            "No"
        }

        PeriodsEverymonths = if (binding.PeriodsEverymonths.text!!.isNotEmpty()) {
            binding.PeriodsEverymonths.text.toString().trim()
        } else {
            "No"
        }
        painds = if (binding.painds.text!!.isNotEmpty()) {
            binding.painds.text.toString().trim()
        } else {
            "No"
        }

        areads = if (binding.areads.text!!.isNotEmpty()) {
            binding.areads.text.toString().trim()
        } else {
            "No"
        }

        feelds = if (binding.feelds.text!!.isNotEmpty()) {
            binding.feelds.text.toString().trim()
        } else {
            "No"
        }
        binond = if (binding.binond.text!!.isNotEmpty()) {
            binding.binond.text.toString().trim()
        } else {
            "No"
        }


        ApiClient.apiService.addPatientMedical(
            sessionManager.ionId.toString(),
            sessionManager.idToken.toString(),
            sessionManager.group.toString(),
            AdapterPatientList.studentId,
            currentDate,
            "",
            "",
            almedcine,
            "",                                       //surgical
            past,                    // binding.anemmla.text.toString()
            ongoing,
            surgical,
            "0",
            "0",
            "NA",
            "No",
            "",
            "",
            "",
            "",
            "",
            "",
            heartin,
            hyperthyroidin,
            hypothyroidin,
            anemmla,
            bitotSpot,
            Rickets,
            Malnutrition,
            Goitre,
            skinss,
            ottyss,
            heartdisease,
            Respiratory,
            DentalConditions,
            Episodes,
            visions,
            hearrss,
            nerosdss,
            motrsds,
            cogsa,
            lagsa,
            begaasds,
            learnsds,
            disorsd,
            occurringinyourbody,
            smokingordrinking,
            depressedMost,
            MenstrualCycleStarted,
            PeriodsEverymonths,
            painds,
            areads,
            feelds,
            binond,
        ).enqueue(object : Callback<ModelNewAppoint> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelNewAppoint>, response: Response<ModelNewAppoint>
            ) {
                try {
                    if (response.code() == 500) {
                        myToast(this@StudentDetailsOne, "Server Error")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.code() == 404) {
                        myToast(this@StudentDetailsOne, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    } else if (response.body()!!.message == "successful") {
                        myToast(this@StudentDetailsOne, "${response.body()!!.message}")
                        startActivity(Intent(this@StudentDetailsOne, AllMedicalHistory::class.java))
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@StudentDetailsOne, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@StudentDetailsOne, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelNewAppoint>, t: Throwable) {
                apiCallAddMedicalHistory()
                //  myToast(this@ProfileActivity, "Something went wrong")
                AppProgressBar.hideLoaderDialog()

            }

        })
    }


}