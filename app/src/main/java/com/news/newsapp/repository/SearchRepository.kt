package com.news.newsapp.repository

import com.news.newsapp.network.NewsApi
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: NewsApi) {
    suspend fun searchNews(searchNews:String,page:Int) = api.searchNews(searchNews, page)
}