package com.example.mancoin.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mancoin.ApiManager.ApiManager
import com.example.mancoin.ApiManager.RetrofitClient
import com.example.mancoin.activities.NewsActivity
import com.example.mancoin.adapter.NewsAdapter
import com.example.mancoin.data.NewsItem
import com.example.mancoin.data.NewsResponse
import com.example.mancoin.databinding.FragmentNewsBinding
import com.example.mancoin.itemEvent.itemEvent

class newsFragment : Fragment() , itemEvent {
    lateinit var binding: FragmentNewsBinding
    lateinit var apiManager: ApiManager
    lateinit var newsAdapter: NewsAdapter
    private var newsMutedListSearch : MutableList<NewsItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        Log.v("msgShowFragment", "is running")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Initialize Retrofit and APIManager
        val apiService = RetrofitClient.create()
        apiManager = ApiManager(apiService)

        initUI()
    }

    private fun initUI() {
        getNewsFromAPi()
        showDataInRecycler()
        refreshItems()
    }

    private fun refreshItems() {
        binding.swipeRefreshMdNews.setOnRefreshListener {
            initUI()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshMdNews.isRefreshing = false
            } , 2000)
        }
    }

    private fun getNewsFromAPi() {
        apiManager.getLIstNews(object : ApiManager.ApiCallBack<List<NewsItem>> {
            override fun onSuccess(data: List<NewsItem>) {
                val validData = data.filter { it.title != null && it.imageurl != null }
                newsMutedListSearch = validData.toMutableList()
                newsAdapter.updateData(validData) // Use validData instead of data
            }

            override fun onError(errorMessage: String) {
                Log.v("msgErrorNewsFragment", errorMessage)
            }
        })
    }

    private fun showDataInRecycler(){
        newsAdapter = NewsAdapter(emptyList() , this)
        binding.recyclerMdNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onItemClicked(dataSend : NewsItem) {
        val intent = Intent(requireContext(), NewsActivity::class.java)
        intent.putExtra("sendNewsData", dataSend)
        startActivity(intent)

    }
}
