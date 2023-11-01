package com.example.hhfoundation.addPrescription.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelLabList {
    @SerializedName("labcategory")
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

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("category")
        @Expose
        var category: String? = null

    }
}