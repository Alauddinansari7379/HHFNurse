package com.example.hhfoundation.labReport.model

data class Labreport(
    val created_at: String,
    val description: String,
    val doctor: String,
    val id: String,
    val img_url: String,
    val isActive: String,
    val name: String,
    val patient_id: String,
    val prescription_id: String,
    val requested_date: String,
    val title: String
)