package com.guillaume.project9.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.guillaume.project9.R
import com.guillaume.project9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.mainToolbar)
        binding.mainToolbar.title = "@string/app_name"


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        //return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_bar_add_property -> startActivity(Intent(this, AddPropertyActivity::class.java))

            R.id.action_bar_map -> startActivity(Intent(this, MapsActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}