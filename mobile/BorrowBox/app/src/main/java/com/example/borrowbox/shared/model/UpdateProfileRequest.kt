package com.example.borrowbox.shared.model

data class UpdateProfileRequest(
    val fullName: String?,
    val currentPassword: String?,
    val newPassword: String?
)