package com.example.mancoin.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiResponse(
    val Message: String?,
    val Data: List<CoinData>
) : Parcelable

@Parcelize
data class CoinData(
    val CoinInfo: CoinInfo?,
    val RAW: RawData ?,
    val DISPLAY : DISPLAY?
) : Parcelable

@Parcelize
data class CoinInfo(
    val Id: String?,
    val Name: String?,
    val FullName: String?,
    val Internal: String?,
    val Algorithm : String?
) : Parcelable

@Parcelize
data class RawData(
    val USD: USDData
) : Parcelable

@Parcelize
data class USDData(
    val PRICE: Double?,
    val CHANGE24HOUR: Double?,
    val IMAGEURL : String?
) : Parcelable

@Parcelize
data class DISPLAY(
    val USD : TopCoins.Data.DISPLAY.USD?
) : Parcelable