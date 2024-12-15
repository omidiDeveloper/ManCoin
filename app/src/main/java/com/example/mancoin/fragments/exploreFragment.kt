package com.example.mancoin.fragments

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
import com.example.mancoin.adapter.CoinAdapter
import com.example.mancoin.data.ApiResponse
import com.example.mancoin.data.CoinData
import com.example.mancoin.databinding.FragmentExploreBinding

@Suppress("UNCHECKED_CAST")
class exploreFragment : Fragment() {
    lateinit var binding: FragmentExploreBinding
    lateinit var apiManager: ApiManager
    lateinit var coinAdapter: CoinAdapter

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
        initUI()
        swipeRefreshLayout()
    }

    private fun initUI() {
        getCoinExploreDataFromAPi()
        setupRecyclerView()

    }

    private fun swipeRefreshLayout() {

        binding.swipeRefreshMdExplore.setOnRefreshListener {

            initUI()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshMdExplore.isRefreshing = false
            }, 2000)
        }

    }

    private fun setupRecyclerView() {
        coinAdapter = CoinAdapter(emptyList())
        binding.recyclerMdExplore.apply {
            adapter = coinAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getCoinExploreDataFromAPi() {

        apiManager.getExploreCoinsData(object : ApiManager.ApiCallBack<List<CoinData>> {
            override fun onSuccess(data: List<CoinData>) {
                val uniqeCoinsList = mutableListOf<CoinData>()
                data.forEach { coin ->
                    if (uniqeCoinsList.none { it.CoinInfo.Id == coin.CoinInfo.Id }) {
                        uniqeCoinsList.add(coin)
                        Log.d(
                            "API_DATA_EXPLORE",
                            "Coin: ${coin.CoinInfo.FullName}, RAW: ${coin.RAW}"
                        )
                    }

                }

                coinAdapter.updateData(data)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                Log.v("msgErrorGetCoinsExplore", errorMessage)
            }
        })

    }

}