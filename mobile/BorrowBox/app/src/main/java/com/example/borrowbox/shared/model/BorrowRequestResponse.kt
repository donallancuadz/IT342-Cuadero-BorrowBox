package com.example.borrowbox.shared.model

data class BorrowRequestResponse(
    val id: Long,
    val itemName: String,
    val requestDate: String,
    val status: String
)