package com.example.mancoin.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mancoin.ApiManager.ApiManager
import com.example.mancoin.ApiManager.IMAGE_BASE_URL
import com.example.mancoin.ApiManager.RetrofitClient
import com.example.mancoin.R
import com.example.mancoin.activities.CoinActivity
import com.example.mancoin.activities.MarketActivity
import com.example.mancoin.adapter.CoinAdapter
import com.example.mancoin.data.CoinAboutAllData
import com.example.mancoin.data.CoinData
import com.example.mancoin.data.coinAboutItem
import com.example.mancoin.databinding.FragmentHomeBinding
import com.example.mancoin.itemEvent.itemEvent2
import com.google.gson.Gson

class homeFragment : Fragment() , itemEvent2{

    private lateinit var apiManager: ApiManager
    private lateinit var coinAdapter: CoinAdapter
    private lateinit var binding: FragmentHomeBinding
    lateinit var dataNews: ArrayList<Pair<String, String>>
    lateinit var dataAboutMap : MutableMap<String , coinAboutItem>
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Initialize Retrofit and APIManager
        val apiService = RetrofitClient.create()
        apiManager = ApiManager(apiService)

        //btn listener for change the fragments
        binding.moduleCoinsMdHome.btnEcploremoreMdHome.setOnClickListener {
            val activity = requireActivity() as MarketActivity
            //this is for change the navigation and go to other fragment with function that we made on Market Activity
            activity.navigateToFragment(exploreFragment(), R.id.bm_nav_explore)
        }

        progressBar = binding.progressBar

        setDataCoinABout()
        swipeRefresh()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        initUI()
    }
    //this function is for show all functions at once =>
    private fun initUI() {

        getDataFromApi()
        getNewsFromApi()
        getBTCandUSDFromAPi()
    }

    //for swipe refresh =>
    private fun swipeRefresh(){
        binding.swipeRefreshMdHome.setOnRefreshListener {
            initUI()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshMdHome.isRefreshing = false
            } , 2000)

        }

    }

    //this is for find btc and eth coins for set to watch list =>
    private fun getBTCandUSDFromAPi() {
        apiManager.getCoinsData(object : ApiManager.ApiCallBack<List<CoinData>> {


            override fun onSuccess(data: List<CoinData>) {
                // find Bitcoin and Ethereum info in our data from server
                val btcData = data.find { it.CoinInfo?.Name == "BTC" }
                val usdData = data.find { it.CoinInfo?.Name == "ETH" }

                // به‌روزرسانی UI برای بیت‌کوین و دلار
                btcData?.let { updateBTCModule(it) }
                usdData?.let { updateUSDModule(it) }
            }

            override fun onError(errorMessage: String) {
                context?.let {
                    Toast.makeText(it, "Error message", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //this function is for set data to Bitcoin watch list =>
    private fun updateBTCModule(btcData: CoinData) {
        binding.apply {
            Glide.with(binding.root)
                .load(IMAGE_BASE_URL + btcData.RAW?.USD?.IMAGEURL)
                .into(binding.moduleMainMdHome.imgCoinRecMdHome)

            binding.moduleMainMdHome.txtNameCoinMdHome.text = btcData.CoinInfo?.Name

            val priceValue = btcData.RAW?.USD?.PRICE
            val indexDot = priceValue.toString().indexOf('.')
            binding.moduleMainMdHome.txtPriceWatchlistMdHome.text =
                priceValue.toString().substring(0, indexDot + 3) + " $"


            val changeValue = btcData.RAW?.USD?.CHANGE24HOUR!!
            if (changeValue > 0) {
                binding.moduleMainMdHome.txtPercentRecMdHome.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.greenColor
                    )
                )
                binding.moduleMainMdHome.txtPercentRecMdHome.text =
                    changeValue.toString().take(4) + " $"
                binding.moduleMainMdHome.txtPlusOrDownRecHome.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.greenColor
                    )
                )
                binding.moduleMainMdHome.txtPlusOrDownRecHome.text = "+ "
            } else if (changeValue < 0) {
                binding.moduleMainMdHome.txtPercentRecMdHome.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.redColor
                    )
                )
                binding.moduleMainMdHome.txtPlusOrDownRecHome.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.redColor
                    )
                )
                binding.moduleMainMdHome.txtPercentRecMdHome.text =
                    changeValue.toString().take(5) + "$"
                binding.moduleMainMdHome.txtPlusOrDownRecHome.text = ""
            } else {
                binding.moduleMainMdHome.txtPercentRecMdHome.text = "0%"
                binding.moduleMainMdHome.txtPlusOrDownRecHome.text = ""
            }

            binding.moduleMainMdHome.txtPlusOrDownRecHome.setTextColor(
                if (btcData.RAW.USD.CHANGE24HOUR!! >= 0) Color.GREEN else Color.RED
            )
        }
    }

    //this function is for set data to ethereum watch list =>
    private fun updateUSDModule(ethData: CoinData) {
        binding.apply {
            Glide.with(binding.root)
                .load(IMAGE_BASE_URL + ethData.RAW?.USD?.IMAGEURL)
                .into(binding.moduleMainMdHome.imgUsdtWatchListMdHome)

            binding.moduleMainMdHome.txtNameUsdtMdHome.text = ethData.CoinInfo?.Name

            val priceValue = ethData.RAW?.USD?.PRICE
            val indexDot = priceValue.toString().indexOf('.')
            binding.moduleMainMdHome.txtPriceUsdtWatchlistMdHome.text =
                priceValue.toString().substring(0, indexDot + 3) + " $"


            val changeValue = ethData.RAW?.USD?.CHANGE24HOUR!!
            if (changeValue > 0) {
                binding.moduleMainMdHome.txtPercentUsdtWatchMdHome.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.greenColor
                    )
                )
                binding.moduleMainMdHome.txtPercentUsdtWatchMdHome.text =
                    changeValue.toString().take(4) + " $"
                binding.moduleMainMdHome.txtPlusOrDownUsdtHome.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.greenColor
                    )
                )
                binding.moduleMainMdHome.txtPlusOrDownUsdtHome.text = "+ "
            }
            else if (changeValue < 0) {
                binding.moduleMainMdHome.txtPercentUsdtWatchMdHome.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.redColor
                    )
                )
                binding.moduleMainMdHome.txtPlusOrDownUsdtHome.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.redColor
                    )
                )
                binding.moduleMainMdHome.txtPercentUsdtWatchMdHome.text =
                    changeValue.toString().take(5) + " $"
                binding.moduleMainMdHome.txtPlusOrDownUsdtHome.text = ""
            }
            else {
                binding.moduleMainMdHome.txtPercentUsdtWatchMdHome.text = "0 %"
                binding.moduleMainMdHome.txtPlusOrDownUsdtHome.text = ""
            }

        }
    }

    //this function is for get news data fro api =>
    private fun getNewsFromApi() {
        apiManager.getNews(object : ApiManager.ApiCallBack<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews = data
                refreshNews()
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(context, "error => " + errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    //this one is for refresh news data =>
    private fun refreshNews() {
        val randomAccess = (0..49).random()
        binding.moduleMainMdHome.txtNewsMdUpHome.text = dataNews[randomAccess].first
        binding.moduleMainMdHome.txtNewsMdUpHome.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataNews[randomAccess].second))
            startActivity(intent)
        }
        binding.moduleMainMdHome.imgRefreshMdUpHome.setOnClickListener {
            refreshNews()
        }
    }

    //this is for show the data from api to our recycler view =>
    private fun setupRecyclerView() {
        coinAdapter = CoinAdapter(emptyList() ,this)
        binding.moduleCoinsMdHome.recyclerMdHome.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    //this is for show all items when aren't empty =>
    private fun getDataFromApi() {
        progressBar.visibility = View.VISIBLE
        apiManager.getCoinsData(object : ApiManager.ApiCallBack<List<CoinData>> {
            override fun onSuccess(data: List<CoinData>) {
                data.forEach { coin ->
                    Log.d("API_DATA", "Coin: ${coin.CoinInfo?.FullName}, RAW: ${coin.RAW}")
                }

                coinAdapter.updateData(data)
                progressBar.visibility = View.GONE
            }

            override fun onError(errorMessage: String) {
                if (isAdded) { // Check if Fragment is attached
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Error fetching data", Toast.LENGTH_SHORT).show()
                    }
                }
                progressBar.visibility = View.GONE
            }
        })
    }


    private fun setDataCoinABout(){
        val fileInString = activity?.applicationContext?.assets
            ?.open("currencyinfo.json")
            ?.bufferedReader()
            ?.use { it.readText() }
        val gson = Gson()
        val dataAboutAll = gson.fromJson(fileInString , CoinAboutAllData::class.java)

        dataAboutMap = mutableMapOf<String , coinAboutItem>()

        dataAboutAll.forEach {
            dataAboutMap[it.currencyName] = coinAboutItem(
                it.info.web,
                it.info.twt,
                it.info.reddit,
                it.info.github,
                it.info.desc
            )
        }


    }

    override fun onItemClicked(dataSend: CoinData) {
        val intent = Intent(requireContext(), CoinActivity::class.java)
        val bundle = Bundle()

        val coinName = dataSend.CoinInfo?.Name
        if (coinName == null) {
            Log.e("IntentError", "CoinInfo.Name is null")
        } else if (!dataAboutMap.containsKey(coinName)) {
            Log.e("IntentError", "Key not found in dataAboutMap: $coinName")
        }

        bundle.putParcelable("bundle1", dataSend)
        bundle.putParcelable("bundle2", dataAboutMap[coinName] ?: coinAboutItem())
        Log.d("IntentDebug", "Bundle Content: ${bundle.toString()}")

        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }


}
