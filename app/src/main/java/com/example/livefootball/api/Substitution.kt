package com.example.livefootball.api

data class Substitution(
    val minute: Int,
    val playerIn: PlayerIn,
    val playerOut: PlayerOut,
    val team: TeamXX
)