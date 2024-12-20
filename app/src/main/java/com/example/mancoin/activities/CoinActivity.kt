package com.example.mancoin.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.mancoin.ApiManager.ALL
import com.example.mancoin.ApiManager.ApiManager
import com.example.mancoin.ApiManager.HOUR
import com.example.mancoin.ApiManager.HOURS24
import com.example.mancoin.ApiManager.IMAGE_BASE_URL
import com.example.mancoin.ApiManager.MONTH
import com.example.mancoin.ApiManager.MONTH3
import com.example.mancoin.ApiManager.RetrofitClient
import com.example.mancoin.ApiManager.TWITTER_BASE_URL
import com.example.mancoin.ApiManager.WEEK
import com.example.mancoin.ApiManager.YEAR
import com.example.mancoin.R
import com.example.mancoin.adapter.ChartAdapter
import com.example.mancoin.data.ChartData
import com.example.mancoin.data.CoinData
import com.example.mancoin.data.coinAboutItem
import com.example.mancoin.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {
    lateinit var binding : ActivityCoinBinding
    lateinit var dataThisCoin : CoinData
    lateinit var dataThisCoinMap : coinAboutItem
    lateinit var apiManager : ApiManager

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

        // Initialize Retrofit and APIManager
        val apiService = RetrofitClient.create()
        apiManager = ApiManager(apiService)

        setSupportActionBar(binding.moduleToolbarAcCoin.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val btnClose =  binding.moduleToolbarAcCoin.imgCloseMdToolbar
        btnClose.isVisible = true
        btnClose.setOnClickListener {
            finish()
        }

        //get data from intent =>
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
        initChartUI()
    }

    private fun initChartUI() {

        //here we set a default value for period and one time that user don't clicked on radio show as default
        var period = HOUR
        requestDataAndShow(period)

        //when user clicked on a radio button change the period value =>
        binding.moduleChartAcCoin.radioGroupMdChart.setOnCheckedChangeListener { _, checkedId ->

            when(checkedId){
                R.id.radio_12h_md_chart -> {
                    period = HOUR
                }
                R.id.radio_1day_md_chart -> {
                    period = HOURS24
                }
                R.id.radio_1week_md_chart -> {
                    period = WEEK
                }
                R.id.radio_1month_md_chart -> {
                    period = MONTH
                }
                R.id.radio_3month_md_chart -> {
                    period = MONTH3
                }
                R.id.radio_1year_md_chart -> {
                    period = YEAR
                }
                R.id.radio_all_md_chart -> {
                    period = ALL
                }

            }
            requestDataAndShow(period)

        }

        binding.moduleChartAcCoin.txtPriceMdChart.text = dataThisCoin.DISPLAY?.USD?.pRICE
        binding.moduleChartAcCoin.txtChangePriceMdChart.text = " " + dataThisCoin.DISPLAY?.USD?.cHANGE24HOUR

        val changeData = dataThisCoin.RAW?.USD?.CHANGE24HOUR!!
        if (changeData > 0){

            binding.moduleChartAcCoin.txtPercentChangeMdChart.setTextColor(
                ContextCompat.getColor(this , R.color.greenColor)
            )
            binding.moduleChartAcCoin.txtPercentChangeMdChart.text = dataThisCoin.DISPLAY?.USD?.cHANGE24HOUR
            binding.moduleChartAcCoin.txtPlusOrDownMdChart.setTextColor(
                ContextCompat.getColor(this , R.color.greenColor)
            )
            binding.moduleChartAcCoin.txtPlusOrDownMdChart.text = "▲"
            binding.moduleChartAcCoin.chartCoinMdChart.lineColor = ContextCompat.getColor(this, R.color.greenColor)

        }else if (changeData < 0){

            binding.moduleChartAcCoin.txtPercentChangeMdChart.setTextColor(
                ContextCompat.getColor(this , R.color.redColor)
            )
            binding.moduleChartAcCoin.txtPercentChangeMdChart.text = dataThisCoin.DISPLAY?.USD?.cHANGE24HOUR
            binding.moduleChartAcCoin.txtPlusOrDownMdChart.setTextColor(
                ContextCompat.getColor(this , R.color.redColor)
            )
            binding.moduleChartAcCoin.txtPlusOrDownMdChart.text = "▼"
            binding.moduleChartAcCoin.chartCoinMdChart.lineColor = ContextCompat.getColor(this, R.color.redColor)
        }


        binding.moduleChartAcCoin.chartCoinMdChart.setScrubListener {
            if ( it == null ){
                binding.moduleChartAcCoin.txtPriceMdChart.text = dataThisCoin.DISPLAY?.USD?.pRICE
            }else{
                binding.moduleChartAcCoin.txtPriceMdChart.text = "$" + (it as ChartData.Data).close.toString()
            }
        }

    }

    fun requestDataAndShow(period : String){
        apiManager.getChartData(
            dataThisCoin.CoinInfo?.Name.toString(),
            period,
            object  : ApiManager.ApiCallBack<Pair<List<ChartData.Data> , ChartData.Data?>>{
                override fun onSuccess(data: Pair<List<ChartData.Data>, ChartData.Data?>) {
                    val chartAdapter = ChartAdapter(data.first , data.second?.open.toString())
                    binding.moduleChartAcCoin.chartCoinMdChart.adapter = chartAdapter
                }

                override fun onError(errorMessage: String) {
                    Log.v("logErrorChart" , errorMessage)
                }

            })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}