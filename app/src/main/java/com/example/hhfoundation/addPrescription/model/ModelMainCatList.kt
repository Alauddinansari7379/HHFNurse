package com.example.hhfoundation.addPrescription.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelMainCatList {
    @SerializedName("maincat")
    @Expose
    var result: List<Result>? = null

    inner class Result {
        @SerializedName("departid")
        @Expose
        var departid: Int? = null

        @SerializedName("departmentname")
        @Expose
        var departmentname: String? = null


    }
}