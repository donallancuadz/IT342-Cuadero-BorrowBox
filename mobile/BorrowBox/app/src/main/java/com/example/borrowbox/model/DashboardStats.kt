package com.example.borrowbox.model

data class RecentRequestItem(
    val itemName: String,
    val requestDate: String,
    val status: String
)

data class DashboardStats(
    val activeBorrows: Long,
    val pendingRequests: Long,
    val returnedItems: Long,
    val availableItems: Long,
    val recentRequests: List<RecentRequestItem>
)