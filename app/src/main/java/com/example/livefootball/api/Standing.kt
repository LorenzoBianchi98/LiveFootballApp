package com.example.livefootball.api

data class Standing(
    val group: Any,
    val stage: String,
    val table: List<Table>,
    val type: String
)