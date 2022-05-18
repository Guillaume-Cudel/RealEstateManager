package com.guillaume.project9.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guillaume.project9.R
import com.guillaume.project9.databinding.FragmentPropertyDetailBinding
import com.guillaume.project9.databinding.FragmentPropertyListBinding
import com.guillaume.project9.model.Property


class PropertyDetailFragment : Fragment() {

    private lateinit var binding: FragmentPropertyDetailBinding
    private var property: Property? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPropertyDetailBinding.inflate(inflater, container, false)


        property = arguments?.getSerializable("property") as Property?

        return binding.root
    }


}