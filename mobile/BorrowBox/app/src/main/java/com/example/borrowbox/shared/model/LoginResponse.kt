package com.example.borrowbox.shared.model

data class LoginResponse(
    val token: String,
    val id: Long,
    val fullName: String,
    val email: String,
    val tokenType: String
)