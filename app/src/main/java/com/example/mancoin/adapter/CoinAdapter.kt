package com.example.mancoin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mancoin.ApiManager.IMAGE_BASE_URL
import com.example.mancoin.data.TopCoins
import com.example.mancoin.databinding.ItemRecyclerCoinViewBinding

class CoinAdapter(private val data : ArrayList<TopCoins.Data>,
                  private val recyclereCallBack : RecyclerCallBack
) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>(){
    lateinit var binding : ItemRecyclerCoinViewBinding



    inner class CoinViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bindViews(dataCoin: TopCoins.Data){

            binding.txtNameCoinMdRec.text = dataCoin.coinInfo.name
            binding.txtInternalCoinMdRec.text = dataCoin.coinInfo.internal
            binding.txtPriceWatchlistMdHome.text = dataCoin.rAW.uSD.pRICE.toString()
            binding.txtPercentRecMdHome.text = dataCoin.rAW.uSD.cHANGE24HOUR.toString()

            Glide
                .with(binding.root)
                .load(IMAGE_BASE_URL + dataCoin.coinInfo.imageUrl)
                .transform(RoundedCorners(32))
                .into(binding.imgCoinRecMdHome)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        binding = ItemRecyclerCoinViewBinding.inflate(inflate , parent , false)
        return CoinViewHolder(binding.root)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    interface RecyclerCallBack{
        fun onCoinItemClicked(dataCoin: TopCoins.Data)
    }

}