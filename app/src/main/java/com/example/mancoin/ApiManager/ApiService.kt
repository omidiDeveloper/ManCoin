package com.example.mancoin.ApiManager

import com.example.mancoin.ApiManager.model.TopCoins
import com.example.mancoin.ApiManager.model.getNews
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(
        @Query("sortOrderBy") sortOrderNoews : String = "popular"
    ) : Call<getNews>

    @Headers(API_KEY)
    @GET("top/totalvolfull/")
    fun getTopCoins(
        @Query("limit") limit_coins : Int = 5 ,
        @Query("tsym") toSymbol : String = "USD"
    ) : Call<TopCoins>

}