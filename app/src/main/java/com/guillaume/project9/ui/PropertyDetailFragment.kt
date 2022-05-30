package com.guillaume.project9.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.guillaume.project9.R
import com.guillaume.project9.databinding.FragmentPropertyDetailBinding
import com.guillaume.project9.databinding.FragmentPropertyListBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.PointsOfInterest
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

        setHasOptionsMenu(true)


        property = arguments?.getSerializable("property") as Property?

        propertyVM.getPointsOfInterestByProperty(property!!.propertyId).observe(requireActivity(), Observer {
            val interest = it
            displayInterestPoints(interest)
        })

        propertyVM.getPhotosByProperty(property!!.propertyId).observe(requireActivity(), Observer {
            photoList = it
            displayData()
            //displayInterestPoints()
            displayPhotos(photoList)
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val add = menu.findItem(R.id.action_bar_add_property)
        add.isVisible = false
        val search = menu.findItem(R.id.action_bar_search_property)
        search.isVisible = false
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_bar_edit_property -> {
                //todo handle this
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/

    private fun displayData(){
        binding.detailTextAgentResponse.text = property!!.agent
        binding.detailTextKindResponse.text = property!!.kind
        binding.detailTextPriceResponse.text = property!!.price.toString()
        binding.detailTextDescriptionResponse.text = property?.description
        binding.detailTextSurfaceResponse.text = property!!.surface.toString()
        val rooms = property?.rooms.toString()
        binding.detailTextRoomsResponse.text = rooms
        binding.detailTextLocationAddressResponse.text = property!!.address
        binding.detailTextLocationPostalCodeResponse.text = property!!.postalCode.toString()
        binding.detailTextLocationCityResponse.text = property!!.cityAddress
    }

    private fun displayInterestPoints(interestList: List<PointsOfInterest>){
        val yes = "Yes"
        for(item in interestList){
            if(item.pointOfInterest.equals("School"))
                binding.detailTextSchoolResponse.text = yes
            if (item.pointOfInterest.equals("Park"))
                binding.detailTextParkResponse.text = yes
            if (item.pointOfInterest.equals("Transport"))
                binding.detailTextTransportResponse.text = yes
            if (item.pointOfInterest.equals("Shop"))
                binding.detailTextShopResponse.text = yes
        }
    }

    //todo display photos
    private fun displayPhotos(photos: List<Photo?>){
        binding.detailCard1.isVisible = false
    }


}