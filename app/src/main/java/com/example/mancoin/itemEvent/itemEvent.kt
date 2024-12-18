package com.example.mancoin.itemEvent

import com.example.mancoin.data.NewsItem
import com.example.mancoin.data.NewsResponse

interface itemEvent {
    fun onItemClicked(dataSend : NewsItem){

    }
}