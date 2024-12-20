package com.example.mancoin.data


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

class CoinAboutAllData : ArrayList<CoinAboutAllData.CoinAboutAllDataItem>(){
    @Parcelize
    data class CoinAboutAllDataItem(
        @SerializedName("currencyName")
        val currencyName: String,
        @SerializedName("info")
        val info: Info
    ) : Parcelable {
        @Parcelize
        data class Info(
            @SerializedName("desc")
            val desc: String,
            @SerializedName("forum")
            val forum: String,
            @SerializedName("github")
            val github: String,
            @SerializedName("reddit")
            val reddit: String,
            @SerializedName("twt")
            val twt: String,
            @SerializedName("web")
            val web: String
        ) : Parcelable
    }
}