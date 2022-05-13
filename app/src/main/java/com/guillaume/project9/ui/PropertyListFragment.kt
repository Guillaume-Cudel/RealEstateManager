package com.guillaume.project9.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.project9.R
import com.guillaume.project9.databinding.FragmentPropertyListBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.viewmodel.PropertyViewModel


class PropertyListFragment : Fragment() {

    private lateinit var binding: FragmentPropertyListBinding
    private var recyclerView: RecyclerView? = null
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((activity?.application as PropertysApplication).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentPropertyListBinding.inflate(inflater, container, false)

        recyclerView = binding.propertysRecycleView
        val adapter = PropertyListAdapter()
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(activity)

        propertyVM.allPropertys.observe(requireActivity(), Observer { propertys ->
            propertys?.let{ adapter.submitList(it)}
        })

        return binding.root
    }

}