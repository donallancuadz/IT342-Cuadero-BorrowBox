package com.example.borrowbox.model

data class MeResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val role: String? = null
)