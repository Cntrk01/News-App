package com.news.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.newsapp.data.NewsResponse
import com.news.newsapp.repository.NewsRepository
import com.news.newsapp.ui.fragments.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel(){
    val breakingNews:MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var breakingPage=1
    var breakingNewsResponse : NewsResponse ?=null

    init {
        getBreakingNews("us")
    }


     fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        val response=newsRepository.getBreakingNews(countryCode,breakingPage)
        breakingNews.postValue(handleBreakNewResponse(response))

    }

    private fun handleBreakNewResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                breakingPage++
                if (breakingNewsResponse ==null){
                    breakingNewsResponse=it
                }else{
                    val oldArticle=breakingNewsResponse?.articles
                    val newArticle=it.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resources.Success(breakingNewsResponse ?: it)
            }
        }
        return Resources.Error(response.message())
    }
}