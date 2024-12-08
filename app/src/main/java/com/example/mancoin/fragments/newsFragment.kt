package com.example.mancoin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mancoin.databinding.ActivityMarketBinding
import com.example.mancoin.databinding.FragmentExploreBinding
import com.example.mancoin.databinding.FragmentHomeBinding

class fragmentExplore : Fragment() {
    lateinit var binding: FragmentExploreBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        binding = FragmentExploreBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}
