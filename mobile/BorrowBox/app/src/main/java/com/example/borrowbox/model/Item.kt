package com.example.borrowbox.model

data class Item(
    val id: Long,
    val name: String,
    val description: String,
    val available: Boolean
)