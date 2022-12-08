package com.example.livefootball.api

data class MatchesApiJSon(
    val competition: CompetitionXX,
    val filters: FiltersXX,
    val matches: List<Matche>,
    val resultSet: ResultSet
)