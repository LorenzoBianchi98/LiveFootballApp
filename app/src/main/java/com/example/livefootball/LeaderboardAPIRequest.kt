package com.example.livefootball

import com.example.livefootball.api.LeaderboardApiJSON
import retrofit2.Call
import retrofit2.http.*

interface LeaderboardAPIRequest {


    @Headers("X-Auth-Token: 5fa76bb56dcd424c8f18fc458cb474f1")
    @GET
    suspend fun getLeaderboard(
        @Url path: String,
        ) : LeaderboardApiJSON
}