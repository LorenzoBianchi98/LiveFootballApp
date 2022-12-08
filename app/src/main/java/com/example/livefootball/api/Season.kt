package com.example.livefootball.api

data class Season(
    val currentMatchday: Int,
    val endDate: String,
    val id: Int,
    val startDate: String
)