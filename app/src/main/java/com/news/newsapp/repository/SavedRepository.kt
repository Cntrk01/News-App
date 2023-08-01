package com.news.newsapp.repository

import androidx.lifecycle.LiveData
import com.news.newsapp.data.Article
import com.news.newsapp.db.ArticleDao
import com.news.newsapp.network.NewsApi
import javax.inject.Inject

class SavedRepository @Inject constructor(private val dao: ArticleDao) {
    suspend fun insertArticle(article: Article) = dao.insertArticle(article)
    suspend fun deleteArticle(article: Article) = dao.deleteArticle(article)
    fun getArticle() : LiveData<List<Article>> = dao.getArticles()
    suspend fun getArticleUrlControl(id:String)  = dao.getArticleUrlControl(id)
}