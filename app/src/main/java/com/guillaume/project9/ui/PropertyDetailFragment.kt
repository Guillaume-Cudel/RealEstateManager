package com.guillaume.project9.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
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
import java.text.SimpleDateFormat


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


        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

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
        if(property != null){
            setMapImage(property!!.address, property!!.cityAddress)
        }

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
        val map = menu.findItem(R.id.action_bar_map)
        map.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_bar_edit_property -> {
                val intent = Intent(activity, EditPropertyActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("property_to_edit", property)
                intent.putExtras(bundle)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setMapImage(address: String, city: String){
        val centerRequest = adaptToCenterRequest(address, city)
        val markerRequest = adaptToMarkerRequest(address, city)
        val baseUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=16&size=400x400&scale=2&key=AIzaSyCl_z53QkxNDCnSnZwEHQWIK3PNlc6wtwc$centerRequest$markerRequest"

        Glide.with(this.requireActivity())
            .load(baseUrl)
            .centerCrop()
            .into(binding.detailImageMap)

    }

    private fun adaptToCenterRequest( address: String, city: String): String{
        //&center=4+rue+virginia+woolf+toulouse,FR
        val addressConverted = convertToRequest(address)
        val request = "&center=$addressConverted+$city,FR"
        return request
    }


    private fun adaptToMarkerRequest(address : String, city: String): String{
        //&markers=color:red|%4+rue+virginia+woolf+toulouse
        val addressConverted = convertToRequest(address)
        val request = "&markers=color:red|%$addressConverted+$city"
        return request
    }

    private fun convertToRequest(s: String): String {
        return s.replace(' ', '+')
    }

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
        if(property!!.sold){
            binding.detailTextSoldDate.text = convertToDate(property!!.launchOrSellDate)
        } else {
            binding.detailTextSold.isVisible = false
            binding.detailTextSoldDate.isVisible = false
        }
    }

    private fun convertToDate(time: Long): String{
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return sdf.format(time)
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
                if(photos[0]?.description.equals("")) binding.detailCard1Text.text = getString(R.string.photo_1) else binding.detailCard1Text.text = photos[0]?.description }
            2 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals("")) binding.detailCard1Text.text = getString(R.string.photo_1) else binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals("")) binding.detailCard2Text.text = getString(R.string.photo_2) else binding.detailCard2Text.text = photos[1]?.description}
            3 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals("")) binding.detailCard1Text.text = getString(R.string.photo_1) else binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals("")) binding.detailCard2Text.text = getString(R.string.photo_2) else binding.detailCard2Text.text = photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if(photos[2]?.description.equals("")) binding.detailCard3Text.text = getString(R.string.photo_3) else binding.detailCard3Text.text = photos[2]?.description}
            4 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals("")) binding.detailCard1Text.text = getString(R.string.photo_1) else binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals("")) binding.detailCard2Text.text = getString(R.string.photo_2) else binding.detailCard2Text.text = photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if(photos[2]?.description.equals("")) binding.detailCard3Text.text = getString(R.string.photo_3) else binding.detailCard3Text.text = photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if(photos[3]?.description.equals("")) binding.detailCard4Text.text = getString(R.string.photo_4) else binding.detailCard4Text.text = photos[3]?.description}
            5 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals("")) binding.detailCard1Text.text = getString(R.string.photo_1) else binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals("")) binding.detailCard2Text.text = getString(R.string.photo_2) else binding.detailCard2Text.text = photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if(photos[2]?.description.equals("")) binding.detailCard3Text.text = getString(R.string.photo_3) else binding.detailCard3Text.text = photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if(photos[3]?.description.equals("")) binding.detailCard4Text.text = getString(R.string.photo_4) else binding.detailCard4Text.text = photos[3]?.description
                binding.detailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4]!!.photos.toString()).absolutePath))
                if(photos[4]?.description.equals("")) binding.detailCard5Text.text = getString(R.string.photo_5) else binding.detailCard5Text.text = photos[4]?.description}
            6 -> {binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if(photos[0]?.description.equals("")) binding.detailCard1Text.text = getString(R.string.photo_1) else binding.detailCard1Text.text = photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if(photos[1]?.description.equals("")) binding.detailCard2Text.text = getString(R.string.photo_2) else binding.detailCard2Text.text = photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if(photos[2]?.description.equals("")) binding.detailCard3Text.text = getString(R.string.photo_3) else binding.detailCard3Text.text = photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if(photos[3]?.description.equals("")) binding.detailCard4Text.text = getString(R.string.photo_4) else binding.detailCard4Text.text = photos[3]?.description
                binding.detailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4]!!.photos.toString()).absolutePath))
                if(photos[4]?.description.equals("")) binding.detailCard5Text.text = getString(R.string.photo_5) else binding.detailCard5Text.text = photos[4]?.description
                binding.detailCard6Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[5]!!.photos.toString()).absolutePath))
                if(photos[5]?.description.equals("")) binding.detailCard6Text.text = getString(R.string.photo_6) else binding.detailCard6Text.text = photos[5]?.description}
        }
    }

}