package com.example.livefootball.api

data class Goal(
    val assist: Any,
    val minute: Int,
    val scorer: Scorer,
    val team: TeamX,
    val type: Any
)