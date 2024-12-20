package com.example.mancoin.itemEvent

import com.example.mancoin.data.CoinData
import com.example.mancoin.data.NewsItem
import com.example.mancoin.data.NewsResponse

interface itemEvent2 {
    fun onItemClicked(dataSend : CoinData){

    }
}