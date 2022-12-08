package com.example.livefootball.api

data class Matche(
    val area: AreaXX,
    val attendance: Any,
    val awayTeam: AwayTeamX,
    val bookings: List<Any>,
    val competition: CompetitionXXX,
    val goals: List<GoalX>,
    val group: Any,
    val homeTeam: HomeTeamX,
    val id: Int,
    val injuryTime: Int,
    val lastUpdated: String,
    val matchday: Int,
    val minute: String,
    val odds: Odds,
    val penalties: List<Penalty>,
    val referees: List<RefereeX>,
    val score: ScoreXXX,
    val season: SeasonXX,
    val stage: String,
    val status: String,
    val substitutions: List<Any>,
    val utcDate: String,
    val venue: String
)