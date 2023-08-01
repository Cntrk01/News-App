package com.news.newsapp.repository

import com.news.newsapp.db.ArticleDao
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val dao: ArticleDao) {
   
}