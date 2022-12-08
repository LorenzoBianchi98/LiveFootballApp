package com.example.livefootball.api

data class LeaderboardApiJSON(
    val area: AreaX,
    val competition: CompetitionX,
    val filters: FiltersX,
    val season: SeasonX,
    val standings: List<Standing>
)