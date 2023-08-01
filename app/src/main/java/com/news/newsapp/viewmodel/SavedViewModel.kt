package com.news.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.newsapp.data.Article
import com.news.newsapp.repository.SavedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val savedRepository: SavedRepository)
    :ViewModel(){
    var urlObserve : MutableLiveData<String> = MutableLiveData()


    fun saveArticle(article: Article) = viewModelScope.launch{
        savedRepository.insertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch{
        savedRepository.deleteArticle(article)
    }
    fun getArticle() =savedRepository.getArticle()

    suspend fun isArticleExist(url: String) : Int = savedRepository.getArticleUrlControl(url)
}