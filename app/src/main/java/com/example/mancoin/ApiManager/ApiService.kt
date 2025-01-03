package com.example.mancoin.ApiManager

import com.example.mancoin.data.ApiResponse
import com.example.mancoin.data.ChartData
import com.example.mancoin.data.NewsResponse
import com.example.mancoin.data.getNews
import org.intellij.lang.annotations.RegExp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
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
        @Query("limit") limit: Int = 100
    ): Call<ApiResponse>

    @Headers(API_KEY)
    @GET("v2/news/")
    fun newsList(
        @Query("lang") lang : String = "EN"
    ): Call<NewsResponse>

    @Headers(API_KEY)
    @GET("{period}")
    fun getChartData(
        @Path("period") period : String,
        @Query("fsym") fromSymbol : String,
        @Query("limit") limit : Int,
        @Query("aggregate") aggregate : Int,
        @Query("tsym") toSymbol : String = "USD"
    ) : Call<ChartData>
}