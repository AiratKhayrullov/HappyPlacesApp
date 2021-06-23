package com.airat.happyplacesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airat.happyplacesapp.databinding.ActivityAddHappyPlace2Binding

class AddHappyPlace : AppCompatActivity() {
    private lateinit var binding : ActivityAddHappyPlace2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlace2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true )
        binding.toolbarAddPlace.setNavigationOnClickListener {
            onBackPressed()
        }



    }
}