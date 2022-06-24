package com.guillaume.project9.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.project9.R
import com.guillaume.project9.databinding.FragmentPropertyListBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Property
import com.guillaume.project9.viewmodel.PropertyViewModel
import com.guillaume.project9.viewmodel.SearchViewModel
import com.guillaume.project9.viewmodel.SearchViewModelFactory
import java.util.*
import kotlin.concurrent.schedule


class PropertyListFragment : Fragment(), Communicator {

    private lateinit var binding: FragmentPropertyListBinding
    private var recyclerView: RecyclerView? = null
    private val adapter = PropertyListAdapter(this@PropertyListFragment)
    private var propertyListSearched: MutableList<Property> = mutableListOf()
    private var databaseList: List<Property> = listOf()

    private lateinit var searchVM: SearchViewModel
    private lateinit var searchVMfactory: SearchViewModelFactory
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((activity?.application as PropertysApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPropertyListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        searchVMfactory =
            SearchViewModelFactory((activity?.application as PropertysApplication).repository)
        searchVM = ViewModelProvider(this, searchVMfactory)[SearchViewModel::class.java]


        recyclerView = binding.propertysRecycleView
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        propertyVM.allPropertys.observe(requireActivity(), Observer { propertys ->
            databaseList = propertys
            propertys?.let { adapter.submitList(it) }
        })

        return binding.root
    }

    override fun passData(property: Property) {
        val intent = Intent(requireActivity(), PropertyDetailActivity::class.java)
        intent.putExtra("property", property)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val edit = menu.findItem(R.id.action_bar_edit_property)
        edit.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bar_search_property -> {
                openSearchDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSearchDialog() {

        val builder = AlertDialog.Builder(requireContext()).create()
        val view = layoutInflater.inflate(R.layout.dialog_picture_search, null)
        val kinds = resources.getStringArray(R.array.Kind)
        val kindsAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            kinds
        )
        val kindSpinner = view.findViewById<Spinner>(R.id.search_dialog_kind_spinner)
        kindSpinner.adapter = kindsAdapter
        val photoNumber = resources.getStringArray(R.array.Photo_number)
        val photoNumberAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            photoNumber
        )
        val photoNumberSpinner = view.findViewById<Spinner>(R.id.search_dialog_photos_spinner)
        photoNumberSpinner.adapter = photoNumberAdapter
        val minPriceText = view.findViewById<EditText>(R.id.search_dialog_price_min_response)
        val maxPriceText = view.findViewById<EditText>(R.id.search_dialog_price_max_response)
        val minSurfaceText = view.findViewById<EditText>(R.id.search_dialog_surface_min_response)
        val maxSurfaceText = view.findViewById<EditText>(R.id.search_dialog_surface_max_response)
        val cityText = view.findViewById<EditText>(R.id.search_dialog_city_response)
        val schoolBox = view.findViewById<CheckBox>(R.id.search_dialog_interest_school)
        val parkBox = view.findViewById<CheckBox>(R.id.search_dialog_interest_park)
        val transportBox = view.findViewById<CheckBox>(R.id.search_dialog_interest_transport)
        val shopBox = view.findViewById<CheckBox>(R.id.search_dialog_interest_shop)
        val buttonOK = view.findViewById<Button>(R.id.search_dialog_button_ok)
        val buttonReset = view.findViewById<Button>(R.id.search_dialog_button_reset)

        builder.setView(view)
        builder.setTitle(getString(R.string.looking_for))

        buttonOK.setOnClickListener {
            propertyListSearched.clear()
            val kind = kindSpinner.selectedItem.toString()
            val photosNumber = photoNumberSpinner.selectedItem.toString()
            val minPrice = minPriceText.text.toString()
            val maxPrice = maxPriceText.text.toString()
            val minSurface = minSurfaceText.text.toString()
            val maxSurface = maxSurfaceText.text.toString()
            val city = cityText.text.toString()

            val interestList: MutableList<String> = mutableListOf()

            if (schoolBox.isChecked) interestList.add("School")
            if (parkBox.isChecked) interestList.add("Park")
            if (transportBox.isChecked) interestList.add("Transport")
            if (shopBox.isChecked) interestList.add("Shop")

            searchPropertysWithConditions(
                kind,
                minPrice,
                maxPrice,
                minSurface,
                maxSurface,
                city,
                photosNumber,
                interestList.toList()
            )
            builder.dismiss()
        }
        buttonReset.setOnClickListener {
            adapter.submitList(databaseList)
            builder.dismiss()
        }
        builder.show()
    }

    private fun searchPropertysWithConditions(
        kind: String,
        priceMin: String,
        priceMax: String,
        surfaceMin: String,
        surfaceMax: String,
        city: String,
        photoNumber: String,
        interest: List<String>
    ) {
        var smallPrice = priceMin
        var tallPrice = priceMax
        var smallSurface = surfaceMin
        var tallSurface = surfaceMax
        var photos: Int = 0
        var interestNumber = 0
        if (smallPrice == "") smallPrice = "1"
        if (tallPrice == "") tallPrice = "100000000"
        if (smallSurface == "") smallSurface = "1.0"
        if (tallSurface == "") tallSurface = "500000.0"
        if (photoNumber != "None") photos = photoNumber.toInt()
        val interestTest = interest.size
        if (interest.isNotEmpty()) interestNumber = interest.size


        searchVM.searchPropertysWithConditions(
            kind, smallPrice.toInt(), tallPrice.toInt(),
            smallSurface.toDouble(), tallSurface.toDouble()
        ).observe(viewLifecycleOwner) {

            it.map { property ->
                if (!property.sold) {

                    propertyVM.getPhotosByProperty(property.propertyId)
                        .observe(requireActivity(), Observer { photosListSaved ->
                            val photosInt = photosListSaved.size

                            propertyVM.getPointsOfInterestByProperty(property.propertyId)
                                .observe(requireActivity(), Observer { interestListSaved ->
                                    var interestCounter = 0
                                    interest.map { word ->
                                        interestListSaved.map { pointOfInterest ->
                                            if (pointOfInterest.pointOfInterest == word) interestCounter++

                                        }
                                    }

                                    if (city != "") {
                                        if (photos <= photosInt && interestNumber <= interestCounter && property.cityAddress == city) {
                                            addOrNotInSearchList(property)
                                            //propertyListSearched.add(property)
                                        }
                                    } else {
                                        if (photos <= photosInt && interestNumber <= interestCounter) {
                                            addOrNotInSearchList(property)
                                            //propertyListSearched.add(property)
                                        }
                                    }



                                    if (propertyListSearched.isEmpty()) returnNoProperty()
                                    else adapter.submitList(propertyListSearched)
                                })
                        })
                } else {
                    returnNoProperty()
                }
            }
            if (it.isEmpty()) {
                returnNoProperty()
            }
        }
    }

    private fun addOrNotInSearchList(property: Property){
        var isSame = false
        propertyListSearched.map { item ->
            isSame = item.propertyId == property.propertyId
        }
        if(!isSame) propertyListSearched.add(property)
    }

    private fun returnNoProperty() {
        Toast.makeText(
            requireContext(),
            "Sorry, there are no property with your criteria... ",
            Toast.LENGTH_LONG
        ).show()
    }
}