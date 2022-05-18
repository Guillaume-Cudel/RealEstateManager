package com.guillaume.project9.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.guillaume.project9.R
import com.guillaume.project9.databinding.FragmentPropertyDetailBinding
import com.guillaume.project9.databinding.FragmentPropertyListBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import com.guillaume.project9.viewmodel.PropertyViewModel


class PropertyDetailFragment : Fragment() {

    private lateinit var binding: FragmentPropertyDetailBinding
    private var property: Property? = null
    private var photoList: List<Photo?> = listOf()
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((activity?.application as PropertysApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPropertyDetailBinding.inflate(inflater, container, false)

        property = arguments?.getSerializable("property") as Property?
        propertyVM.getPhotosByProperty(property!!.propertyId).observe(requireActivity(), Observer {
            photoList = it
        })

        return binding.root
    }


}