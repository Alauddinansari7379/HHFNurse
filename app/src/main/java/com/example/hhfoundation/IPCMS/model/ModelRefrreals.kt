package com.example.hhfoundation.IPCMS.model

import com.example.hhfoundation.labReport.model.Prescriptiondetail

data class ModelRefrreals(
    val medicinedetails: List<Medicinedetail>,
    val prescriptiondetails: ArrayList<Prescriptiondetail>
)