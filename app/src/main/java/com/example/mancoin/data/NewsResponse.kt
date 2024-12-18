package com.example.mancoin.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    val Data: List<NewsItem>?,
    val Type: Int?,
    val Message: String?,
    val HasWarning: Boolean?
) : Parcelable

@Parcelize
data class NewsItem(
    val id: String?,
    val guid: String?,
    val published_on: Long?,
    val imageurl: String?,
    val title: String?,
    val url: String?,
    val body: String?,
    val tags: String?,
    val lang: String?,
    val upvotes: String?,
    val downvotes: String?,
    val categories: String?,
    val source_info: SourceInfo?,
    val source: String?
) : Parcelable

@Parcelize
data class SourceInfo(
    val name: String?,
    val img: String?,
    val lang: String?
) : Parcelable
