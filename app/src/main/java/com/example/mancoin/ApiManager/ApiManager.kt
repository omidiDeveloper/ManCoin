package com.example.mancoin.ApiManager

import android.util.Log
import com.example.mancoin.data.ApiResponse
import com.example.mancoin.data.ChartData
import com.example.mancoin.data.CoinData
import com.example.mancoin.data.NewsItem
import com.example.mancoin.data.NewsResponse
import com.example.mancoin.data.TopCoins
import com.example.mancoin.data.getNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager(apiService: ApiService) {
    private val apiService: ApiService

    init {
        val retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        this.apiService = retrofit.create(ApiService::class.java)
    }

    fun getNews(apiCallBack: ApiCallBack<ArrayList<Pair<String, String>>>) {
        apiService.getTopNews().enqueue(object : Callback<getNews> {
            override fun onResponse(call: Call<getNews>, response: Response<getNews>) {
                val data = response.body()
                if (data == null) {
                    apiCallBack.onError("Response body is null")
                    return
                }

                val dataToSend: ArrayList<Pair<String, String>> = arrayListOf()

                data.data?.forEach {
                    val title = it?.title ?: "No Title"
                    val url = it?.url ?: "No URL"
                    dataToSend.add(Pair(title, url))
                }

                apiCallBack.onSuccess(dataToSend)
            }

            override fun onFailure(call: Call<getNews>, t: Throwable) {
                val errorMessage = t.message ?: "Unknown error occurred"
                apiCallBack.onError(errorMessage)
            }
        })
    }


    fun getCoinsData(apiCallback: ApiCallBack<List<CoinData>>) {
        apiService.getTopCoins().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.Data
                    apiCallback.onSuccess(data)
                } else {
                    apiCallback.onError("Response error or body is null")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                apiCallback.onError(t.message ?: "Unknown error occurred")
                Log.v("msgErrorOf" , t.message!!)
            }
        })
    }

    fun getExploreCoinsData(apiCallback: ApiCallBack<List<CoinData>>) {

        apiService.getTopCoinsExplore().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.Data
                    apiCallback.onSuccess(data)
                } else {
                    apiCallback.onError("Response error or body is null")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                apiCallback.onError(t.message ?: "Unknown error occurred")
                Log.v("msgErrorOf" , t.message!!)
            }
        })
    }

    fun getLIstNews(apiCallback: ApiCallBack<List<NewsItem>>){
        apiService.newsList().enqueue(object : Callback<NewsResponse>{
            override fun onResponse(p0: Call<NewsResponse>, p1: Response<NewsResponse>) {
                if (p1.isSuccessful && p1.body() != null) {
                    val data = p1.body()!!.Data
                    if (data != null) {
                        apiCallback.onSuccess(data)
                    }
                } else {
                    apiCallback.onError("Response error or body is null")
                }
            }

            override fun onFailure(p0: Call<NewsResponse>, p1: Throwable) {
                Log.v("msgErrorListNews" , p1.message!!)
            }

        })
    }

    fun getChartData(
        symbol : String , period : String ,
        apiCallBack: ApiCallBack<Pair<List<ChartData.Data> , ChartData.Data?>>){

        var histoPeriod = ""
        var limit = 30
        var aggregate = 1

        when(period){

            HOUR -> {
                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 1
            }

            HOURS24 -> {
                histoPeriod = HISTO_HOUR
                limit = 24
            }

            WEEK -> {
                histoPeriod = HISTO_HOUR
                aggregate = 6
            }

            MONTH -> {
                histoPeriod = HISTO_DAY
                limit = 30
            }

            MONTH3 -> {
                histoPeriod = HISTO_DAY
                limit = 90
            }

            YEAR -> {
                histoPeriod = HISTO_DAY
                aggregate = 13
            }

            ALL -> {
                histoPeriod = HISTO_DAY
                aggregate = 30
                limit = 2000
            }

        }

        apiService.getChartData(histoPeriod , symbol , limit , aggregate).enqueue(object  : Callback<ChartData>{
            override fun onResponse(p0: Call<ChartData>, p1: Response<ChartData>) {
                val fullData = p1.body()!!
                val data1 = fullData.data
                val data2 = fullData.data.maxByOrNull { it.close.toFloat() }
                val returnData = Pair(data1 , data2)
                apiCallBack.onSuccess(returnData)
            }

            override fun onFailure(p0: Call<ChartData>, p1: Throwable) {
                apiCallBack.onError(p1.message!!)
            }

        })

    }

    interface ApiCallBack<T> {
        fun onSuccess(data: T)
        fun onError(errorMessage: String)
    }

}
