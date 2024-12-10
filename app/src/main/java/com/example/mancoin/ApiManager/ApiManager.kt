package com.example.mancoin.ApiManager

import com.example.mancoin.ApiManager.model.getNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    private val apiService: ApiService

    init {
        val retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        apiService = retrofit.create(ApiService::class.java)
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

    interface ApiCallBack<T> {
        fun onSuccess(data: T)
        fun onError(errorMessage: String)
    }

}
