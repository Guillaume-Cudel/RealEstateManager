package com.guillaume.project9.ui

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.guillaume.project9.R
import com.guillaume.project9.databinding.ActivityAddPropertyBinding
import com.guillaume.project9.model.PropertyPhoto
import java.io.File
import java.net.URI
import java.util.*

class AddPropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPropertyBinding

    private var kind: Int = 2
    private var kindResult: String = ""
    private var cardChoosed = 0
    private var photoDescription: String? = null
    private var photoName: String? = null
    private var photo1: PropertyPhoto? = null
    private var photo2: PropertyPhoto? = null
    private var photo3: PropertyPhoto? = null
    private var photo4: PropertyPhoto? = null
    private var interestList = mutableSetOf("")




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEstateAgentInputText()
        setKindProperty()
        addPhotos()
        setListOfInteret()

        binding.addPropertyValidateButton.setOnClickListener {
            recovePropertyData()
        }
    }

    private fun setListOfInteret(){
        interestList.remove("")

        binding.addPropertySchoolBox.setOnClickListener {
            val school = "School"
            if(binding.addPropertySchoolBox.isChecked){
                interestList.add(school)
            }else interestList.remove(school)
        }
        binding.addPropertyParcBox.setOnClickListener {
            val park = "Park"
            if(binding.addPropertyParcBox.isChecked){
                interestList.add(park)
            } else interestList.remove(park)
        }
        binding.addPropertyTransportBox.setOnClickListener {
            val transport = "Transport"
            if(binding.addPropertyTransportBox.isChecked){
                interestList.add(transport)
            } else interestList.remove(transport)
        }
        binding.addPropertyShopBox.setOnClickListener {
            val shop = "Shop"
            if(binding.addPropertyShopBox.isChecked){
                interestList.add(shop)
            } else interestList.remove(shop)
        }

    }


    private fun setKindProperty() {
        setKindProperty(kind)
        binding.addPropertyKindHouse.setOnClickListener {
            kind = 0
            setKindProperty(kind)
        }
        binding.addPropertyKindGround.setOnClickListener {
            kind = 1
            setKindProperty(kind)
        }
        binding.addPropertyKindApartment.setOnClickListener {
            kind = 2
            setKindProperty(kind)
        }
        binding.addPropertyKindParking.setOnClickListener {
            kind = 3
            setKindProperty(kind)
        }
        binding.addPropertyKindOther.setOnClickListener {
            kind = 4
            setKindProperty(kind)
        }
    }


    private fun setKindProperty(kind: Int) {
        binding.addPropertyKindHouse.clearColorFilter()
        binding.addPropertyKindHouse.setBackgroundResource(R.drawable.round)
        binding.addPropertyKindGround.clearColorFilter()
        binding.addPropertyKindGround.setBackgroundResource(R.drawable.round)
        binding.addPropertyKindApartment.clearColorFilter()
        binding.addPropertyKindApartment.setBackgroundResource(R.drawable.round)
        binding.addPropertyKindParking.clearColorFilter()
        binding.addPropertyKindParking.setBackgroundResource(R.drawable.round)
        binding.addPropertyKindOther.clearColorFilter()
        binding.addPropertyKindOther.setBackgroundResource(R.drawable.round)

        when (kind) {
            0 ->{
                kindResult = "House"
                binding.addPropertyKindHouse.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
                binding.addPropertyKindHouse.setBackgroundColor(resources.getColor(R.color.white))
            }
            1 ->{
                kindResult = "Ground"
                binding.addPropertyKindGround.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
                binding.addPropertyKindGround.setBackgroundColor(resources.getColor(R.color.white))
            }
            2 ->{
                kindResult = "Apartment"
                binding.addPropertyKindApartment.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
                binding.addPropertyKindApartment.setBackgroundColor(resources.getColor(R.color.white))
            }
            3 ->{
                kindResult = "Parking"
                binding.addPropertyKindParking.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
                binding.addPropertyKindParking.setBackgroundColor(resources.getColor(R.color.white))
            }
            4 ->{
                kindResult = "Other"
                binding.addPropertyKindOther.setColorFilter(resources.getColor(R.color.overlay_light_primaryContainer))
                binding.addPropertyKindOther.setBackgroundColor(resources.getColor(R.color.white))
            }
        }
    }

    private fun recovePropertyData() {
        // todo recove all data
        kindResult
        binding.addPropertyPriceEdit.editableText.toString()
        binding.addPropertySurfaceEdit.editableText.toString()
        binding.addPropertyRoomsEdit.editableText.toString()
        binding.addPropertyAddressEdit.editableText.toString()
        binding.addPropertyAddressPostalCodeEdit.editableText.toString()
        binding.addPropertyAddressCityEdit.editableText.toString()
        binding.addPropertyDescriptionEdit.editableText.toString()
        interestList.size
        binding.addPropertyEstateAgentText.editableText.toString()
        val photos: MutableList<PropertyPhoto?> = mutableListOf(photo1, photo2, photo3, photo4)

    }


    private fun configureEstateAgentInputText() {
        val estateAgents = resources.getStringArray(R.array.Estate_agents)
        val arrayAdapter = ArrayAdapter(this, R.layout.estate_agent_list_items, estateAgents)
        val autoCompleteTextView: AutoCompleteTextView =
            findViewById(R.id.add_property_estate_agent_text)
        autoCompleteTextView.setAdapter(arrayAdapter)
    }


    private fun addPhotos() {
        binding.addPropertyCard1.setOnClickListener {
            cardChoosed = 1
            launchAddPhotoActivity()
        }
        binding.addPropertyCard2.setOnClickListener {
            cardChoosed = 2
            launchAddPhotoActivity()
        }
        binding.addPropertyCard3.setOnClickListener {
            cardChoosed = 3
            launchAddPhotoActivity()
        }
        binding.addPropertyCard4.setOnClickListener {
            cardChoosed = 4
            launchAddPhotoActivity()
        }
    }

    private fun launchAddPhotoActivity(){
        val intent = Intent(this, AddPhotoActivity::class.java)
        intent.putExtra("card_number", cardChoosed)
        intent.putExtra("photo_name", photoName)
        intent.putExtra("photo_description", photoDescription)
        Log.i("INTENT:",  "$cardChoosed is send !")
        getPhotoContent.launch(intent)
    }

    private val getPhotoContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
        val receiveData = it.data

        val bundle: Bundle? = receiveData?.extras
        if (bundle != null) {
            cardChoosed = bundle.getInt("card_number")
            photoDescription= bundle.getString("photo_description")
            photoName = bundle.getString("photo_name")
            Log.i("ACTIVITY1_data_received", "Received data: $photoDescription")
            val photoFile = File(photoName)
            setPhotoInArea(cardChoosed, photoFile, photoDescription)

        }
    })

    private fun setPhotoInArea(card: Int, photo: File, photoDecription: String?) {
        var image: ImageView = binding.addPropertyPhoto1
        val myBitmap = BitmapFactory.decodeFile(photo.absolutePath)

        when (card) {
            1 -> {
                image = binding.addPropertyPhoto1
                binding.addPropertyPhoto1Text.text = ""
                photo1 = PropertyPhoto(uniqueId(), photo, photoDecription)
            }
            2 -> {
                image = binding.addPropertyPhoto2
                binding.addPropertyPhoto2Text.text = ""
                photo2 = PropertyPhoto(uniqueId(), photo, photoDecription)
            }
            3 -> {
                image = binding.addPropertyPhoto3
                binding.addPropertyPhoto3Text.text = ""
                photo3 = PropertyPhoto(uniqueId(), photo, photoDecription)
            }
            4 -> {
                image = binding.addPropertyPhoto4
                binding.addPropertyPhoto4Text.text = ""
                photo4 = PropertyPhoto(uniqueId(), photo, photoDecription)
            }
        }
        Glide.with(this)
            .load(myBitmap)
            .centerCrop()
            .into(image)
    }

    fun uniqueId():String = UUID.randomUUID().toString()


}