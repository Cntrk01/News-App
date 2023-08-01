package com.news.newsapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.RawValue

@Entity(tableName = "articles")
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String? ,
    val description: String? ,
    val publishedAt: String? ,
    val source: @RawValue Source , // Provide a default Source object
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Parcelable{

    // Bu fonksiyon boş olan Article öğelerini kontrol eder.
    // Tüm gerekli özellikler doluysa true, aksi takdirde false döndürür.
    fun isValid(): Boolean {
        return !(author.isNullOrEmpty() ||
                content.isNullOrEmpty() ||
                description.isNullOrEmpty() ||
                publishedAt.isNullOrEmpty() ||
                source.name.isNullOrEmpty() || title.isNullOrEmpty()
                || url.isNullOrEmpty() || urlToImage.isNullOrEmpty())
    }
}
