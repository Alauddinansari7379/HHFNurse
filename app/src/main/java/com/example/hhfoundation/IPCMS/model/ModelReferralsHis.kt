package com.example.hhfoundation.IPCMS.model

data class ModelReferralsHis(
    val doctors: List<Doctor>,
    val patients: List<Patient>,
    val prescriptions: ArrayList<Prescription>,
    val settings: Settings
)