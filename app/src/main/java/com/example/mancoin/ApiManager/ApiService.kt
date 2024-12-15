package com.example.mancoin.ApiManager

import com.example.mancoin.data.ApiResponse
import com.example.mancoin.data.getNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import kotlin.math.max

interface ApiService {

    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(
        @Query("sortOrderBy") sortOrderNoews : String = "popular"
    ) : Call<getNews>

    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getTopCoins(
        @Query("tsym") toSymbol: String = "USD",
        @Query("limit") limit: Int = 5
    ): Call<ApiResponse>

    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getTopCoinsExplore(
        @Query("tsym") toSymbol: String = "USD",
        @Query("limit") limit: Int = 30
    ): Call<ApiResponse>


}