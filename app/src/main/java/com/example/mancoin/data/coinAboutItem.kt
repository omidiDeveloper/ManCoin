package com.example.mancoin.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class coinAboutItem(
    val website : String? = "No-Data",
    val twitter : String? = "No-Data",
    val reddit : String? = "No-Data",
    val github : String? = "No-Data",
    val desc : String? = "No-Data"
) : Parcelable
