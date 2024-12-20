package com.example.mancoin.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mancoin.ApiManager.IMAGE_BASE_URL
import com.example.mancoin.ApiManager.TWITTER_BASE_URL
import com.example.mancoin.R
import com.example.mancoin.data.CoinData
import com.example.mancoin.data.coinAboutItem
import com.example.mancoin.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {
    lateinit var binding : ActivityCoinBinding
    lateinit var dataThisCoin : CoinData
    lateinit var dataThisCoinMap : coinAboutItem

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCoinBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val fromIntent = intent.getBundleExtra("bundle")!!
        if (fromIntent != null){
            dataThisCoin = fromIntent.getParcelable<CoinData>("bundle1")!!
            dataThisCoinMap = fromIntent.getParcelable<coinAboutItem>("bundle2")!!
        }else{
            Log.v("msgIntent" , "data are null")
        }

        binding.moduleToolbarAcCoin.txtToolbar.text = dataThisCoin.CoinInfo?.Name
        Glide
            .with(binding.root)
            .load(IMAGE_BASE_URL + dataThisCoin.DISPLAY?.USD?.iMAGEURL)
            .into(binding.moduleToolbarAcCoin.imgLogoMdToolbar)

        initUI()

    }

    private fun initUI() {
        initStateUI()
        initAbutUI()
       // initChartUI()
    }

    private fun initAbutUI() {
        if (dataThisCoin != null){
            binding.moduleAboutAcCoin.txtWebsiteUrlMdAbout.text = dataThisCoinMap.website
            binding.moduleAboutAcCoin.txtGithubUrlMdAbout.text = dataThisCoinMap.github
            binding.moduleAboutAcCoin.txtRedditUrlMdAbout.text = dataThisCoinMap.reddit
            binding.moduleAboutAcCoin.txtTwitterUrlMdAbout.text ="@" +  dataThisCoinMap.twitter
            binding.moduleAboutAcCoin.txtSomeMoreMdChart.text = dataThisCoinMap.desc

            binding.moduleAboutAcCoin.txtWebsiteUrlMdAbout.setOnClickListener {
                openWebClicked(dataThisCoinMap.website!!)
            }

            binding.moduleAboutAcCoin.txtGithubUrlMdAbout.setOnClickListener {
                openWebClicked(dataThisCoinMap.github!!)
            }

            binding.moduleAboutAcCoin.txtRedditUrlMdAbout.setOnClickListener {
                openWebClicked(dataThisCoinMap.reddit!!)
            }

            binding.moduleAboutAcCoin.txtTwitterUrlMdAbout.setOnClickListener {
                openWebClicked(TWITTER_BASE_URL + dataThisCoinMap.twitter!!)
            }

        }else{
            Log.v("msgErrorInitAbout" , "data are null")
        }

    }
    private fun openWebClicked(url : String){
        val intent = Intent(Intent.ACTION_VIEW , Uri.parse(url))
        startActivity(intent)
    }


    private fun initStateUI() {
        binding.moduleStatsAcCoin.txtPriceMdStats.text = dataThisCoin.DISPLAY?.USD?.pRICE
        binding.moduleStatsAcCoin.txtLowdayMdStats.text = dataThisCoin.DISPLAY?.USD?.lOWDAY
        binding.moduleStatsAcCoin.txtHighdayMdStats.text = dataThisCoin.DISPLAY?.USD?.hIGHDAY
        binding.moduleStatsAcCoin.txtMarketcapMdStats.text = dataThisCoin.DISPLAY?.USD?.mKTCAP
        val change = dataThisCoin.RAW?.USD?.CHANGE24HOUR
        if (change != null) {
            if (change > 0 ){
                binding.moduleStatsAcCoin.txtChange24hMdStats.setTextColor(
                    ContextCompat.getColor(
                        this ,
                        R.color.greenColor)
                )
                binding.moduleStatsAcCoin.txtChange24hMdStats.text = dataThisCoin.DISPLAY?.USD?.cHANGE24HOUR
            }else if (change < 0){
                binding.moduleStatsAcCoin.txtChange24hMdStats.setTextColor(
                    ContextCompat.getColor(
                        this ,
                        R.color.redColor)
                )
                binding.moduleStatsAcCoin.txtChange24hMdStats.text = dataThisCoin.DISPLAY?.USD?.cHANGE24HOUR
            }
        }

        binding.moduleStatsAcCoin.txtCirculatingSupplyMdStats.text = dataThisCoin.DISPLAY?.USD?.cIRCULATINGSUPPLY
        binding.moduleStatsAcCoin.txtAlgorithmMdStats.text = dataThisCoin.CoinInfo?.Algorithm
    }
}