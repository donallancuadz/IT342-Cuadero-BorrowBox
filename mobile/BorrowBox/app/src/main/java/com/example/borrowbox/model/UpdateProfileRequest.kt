package com.example.borrowbox.model

data class UpdateProfileRequest(
    val fullName: String?,
    val currentPassword: String?,
    val newPassword: String?
)