package com.example.borrowbox.shared.model

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val studentId: String
)
