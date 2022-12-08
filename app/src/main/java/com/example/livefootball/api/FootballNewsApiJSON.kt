package com.example.livefootball.api

data class FootballNewsApiJSON(
    val competition: Competition,
    val count: Int,
    val filters: Filters,
    val matches: List<Match>
)