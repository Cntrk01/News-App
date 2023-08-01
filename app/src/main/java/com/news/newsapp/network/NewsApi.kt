package com.news.newsapp.network

import com.news.newsapp.data.NewsResponse
import com.news.newsapp.activity.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countyCode:String="us",
        @Query("page")
        page:Int=1,
        @Query("apiKey")
        api:String=API_KEY
    ) : Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun searchNews(
        @Query("q")
        searchNews:String,
        @Query("page")
        page:Int=1,
        @Query("apiKey")
        api:String=API_KEY
    ) : Response<NewsResponse>
}