package com.news.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.news.newsapp.data.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article) : Long

    @Query("SELECT * FROM articles")
    fun getArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    //count int döndürür ben fonksiyon dönüşünü string yapmıştım yanlış olmuş !!
    @Query("SELECT COUNT(*) FROM articles WHERE articles.url = :url")
    suspend fun getArticleUrlControl(url: String): Int
}