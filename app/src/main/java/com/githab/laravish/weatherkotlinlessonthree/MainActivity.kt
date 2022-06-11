package com.githab.laravish.weatherkotlinlessonthree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.githab.laravish.weatherkotlinlessonthree.ui.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}