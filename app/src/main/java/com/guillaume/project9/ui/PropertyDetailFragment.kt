package com.guillaume.project9.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import com.guillaume.project9.viewmodel.UtilsViewModel
import java.io.File
import java.lang.StringBuilder
import java.text.SimpleDateFormat


class PropertyDetailFragment : Fragment() {

    private lateinit var binding: FragmentPropertyDetailBinding
    private var property: Property? = null
    private var photoList: List<Photo?> = listOf()
    private val utilsVM: UtilsViewModel by activityViewModels()
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((activity?.application as PropertysApplication).repository)
    }
    private val isLandTablet by lazy {
        resources.getBoolean(R.bool.is_tablet)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyDetailBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        if(isLandTablet){
            //todo configure it
            if(property == null){
                propertyVM.allPropertys.observe(requireActivity(), Observer {
                    property = it[0]
                    displayDataRecoved(property!!)
                })
            } else {
                utilsVM.data.observe(viewLifecycleOwner) {
                    property = it
                    displayDataRecoved(property!!)
                }
            }

        } else {
            utilsVM.data.observe(viewLifecycleOwner) {
                property = it
                displayDataRecoved(property!!)
            }
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(!isLandTablet) {
            val add = menu.findItem(R.id.action_bar_add_property)
            add.isVisible = false
            val search = menu.findItem(R.id.action_bar_search_property)
            search.isVisible = false
            val map = menu.findItem(R.id.action_bar_map)
            map.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bar_edit_property -> {
                val intent = Intent(activity, EditPropertyActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("property_to_edit", property)
                intent.putExtras(bundle)
                startActivity(intent)
                true
            }
            R.id.action_bar_loan_simulator -> {
                openLoanDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setPropertyDisplayed(){
        utilsVM.data.observe(viewLifecycleOwner) {
            property = it
            displayDataRecoved(it)
        }
    }

    private fun displayDataRecoved(item: Property){
        /*propertyVM.listInterestLiveData
            .observe(requireActivity(), Observer {
                val interest = it
                displayInterestPoints(interest)
            })
        propertyVM.getPointsOfInterestByProperty(property!!.propertyId)*/
        val id = item.propertyId

        propertyVM.getPointsOfInterestByProperty(id).observe(requireActivity(), Observer {
            val interest = it
            displayInterestPoints(interest)
        })

        propertyVM.getPhotosByProperty(id)
            .observe(requireActivity(), Observer { photos ->
                photoList = photos
                displayData(item)
                displayPhotos(photoList)
            })

        setMapImage(item.address, item.cityAddress)
    }

    private fun setMapImage(address: String, city: String) {
        val centerRequest = adaptToCenterRequest(address, city)
        val markerRequest = adaptToMarkerRequest(address, city)
        val baseUrl =
            "https://maps.googleapis.com/maps/api/staticmap?zoom=16&size=400x400&scale=2&key=AIzaSyCl_z53QkxNDCnSnZwEHQWIK3PNlc6wtwc$centerRequest$markerRequest"

        Glide.with(this.requireActivity())
            .load(baseUrl)
            .centerCrop()
            .into(binding.detailImageMap)

    }

    private fun adaptToCenterRequest(address: String, city: String): String {
        //&center=4+rue+virginia+woolf+toulouse,FR
        val addressConverted = convertToRequest(address)
        val request = "&center=$addressConverted+$city,FR"
        return request
    }


    private fun adaptToMarkerRequest(address: String, city: String): String {
        //&markers=color:red|%4+rue+virginia+woolf+toulouse
        val addressConverted = convertToRequest(address)
        val request = "&markers=color:red|%$addressConverted+$city"
        return request
    }

    private fun convertToRequest(s: String): String {
        return s.replace(' ', '+')
    }

    private fun displayData(propertyRecoved: Property) {
        binding.detailTextAgentResponse.text = propertyRecoved.agent
        binding.detailTextKindResponse.text = propertyRecoved.kind
        binding.detailTextPriceResponse.text = adaptPriceView(propertyRecoved.price.toString())
        binding.detailTextDescriptionResponse.text = propertyRecoved.description
        binding.detailTextSurfaceResponse.text = propertyRecoved.surface.toString()
        val rooms = propertyRecoved.rooms.toString()
        binding.detailTextRoomsResponse.text = rooms
        binding.detailTextLocationAddressResponse.text = propertyRecoved.address
        binding.detailTextLocationPostalCodeResponse.text = propertyRecoved.postalCode.toString()
        binding.detailTextLocationCityResponse.text = propertyRecoved.cityAddress
        if (propertyRecoved.sold) {
            binding.detailTextSoldDate.text = convertToDate(propertyRecoved.launchOrSellDate)
        } else {
            binding.detailTextSold.isVisible = false
            binding.detailTextSoldDate.isVisible = false
        }
    }

    private fun convertToDate(time: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return sdf.format(time)
    }

    private fun displayInterestPoints(interestList: List<PointsOfInterest>) {
        val yes = "Yes"
        val no = "No"
        binding.detailTextSchoolResponse.text = no
        binding.detailTextParkResponse.text = no
        binding.detailTextTransportResponse.text = no
        binding.detailTextShopResponse.text = no

        interestList.map { item ->
            if (item.pointOfInterest.equals("School"))
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

    private fun displayPhotos(photos: List<Photo?>) {
        binding.detailCard1.isVisible = false
        binding.detailCard2.isVisible = false
        binding.detailCard3.isVisible = false
        binding.detailCard4.isVisible = false
        binding.detailCard5.isVisible = false
        binding.detailCard6.isVisible = false

        when(photos.size){

            1 -> {
                binding.detailCard1.isVisible = true

            }
            2 -> {
                binding.detailCard1.isVisible = true
                binding.detailCard2.isVisible = true
            }
            3 -> {
                binding.detailCard1.isVisible = true
                binding.detailCard2.isVisible = true
                binding.detailCard3.isVisible = true

            }
            4 -> {
                binding.detailCard1.isVisible = true
                binding.detailCard2.isVisible = true
                binding.detailCard3.isVisible = true
                binding.detailCard4.isVisible = true
            }
            5 -> {
                binding.detailCard1.isVisible = true
                binding.detailCard2.isVisible = true
                binding.detailCard3.isVisible = true
                binding.detailCard4.isVisible = true
                binding.detailCard5.isVisible = true
            }
            6 -> {
                binding.detailCard1.isVisible = true
                binding.detailCard2.isVisible = true
                binding.detailCard3.isVisible = true
                binding.detailCard4.isVisible = true
                binding.detailCard5.isVisible = true
                binding.detailCard6.isVisible = true
            }
        }

        /*when (photos.size) {
            0 -> {
                binding.detailCard1.isVisible = false
                binding.detailCard2.isVisible = false
                binding.detailCard3.isVisible = false
                binding.detailCard4.isVisible = false
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false
            }
            1 -> {
                binding.detailCard2.isVisible = false
                binding.detailCard3.isVisible = false
                binding.detailCard4.isVisible = false
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false
            }
            2 -> {
                binding.detailCard3.isVisible = false
                binding.detailCard4.isVisible = false
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false
            }
            3 -> {
                binding.detailCard4.isVisible = false
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false
            }
            4 -> {
                binding.detailCard5.isVisible = false
                binding.detailCard6.isVisible = false
            }
            5 -> {
                binding.detailCard6.isVisible = false
            }
        }*/
        if (photos.isNotEmpty()) {
            recovePhotos(photos)
        }

    }

    private fun recovePhotos(photos: List<Photo?>) {

        when (photos.size) {
            1 -> {
                binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if (photos[0]?.description.equals("")) binding.detailCard1Text.text =
                    getString(R.string.photo_1) else binding.detailCard1Text.text =
                    photos[0]?.description
            }
            2 -> {
                binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if (photos[0]?.description.equals("")) binding.detailCard1Text.text =
                    getString(R.string.photo_1) else binding.detailCard1Text.text =
                    photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if (photos[1]?.description.equals("")) binding.detailCard2Text.text =
                    getString(R.string.photo_2) else binding.detailCard2Text.text =
                    photos[1]?.description
            }
            3 -> {
                binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if (photos[0]?.description.equals("")) binding.detailCard1Text.text =
                    getString(R.string.photo_1) else binding.detailCard1Text.text =
                    photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if (photos[1]?.description.equals("")) binding.detailCard2Text.text =
                    getString(R.string.photo_2) else binding.detailCard2Text.text =
                    photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if (photos[2]?.description.equals("")) binding.detailCard3Text.text =
                    getString(R.string.photo_3) else binding.detailCard3Text.text =
                    photos[2]?.description
            }
            4 -> {
                binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if (photos[0]?.description.equals("")) binding.detailCard1Text.text =
                    getString(R.string.photo_1) else binding.detailCard1Text.text =
                    photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if (photos[1]?.description.equals("")) binding.detailCard2Text.text =
                    getString(R.string.photo_2) else binding.detailCard2Text.text =
                    photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if (photos[2]?.description.equals("")) binding.detailCard3Text.text =
                    getString(R.string.photo_3) else binding.detailCard3Text.text =
                    photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if (photos[3]?.description.equals("")) binding.detailCard4Text.text =
                    getString(R.string.photo_4) else binding.detailCard4Text.text =
                    photos[3]?.description
            }
            5 -> {
                binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if (photos[0]?.description.equals("")) binding.detailCard1Text.text =
                    getString(R.string.photo_1) else binding.detailCard1Text.text =
                    photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if (photos[1]?.description.equals("")) binding.detailCard2Text.text =
                    getString(R.string.photo_2) else binding.detailCard2Text.text =
                    photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if (photos[2]?.description.equals("")) binding.detailCard3Text.text =
                    getString(R.string.photo_3) else binding.detailCard3Text.text =
                    photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if (photos[3]?.description.equals("")) binding.detailCard4Text.text =
                    getString(R.string.photo_4) else binding.detailCard4Text.text =
                    photos[3]?.description
                binding.detailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4]!!.photos.toString()).absolutePath))
                if (photos[4]?.description.equals("")) binding.detailCard5Text.text =
                    getString(R.string.photo_5) else binding.detailCard5Text.text =
                    photos[4]?.description
            }
            6 -> {
                binding.detailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0]!!.photos.toString()).absolutePath))
                if (photos[0]?.description.equals("")) binding.detailCard1Text.text =
                    getString(R.string.photo_1) else binding.detailCard1Text.text =
                    photos[0]?.description
                binding.detailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1]!!.photos.toString()).absolutePath))
                if (photos[1]?.description.equals("")) binding.detailCard2Text.text =
                    getString(R.string.photo_2) else binding.detailCard2Text.text =
                    photos[1]?.description
                binding.detailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2]!!.photos.toString()).absolutePath))
                if (photos[2]?.description.equals("")) binding.detailCard3Text.text =
                    getString(R.string.photo_3) else binding.detailCard3Text.text =
                    photos[2]?.description
                binding.detailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3]!!.photos.toString()).absolutePath))
                if (photos[3]?.description.equals("")) binding.detailCard4Text.text =
                    getString(R.string.photo_4) else binding.detailCard4Text.text =
                    photos[3]?.description
                binding.detailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4]!!.photos.toString()).absolutePath))
                if (photos[4]?.description.equals("")) binding.detailCard5Text.text =
                    getString(R.string.photo_5) else binding.detailCard5Text.text =
                    photos[4]?.description
                binding.detailCard6Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[5]!!.photos.toString()).absolutePath))
                if (photos[5]?.description.equals("")) binding.detailCard6Text.text =
                    getString(R.string.photo_6) else binding.detailCard6Text.text =
                    photos[5]?.description
            }
        }
    }

    private fun openLoanDialog() {

        val builder = AlertDialog.Builder(requireActivity()).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_loan_simulator, null)
        builder.setView(dialogView)
        val contributionText = dialogView.findViewById<EditText>(R.id.dialog_loan_contribution_response)
        val interestRateText = dialogView.findViewById<EditText>(R.id.dialog_loan_rate_response)
        val durationText = dialogView.findViewById<EditText>(R.id.dialog_loan_duration_edit)
        val monthlyAmountText = dialogView.findViewById<TextView>(R.id.dialog_loan_monthly_amount)
        val totalAmountText = dialogView.findViewById<TextView>(R.id.dialog_loan_total_amount)
        val quitButton = dialogView.findViewById<Button>(R.id.dialog_loan_button_quit)
        val calculateButton = dialogView.findViewById<Button>(R.id.dialog_loan_button_calculate)
        
        monthlyAmountText.isVisible = false
        totalAmountText.isVisible = false

        quitButton.setOnClickListener {
            builder.dismiss()
        }

        calculateButton.setOnClickListener {
            val contribution = contributionText.editableText.toString()
            val interestRate = interestRateText.editableText.toString()
            val duration = durationText.editableText.toString()

            if(contribution == "" || interestRate == "" || duration == "") fieldEmptyMessage()
            else {
                calculate(dialogView, contribution, interestRate, duration)
                monthlyAmountText.isVisible = true
                totalAmountText.isVisible = true
            }
        }
        builder.show()
    }

    private fun calculate(view: View, contribution: String, interestRate: String, duration: String){
        val monthlyAmountText = view.findViewById<TextView>(R.id.dialog_loan_monthly_amount_response)
        val totalAmountText = view.findViewById<TextView>(R.id.dialog_loan_total_amount_response)
        val contributionInt = contribution.toInt()
        val rate = interestRate.toDouble()
        val years = duration.toInt()
        val propertyAmount = property!!.price
        val propertyAmountWithoutContribution = propertyAmount - contributionInt

        val totalAmount = ((propertyAmountWithoutContribution * rate)/ 100) * years + propertyAmountWithoutContribution
        val totalAmountInt = totalAmount.toInt()
        val totalAmountResponse = adaptPriceView(totalAmountInt.toString())
        totalAmountText.setText(totalAmountResponse)

        var monthlyAmount = (totalAmount / (years * 12)) + 1
        monthlyAmount = removeLastNchars(monthlyAmount)
        val monthlyAmountResponse = "${monthlyAmount.toInt()}€ /month"
        monthlyAmountText.setText(monthlyAmountResponse)
    }

    private fun fieldEmptyMessage(){
        Toast.makeText(requireContext(), "Please fill all fields to calculate", Toast.LENGTH_SHORT).show()
    }

    private fun removeLastNchars(str: Double): Double {
        val strString = str.toString()
        val maxString = 8
        val strSize = strString.length

        return if(strSize > maxString){
            val difference = strSize - maxString
            strString.substring(0, strSize - difference).toDouble()
        } else str
    }


}