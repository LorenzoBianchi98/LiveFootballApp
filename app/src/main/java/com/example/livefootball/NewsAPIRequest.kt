package com.example.livefootball

import com.example.livefootball.api.NewsApiJSON
import retrofit2.http.GET

interface NewsAPIRequest {

    @GET("/v1/search?category=sports&keywords=calcio&language=it&apiKey=hKqLzG7KRKGtSzz6q0oC6cjKp0ne4xWmUanFu843PXWmrgMq")
    suspend fun getNews() : NewsApiJSON
}