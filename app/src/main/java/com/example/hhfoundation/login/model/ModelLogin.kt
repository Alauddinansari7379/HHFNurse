package com.example.hhfoundation.login.model

import com.google.gson.annotations.Expose

data class ModelLogin(
    @Expose
    val error: Any,
    val expiresIn: Int,
    val hospital_id: String,
    val idToken: String,
    val group: String,
    val ion_id: String,
    val user_type: String,
    val message: String,
    val user_id: String
)