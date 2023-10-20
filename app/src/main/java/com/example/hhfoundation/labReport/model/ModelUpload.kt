package com.example.hhfoundation.labReport.model

data class ModelUpload(
    val description: String,
    val img_url: String,
    val lab_inv: String,
    val message: String,
    val patient_id: String,
    val prescription_id: String,
    val title: String
)