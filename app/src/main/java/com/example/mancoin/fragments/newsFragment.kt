package com.example.mancoin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mancoin.ApiManager.ApiManager
import com.example.mancoin.ApiManager.ApiService
import com.example.mancoin.databinding.ActivityMarketBinding
import com.example.mancoin.databinding.FragmentExploreBinding
import com.example.mancoin.databinding.FragmentHomeBinding
import com.example.mancoin.databinding.FragmentNewsBinding

class newsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


}
