package com.example.mancoin.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mancoin.data.CoinData

//for show new data on old data like search items and delete items
class CoinDiffCallback(
    private val oldList: List<CoinData>,
    private val newList: List<CoinData>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].CoinInfo?.Id == newList[newItemPosition].CoinInfo?.Id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

}
