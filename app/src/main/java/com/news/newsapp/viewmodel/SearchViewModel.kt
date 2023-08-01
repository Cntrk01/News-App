package com.news.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.newsapp.data.NewsResponse
import com.news.newsapp.repository.SearchRepository
import com.news.newsapp.ui.fragments.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    val searchNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
     var searchNewsPage=1
    var searchNewsResponse : NewsResponse ?=null

    fun searchNews(searchResponse:String) = viewModelScope.launch {
        searchNews.postValue(Resources.Loading())
        val response=repository.searchNews(searchResponse,searchNewsPage)
        searchNews.postValue(handleSearchResponse(response))
    }


    private fun handleSearchResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                searchNewsPage++
                if (searchNewsResponse ==null){
                    searchNewsResponse=it
                }else{
                    val oldArticle=searchNewsResponse?.articles
                    val newArticle=it.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resources.Success(searchNewsResponse ?: it)
            }
        }
        return Resources.Error(response.message())
    }
}