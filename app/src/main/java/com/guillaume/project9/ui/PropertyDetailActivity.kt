package com.guillaume.project9.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.guillaume.project9.R
import com.guillaume.project9.databinding.ActivityPropertyDetailBinding
import com.guillaume.project9.model.Property
import com.guillaume.project9.viewmodel.UtilsViewModel

class PropertyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyDetailBinding
    private val utilsVM: UtilsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.detailToolbar)
        binding.detailToolbar.title = "@string/app_name"

        val bundle = intent.extras
        val property = bundle?.getSerializable("property") as Property
        utilsVM.saveProperty(property)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        //return super.onCreateOptionsMenu(menu)
        return true
    }

    /*@RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_bar_add_property -> startActivity(Intent(this, AddPropertyActivity::class.java))

        }
        return super.onOptionsItemSelected(item)
    }*/



}