package com.example.mancoin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mancoin.ApiManager.IMAGE_BASE_URL
import com.example.mancoin.data.CoinData
import com.example.mancoin.databinding.ItemRecyclerCoinViewBinding

class CoinAdapter(private var data: List<CoinData>) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {
        lateinit var binding: ItemRecyclerCoinViewBinding

    inner class CoinViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindView(coinData: CoinData ){

            //this condition is for when a data have problem to show don't call on recycler =>
            if (coinData.RAW != null && coinData.DISPLAY != null ){
                binding.txtNameCoinMdRec.text = coinData.CoinInfo.FullName
                binding.txtInternalCoinMdRec.text = coinData.CoinInfo.Internal
                binding.txtPriceWatchlistMdHome.text = coinData.RAW.USD.PRICE.toString()
                binding.txtPercentRecMdHome.text = coinData.RAW.USD.VOLUME24HOUR.toString()



                Glide
                    .with(binding.root)
                    .load(IMAGE_BASE_URL + coinData.RAW.USD.IMAGEURL)
                    .into(binding.imgCoinRecMdHome)
            }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
         val inflate = LayoutInflater.from(parent.context)
        binding = ItemRecyclerCoinViewBinding.inflate(inflate , parent , false)
        return CoinViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bindView(data[position])
    }


    override fun getItemCount() = data.size

    fun updateData(newData: List<CoinData>) {
        data = newData
        notifyDataSetChanged()
    }
}
