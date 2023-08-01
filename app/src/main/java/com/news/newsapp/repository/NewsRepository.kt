package com.news.newsapp.repository

import com.news.newsapp.data.NewsResponse
import com.news.newsapp.network.NewsApi
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val api: NewsApi) {
    //return etmeden yapmaya çalıştım.Viewmodelde hata verdi.Return edince düzeldi çünkü fonksiyon bu değer döndürmüyor
    //Ama return edince ve Response değer ekleyince çalıştı
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int) : Response<NewsResponse> {
      return  api.getBreakingNews(countryCode,pageNumber)
    }
}