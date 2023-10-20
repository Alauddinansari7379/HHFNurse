package com.example.hhfoundation.followUpPrescription.model

data class ModelReferralsFollow(
    val doctors: ArrayList<DoctorX>,
    val patients: List<PatientX>,
    val prescriptions: ArrayList<PrescriptionX>,
    val prssce: List<Prssce>,
    val settings: SettingsX
)