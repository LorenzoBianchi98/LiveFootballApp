package com.example.livefootball

import com.example.livefootball.api.FootballNewsApiJSON
import com.example.livefootball.api.MatchesApiJSon
import com.example.livefootball.api.NewsApiJSON
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface MatchesAPIRequest {

    @Headers("X-Auth-Token: 5fa76bb56dcd424c8f18fc458cb474f1")
    @GET
    suspend fun getFootballNews(@Url path: String, @Query("matchday") matchday: String) : MatchesApiJSon
}