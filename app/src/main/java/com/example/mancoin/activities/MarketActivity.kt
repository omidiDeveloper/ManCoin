package com.example.mancoin.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mancoin.R
import com.example.mancoin.databinding.ActivityMarketBinding
import com.example.mancoin.fragments.exploreFragment
import com.example.mancoin.fragments.homeFragment
import com.example.mancoin.fragments.newsFragment

class MarketActivity : AppCompatActivity() {
    lateinit var binding : ActivityMarketBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMarketBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        replaceFragment(homeFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.btm_nav_home -> {
                    replaceFragment(homeFragment())
                }

                R.id.bm_nav_explore -> {
                    replaceFragment(exploreFragment())
                }

                R.id.bm_nav_news -> {
                    replaceFragment(newsFragment())
                }
            }
            binding.bottomNavigation.menu.getItem(1).isChecked = false
            true
        }
    }


    //for replace the fragments in your  bottom navigation with click use by this fun
    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main, fragment)
        transaction.commit()
    }
}