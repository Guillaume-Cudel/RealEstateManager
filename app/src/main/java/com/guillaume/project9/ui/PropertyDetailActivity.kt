package com.guillaume.project9.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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

        val bundle = intent.extras
        val property = bundle?.getSerializable("property") as Property
        utilsVM.saveProperty(property)


       /* val bundle = intent.extras
        property = bundle?.getSerializable("property") as Property
        propertyVM.setProperty(property!!)*/

    }



}