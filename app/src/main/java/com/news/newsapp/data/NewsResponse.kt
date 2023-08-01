package com.news.newsapp.data

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: MutableList<Article>
)