package com.example.hhfoundation.addPrescription.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelMedicineList {
    @SerializedName("medicines")
    @Expose
    var result: List<Result>? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    inner class Result {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("Test_Code")
        @Expose
        var Test_Code: String? = null

        @SerializedName("Test_Name")
        @Expose
        var Test_Name: String? = null

        @SerializedName("medicine")
        @Expose
        var medicine: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

    }
}