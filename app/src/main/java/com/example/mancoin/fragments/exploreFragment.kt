package com.example.mancoin.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mancoin.ApiManager.ApiManager
import com.example.mancoin.ApiManager.RetrofitClient
import com.example.mancoin.activities.CoinActivity
import com.example.mancoin.adapter.CoinAdapter
import com.example.mancoin.data.ApiResponse
import com.example.mancoin.data.CoinAboutAllData
import com.example.mancoin.data.CoinData
import com.example.mancoin.data.NewsItem
import com.example.mancoin.data.coinAboutItem
import com.example.mancoin.databinding.FragmentExploreBinding
import com.example.mancoin.itemEvent.itemEvent
import com.example.mancoin.itemEvent.itemEvent2
import com.google.gson.Gson

@Suppress("UNCHECKED_CAST")
class exploreFragment : Fragment() , itemEvent2 {
    lateinit var binding: FragmentExploreBinding
    lateinit var apiManager: ApiManager
    lateinit var coinAdapter: CoinAdapter
    private var coinMutedListSearch: MutableList<CoinData> = mutableListOf()
    lateinit var dataAboutMap : MutableMap<String , coinAboutItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        Log.v("msgShowFragment", "is running")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Initialize Retrofit and APIManager
        val apiService = RetrofitClient.create()
        apiManager = ApiManager(apiService)

        setDataCoinABout()
        swipeRefreshLayout()
        searchItemList()
    }

    override fun onResume() {
        super.onResume()
        initUI()
    }

    private fun initUI() {
        getCoinExploreDataFromAPi()
        setupRecyclerView()

    }

    //for refresh all data
    private fun swipeRefreshLayout() {

        binding.swipeRefreshMdExplore.setOnRefreshListener {

            initUI()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshMdExplore.isRefreshing = false
            }, 2000)
        }

    }

    //for show data on recycler and show to user
    private fun setupRecyclerView() {
        coinAdapter = CoinAdapter(emptyList() , this)
        binding.recyclerMdExplore.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    //for get data from api
    private fun getCoinExploreDataFromAPi() {

        apiManager.getExploreCoinsData(object : ApiManager.ApiCallBack<List<CoinData>> {
            override fun onSuccess(data: List<CoinData>) {
                val validData = data.filter { it.RAW != null && it.DISPLAY != null }
                coinMutedListSearch = validData.toMutableList()
                coinAdapter.updateData(validData)

            }

            override fun onError(errorMessage: String) {
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                Log.v("msgErrorGetCoinsExplore", errorMessage)
            }
        })

    }

    //this is one is for search on items
    private fun searchItemList() {
        binding.searchBoxMdExplore.addTextChangedListener { text: Editable? ->
            val query = text.toString().lowercase()
            if (query.isEmpty()) {
                coinAdapter.updateData(coinMutedListSearch)
                return@addTextChangedListener
            }
            val filteredData = coinMutedListSearch.filter { coinData ->
                val fullName = coinData.CoinInfo?.FullName?.lowercase() ?: ""
                val name = coinData.CoinInfo?.Name?.lowercase() ?: ""
                val id = coinData.CoinInfo?.Id?.lowercase() ?: ""
                fullName.contains(query) || name.contains(query) || id.contains(query)
            }
            coinAdapter.updateData(filteredData)


        }
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