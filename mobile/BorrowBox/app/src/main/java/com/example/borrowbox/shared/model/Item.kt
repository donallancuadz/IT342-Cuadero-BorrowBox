package com.example.borrowbox.shared.model

data class Item(
    val id: Long,
    val name: String,
    val description: String,
    val available: Boolean
)