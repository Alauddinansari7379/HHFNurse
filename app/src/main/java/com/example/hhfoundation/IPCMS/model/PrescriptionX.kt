package com.example.hhfoundation.IPCMS.model

data class PrescriptionX(
    val appointment_id: String,
    val cdate: String,
    val disnote: String,
    val doctorname: String,
    val hospital_id: String,
    val hypothyroid: String,
    val id: String,
    val img_url16: String,
    val img_url17: String,
    val p_date: String,
    val p_depart: String,
    val p_hissue: String,
    val p_hosp: String,
    val p_id: String,
    val referfollowd: String,
    val review_date: Any,
    val status: String,
     val patient: ArrayList<Patient>,
     val doctor: ArrayList<Doctor>,
)