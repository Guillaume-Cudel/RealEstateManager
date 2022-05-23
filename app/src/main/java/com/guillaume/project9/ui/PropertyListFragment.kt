package com.guillaume.project9.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.project9.R
import com.guillaume.project9.databinding.FragmentPropertyListBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import com.guillaume.project9.viewmodel.PropertyViewModel


class PropertyListFragment : Fragment(), Communicator {

    private lateinit var binding: FragmentPropertyListBinding
    private var recyclerView: RecyclerView? = null
    private val adapter = PropertyListAdapter(this@PropertyListFragment)
    private var propertysList: List<Property?> = listOf()
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((activity?.application as PropertysApplication).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentPropertyListBinding.inflate(inflater, container, false)

        recyclerView = binding.propertysRecycleView
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        recyclerView!!.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        propertyVM.allPropertys.observe(requireActivity(), Observer { propertys ->
            propertys?.let{ adapter.submitList(it)}
            propertysList = propertys
        })

        return binding.root
    }

    override fun passData(property: Property) {
        val bundle = Bundle()
        bundle.putSerializable("property", property)

        val transaction = this.parentFragmentManager.beginTransaction()
        val fragment2 = PropertyDetailFragment()
        fragment2.arguments = bundle

        transaction.replace(R.id.propertyListFragment, fragment2)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}