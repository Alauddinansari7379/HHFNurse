package com.example.hhfoundation.addPrescription.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelSubCatList {
    @SerializedName("subcat")
    @Expose
    var result: List<Result>? = null

    inner class Result {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("hospital_id")
        @Expose
        var hospital_id: String? = null

        @SerializedName("date")
        @Expose
        var date: String? = null

        @SerializedName("main_id")
        @Expose
        var main_id: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null


    }
}