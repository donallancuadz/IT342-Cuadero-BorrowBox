package com.example.borrowbox.model

data class BorrowRequestResponse(
    val id: Long,
    val itemName: String,
    val requestDate: String,
    val status: String
)

data class BorrowRequestCreateRequest(
    val itemId: Long
)