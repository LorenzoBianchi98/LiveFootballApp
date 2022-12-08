package com.example.livefootball.api

data class Booking(
    val card: String,
    val minute: Int,
    val player: Player,
    val team: Team
)