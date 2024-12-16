package com.example.mancoin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mancoin.ApiManager.IMAGE_BASE_URL
import com.example.mancoin.R
import com.example.mancoin.data.ApiResponse
import com.example.mancoin.data.CoinData
import com.example.mancoin.databinding.ItemRecyclerCoinViewBinding

class CoinAdapter(private var data: List<CoinData>) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {
    lateinit var binding: ItemRecyclerCoinViewBinding

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(coinData: CoinData) {

            //this condition is for when a data have problem to show don't call on recycler =>
            if (coinData.RAW != null && coinData.DISPLAY != null) {

                binding.txtNameCoinMdRec.text = coinData.CoinInfo.FullName
                binding.txtInternalCoinMdRec.text = coinData.CoinInfo.Internal

                //this is for when change value are more than 0 or it's on gain set green color also short than value and if it on loss change it to red

                val change_value = coinData.RAW.USD.CHANGE24HOUR
                if (change_value > 0) {
                    binding.txtPercentRecMdHome.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.greenColor
                        )
                    )
                    binding.txtPercentRecMdHome.text = change_value.toString().take(4) + " $"
                    binding.txtPlusOrDownRecHome.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.greenColor
                        )
                    )
                    binding.txtPlusOrDownRecHome.text = "+ "
                } else if (change_value < 0) {
                    binding.txtPercentRecMdHome.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.redColor
                        )
                    )
                    binding.txtPercentRecMdHome.text = change_value.toString().take(5) + " $"
                    binding.txtPlusOrDownRecHome.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.redColor
                        )
                    )
                    binding.txtPlusOrDownRecHome.text = " "
                } else {
                    binding.txtPercentRecMdHome.text = "0%"
                }

                //this is for take short than price value
                val priceValue = coinData.RAW.USD.PRICE
                val indexDot = priceValue.toString().indexOf('.')
                binding.txtPriceWatchlistMdHome.text =
                    priceValue.toString().substring(0, indexDot + 3) + " $"




                Glide
                    .with(binding.root)
                    .load(IMAGE_BASE_URL + coinData.RAW.USD.IMAGEURL)
                    .into(binding.imgCoinRecMdHome)
            } else{
                Log.d("BIND_VIEW_N", "Skipped item: ${coinData.CoinInfo.FullName}")
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        binding = ItemRecyclerCoinViewBinding.inflate(inflate, parent, false)
        return CoinViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bindView(data[position])
        Log.d("BIND_VIEW", "Binding data: ${data[position].CoinInfo.FullName}")
    }


    override fun getItemCount() = data.size

    //use by diff callback for have better response also for search is useful
    fun updateData(newData: List<CoinData>) {
        val diffCallback = CoinDiffCallback(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        data = newData
        diffResult.dispatchUpdatesTo(this)
    }




}
