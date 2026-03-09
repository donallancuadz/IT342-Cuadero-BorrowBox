package com.example.borrowbox.model

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String
)