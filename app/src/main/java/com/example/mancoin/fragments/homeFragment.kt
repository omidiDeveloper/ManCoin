package com.example.mancoin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mancoin.databinding.ActivityMarketBinding
import com.example.mancoin.databinding.FragmentHomeBinding

class fragmentHome : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}
