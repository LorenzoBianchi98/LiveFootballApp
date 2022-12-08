package com.example.livefootball.api

data class AwayTeamX(
    val bench: List<Any>,
    val coach: CoachXX,
    val crest: String,
    val formation: String,
    val id: Int,
    val leagueRank: Int,
    val lineup: List<Any>,
    val name: String,
    val shortName: String,
    val tla: String
)