package com.example.mancoin.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mancoin.ApiManager.ApiManager
import com.example.mancoin.databinding.FragmentHomeBinding

class fragmentHome : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var apiManager: ApiManager
    private var dataNews: ArrayList<Pair<String, String>> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        apiManager = ApiManager() // Initialize API Manager
        initUI()
        return binding.root
    }

    private fun initUI() {
        getNewsFromApi()
    }

    private fun getNewsFromApi() {
        apiManager.getNews(object : ApiManager.ApiCallBack<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews = data
                refreshNews()
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(requireContext(), "Message => $errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun refreshNews() {
        if (dataNews.isNotEmpty()) {
            val randomAccess = (0 until dataNews.size).random()
            binding.moduleMainMdHome.txtNewsMdUpHome.text = dataNews[randomAccess].first
            binding.moduleMainMdHome.txtNewsMdUpHome.setOnClickListener {
                val url = dataNews[randomAccess].second
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } catch (e: Exception) {
                    Log.v("testApiMine" , e.message!!)
                    Toast.makeText(requireContext(), "Invalid URL", Toast.LENGTH_SHORT).show()
                }
            }
            binding.moduleMainMdHome.imgRefreshMdUpHome.setOnClickListener {
                refreshNews()
            }
        } else {
            Toast.makeText(requireContext(), "No news available", Toast.LENGTH_SHORT).show()
        }
    }
}
