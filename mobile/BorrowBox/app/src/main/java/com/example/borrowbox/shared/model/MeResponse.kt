package com.example.borrowbox.shared.model

data class MeResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val role: String? = null
)