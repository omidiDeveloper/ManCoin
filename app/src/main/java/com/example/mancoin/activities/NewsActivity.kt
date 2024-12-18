package com.example.mancoin.activities

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.example.mancoin.R
import com.example.mancoin.data.NewsItem
import com.example.mancoin.databinding.ActivityNewsBinding

@Suppress("DEPRECATION")
class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var dataNews: NewsItem

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainNews)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbarMain.setTitleTextColor(ContextCompat.getColor(this, R.color.primary_dark))


        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Retrieve the NewsItem from the intent
        val newsItem = intent.getParcelableExtra<NewsItem>("sendNewsData")
        if (newsItem != null) {
            dataNews = newsItem // Assigning to dataNews
            binding.txtTitleNesAcNes.text = dataNews.title
            binding.txtDescriptionNewsAcNews.text = dataNews.body

            // Check if imageurl is not null or empty before loading
            if (!dataNews.imageurl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(dataNews.imageurl)
                    .into(binding.imgNewsAcNews)
            }else{
                Log.e("NewsActivity", "Image URL is null or empty")
            }

            binding.fabNewsOpenWebAcNews.setOnClickListener {
                val url = dataNews.url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

        } else {
            Log.e("NewsActivity", "NewsItem is null")
            finish() // End activity if null
        }
    }

    // Handle back button in action bar
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
