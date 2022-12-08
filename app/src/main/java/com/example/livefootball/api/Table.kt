package com.example.livefootball.api

data class Table(
    val draw: Int,
    val form: String,
    val goalDifference: Int,
    val goalsAgainst: Int,
    val goalsFor: Int,
    val lost: Int,
    val playedGames: Int,
    val points: Int,
    val position: Int,
    val team: TeamXXX,
    val won: Int
)