package com.example.livefootball.api

data class HomeTeamX(
    val bench: List<Any>,
    val coach: CoachXXX,
    val crest: String,
    val formation: String,
    val id: Int,
    val leagueRank: Int,
    val lineup: List<Any>,
    val name: String,
    val shortName: String,
    val tla: String
)