package com.example.hhfoundation.dasboard.model

data class ModelDashboard(
    val adivise: Int,
    val completlab: Int,
    val emergency: Int,
    val follow: Int,
    val general: Int,
    val medicalhistory: Int,
    val pending: Int,
    val pendinglab: Int,
    val refer: Int,
    val todaysick: Int,
    val totalsick: Int,
    val treated: Int,
    val message: String?
)