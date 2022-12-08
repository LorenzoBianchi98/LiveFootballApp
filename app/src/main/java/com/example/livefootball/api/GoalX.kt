package com.example.livefootball.api

data class GoalX(
    val assist: Any,
    val injuryTime: Any,
    val minute: Int,
    val score: ScoreX,
    val scorer: ScorerX,
    val team: TeamXXXX,
    val type: String
)