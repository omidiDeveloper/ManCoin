package com.example.mancoin.ApiManager

import android.util.Log
import com.example.mancoin.data.ApiResponse
import com.example.mancoin.data.CoinData
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

    interface ApiCallBack<T> {
        fun onSuccess(data: T)
        fun onError(errorMessage: String)
    }

}
