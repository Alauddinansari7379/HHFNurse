package com.example.hhfoundation.followUpPrescription.model

data class ModelReferralsHis(
    val doctors: List<Doctor>,
    val patients: List<Patient>,
    val prescriptions: List<Prescription>,
    val settings: Settings
)