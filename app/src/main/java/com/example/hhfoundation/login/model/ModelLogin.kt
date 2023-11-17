package com.example.hhfoundation.login.model

data class ModelLogin(
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