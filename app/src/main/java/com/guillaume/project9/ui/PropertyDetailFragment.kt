package com.guillaume.project9.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.guillaume.project9.R
import com.guillaume.project9.databinding.FragmentPropertyDetailBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.PointsOfInterest
import com.guillaume.project9.model.Property
import com.guillaume.project9.viewmodel.PropertyViewModel
import java.io.File
import java.lang.StringBuilder


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
        binding.detailTextPriceResponse.text = adaptPriceView(property!!.price.toString())
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

    private fun adaptPriceView(price: String): String {
        val str = StringBuilder(price)
        var idx: Int = str.length - 3
        while (idx > 0) {
            str.insert(idx, ".");
            idx -= 3;
        }
        return "$str €"
    }

    private fun displayPhotos(photos: List<Photo?>){
        when(photos.size){
            0 -> {binding.detailCard1.isVisible = false
                binding.detailCard2.isVisible = false
                binding.detailCard3.isVisible = false
                binding.detailCard4.isVisible = false
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false}
            1 -> {binding.detailCard2.isVisible = false
                binding.detailCard3.isVisible = false
                binding.detailCard4.isVisible = false
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false}
            2 -> { binding.detailCard3.isVisible = false
                binding.detailCard4.isVisible = false
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false}
            3 -> {binding.detailCard4.isVisible = false
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false}
            4 -> {binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false}
            5 -> {binding.detailCard6.isVisible = false}
        }
        if(photos.isNotEmpty()){
        recovePhotos(photos)
        }

    }

    private fun recovePhotos(photos: List<Photo?>){

        when(photos.size){
            1 -> { binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals(null)) binding.detailCard1Text.isVisible = false
                binding.detailCard1Text.text = photos[0]?.description }
            2 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals(null)) binding.detailCard1Text.isVisible = false
                binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals(null)) binding.detailCard2Text.isVisible = false
                binding.detailCard2Text.text = photos[1]?.description}
            3 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals(null)) binding.detailCard1Text.isVisible = false
                binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals(null)) binding.detailCard2Text.isVisible = false
                binding.detailCard2Text.text = photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if(photos[2]?.description.equals(null)) binding.detailCard3Text.isVisible = false
                binding.detailCard3Text.text = photos[2]?.description}
            4 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals(null)) binding.detailCard1Text.isVisible = false
                binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals(null)) binding.detailCard2Text.isVisible = false
                binding.detailCard2Text.text = photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if(photos[2]?.description.equals(null)) binding.detailCard3Text.isVisible = false
                binding.detailCard3Text.text = photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if(photos[3]?.description.equals(null)) binding.detailCard4Text.isVisible = false
                binding.detailCard4Text.text = photos[3]?.description}
            5 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals(null)) binding.detailCard1Text.isVisible = false
                binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals(null)) binding.detailCard2Text.isVisible = false
                binding.detailCard2Text.text = photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if(photos[2]?.description.equals(null)) binding.detailCard3Text.isVisible = false
                binding.detailCard3Text.text = photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if(photos[3]?.description.equals(null)) binding.detailCard4Text.isVisible = false
                binding.detailCard4Text.text = photos[3]?.description
                binding.detailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4]!!.photos.toString()).absolutePath))
                if(photos[4]?.description.equals(null)) binding.detailCard5Text.isVisible = false
                binding.detailCard5Text.text = photos[4]?.description}
            6 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals(null)) binding.detailCard1Text.isVisible = false
                binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals(null)) binding.detailCard2Text.isVisible = false
                binding.detailCard2Text.text = photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if(photos[2]?.description.equals(null)) binding.detailCard3Text.isVisible = false
                binding.detailCard3Text.text = photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if(photos[3]?.description.equals(null)) binding.detailCard4Text.isVisible = false
                binding.detailCard4Text.text = photos[3]?.description
                binding.detailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4]!!.photos.toString()).absolutePath))
                if(photos[4]?.description.equals(null)) binding.detailCard5Text.isVisible = false
                binding.detailCard5Text.text = photos[4]?.description
                binding.detailCard6Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[5]!!.photos.toString()).absolutePath))
                if(photos[5]?.description.equals(null)) binding.detailCard6Text.isVisible = false
                binding.detailCard6Text.text = photos[5]?.description}
        }
    }
}