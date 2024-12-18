package com.example.mancoin.data

data class ApiResponse(
    val Message: String?,
    val Data: List<CoinData>
)

data class CoinData(
    val CoinInfo: CoinInfo?,
    val RAW: RawData ?,
    val DISPLAY : TopCoins.Data.DISPLAY.USD?
)

data class CoinInfo(
    val Id: String?,
    val Name: String?,
    val FullName: String?,
    val Internal: String?
)

data class RawData(
    val USD: USDData
)

data class USDData(
    val PRICE: Double?,
    val CHANGE24HOUR: Double?,
    val IMAGEURL : String?
)
