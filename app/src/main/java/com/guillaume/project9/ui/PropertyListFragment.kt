package com.guillaume.project9.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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
        val intent = Intent(requireActivity(), PropertyDetailActivity::class.java)
        intent.putExtra("property", property)
        startActivity(intent)
        /*val bundle = Bundle()
        bundle.putSerializable("property", property)*/

        /*val transaction = this.parentFragmentManager.beginTransaction()
        val fragment2 = PropertyDetailFragment()
        fragment2.arguments = bundle

        transaction.replace(R.id.propertyListFragment, fragment2)
        transaction.addToBackStack(null)
        transaction.commit()*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val add = menu.findItem(R.id.action_bar_add_property)
        add.isVisible = false
        val edit = menu.findItem(R.id.action_bar_edit_property)
        edit.isVisible = false
        val map = menu.findItem(R.id.action_bar_map)
        map.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bar_search_property -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSearchDialog(){

        val builder = AlertDialog.Builder(requireContext()).create()
        val view = layoutInflater.inflate(R.layout.dialog_picture_search, null)
        val kinds = resources.getStringArray(R.array.Kind)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dialog_picture_search, kinds)
        val kindSpinner = view.findViewById<Spinner>(R.id.search_dialog_kind_spinner)
        kindSpinner.adapter = arrayAdapter
        val photoNumber = resources.getStringArray(R.array.Photo_number)
        val adapter = ArrayAdapter(requireContext(), R.layout.dialog_picture_search, photoNumber)
        val photoNumberSpinner = view.findViewById<Spinner>(R.id.search_dialog_photos_spinner)
        photoNumberSpinner.adapter = adapter
        val minPrice = view.findViewById<EditText>(R.id.search_dialog_price_min_response)
        val maxPrice = view.findViewById<EditText>(R.id.search_dialog_price_max_response)
        val minSurface = view.findViewById<EditText>(R.id.search_dialog_surface_min_response)
        val maxSurface = view.findViewById<EditText>(R.id.search_dialog_surface_max_response)
        val city = view.findViewById<EditText>(R.id.search_dialog_city_response)
        val school = view.findViewById<CheckBox>(R.id.search_dialog_interest_school)
        val park = view.findViewById<CheckBox>(R.id.search_dialog_interest_park)
        val transport = view.findViewById<CheckBox>(R.id.search_dialog_interest_transport)
        val shop = view.findViewById<CheckBox>(R.id.search_dialog_interest_shop)
        val buttonOK = view.findViewById<Button>(R.id.search_dialog_button_ok)
        val buttonReset = view.findViewById<Button>(R.id.search_dialog_button_reset)

        builder.setView(view)
        builder.setTitle(getString(R.string.looking_for))
        buttonOK.setOnClickListener {
            //todo finish it
            //val
        }
    }


    /*val builder = AlertDialog.Builder(this).create()
    val view = layoutInflater.inflate(R.layout.dialog_picture_choice, null)
    val remove = view.findViewById<ImageView>(R.id.alert_remove_image)
    val gallery = view.findViewById<ImageView>(R.id.alert_gallery_image)

    builder.setView(view)
    remove.setOnClickListener {
        if(imageView.drawable != null){
            photosMutableList.remove(photosList[itemList])}
        imageView.setImageDrawable(null)
        builder.dismiss()
    }
    gallery.setOnClickListener {
        builder.dismiss()
        launchAddPhotoActivity()
    }
    builder.setCanceledOnTouchOutside(true)
    builder.show()*/








}