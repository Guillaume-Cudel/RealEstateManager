package com.guillaume.project9.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.guillaume.project9.R
import com.guillaume.project9.databinding.ActivityAddPropertyBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Property
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.PointsOfInterest
import com.guillaume.project9.viewmodel.PropertyViewModel
import java.io.File
import java.util.*

class AddPropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPropertyBinding

    private val CHANNEL_ID = "CHANNEL_ID"
    val notificationID = 1
    private var kind: Int = 2
    private var kindResult: String = ""
    private var cardChoosed = 0
    private var photoDescription: String? = null
    private var photoName: String? = null
    private var photoList: MutableSet<Photo?> = mutableSetOf()
    private var photoListString : MutableSet<String?> = mutableSetOf()
    private var interestList: MutableSet<String?> = mutableSetOf()
    private val id: String = uniqueId()
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((application as PropertysApplication).repository)
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)*/


        configureEstateAgentInputText()
        setKindProperty()
        addPhotos()
        setListOfInterest()

        activateSaveButton()
        binding.addPropertyValidateButton.setOnClickListener {
            recovePropertyData()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        val itemId = item.itemId
        if(itemId == android.R.id.home){
            finish()
        }
        return true
    }

    private fun activateSaveButton(){
        binding.addPropertyEstateAgentText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.addPropertyValidateButton.isEnabled = s?.length!! > 0
                binding.addPropertyValidateButton.backgroundTintList =
                    ContextCompat.getColorStateList(this@AddPropertyActivity, R.color.overlay_light_primary)
            }
        })
    }

    private fun setListOfInterest(){

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun recovePropertyData() {
        val price = binding.addPropertyPriceEdit.editableText?.toString()
        val surface = binding.addPropertySurfaceEdit.editableText?.toString()
        val rooms = binding.addPropertyRoomsEdit.editableText?.toString()
        val description: String? = binding.addPropertyDescriptionEdit.editableText?.toString()
        verifyEmptyData(description)

        val list: List<Photo?> = photoList.toList()
        val stringPhotosList: List<String?> = photoListString.toList()
        var firstPhoto: String? = null
        if(stringPhotosList.isNotEmpty()){
            firstPhoto = stringPhotosList[0]
        }
        val address = binding.addPropertyAddressEdit.editableText?.toString()
        val postalCode = binding.addPropertyAddressPostalCodeEdit.editableText?.toString()
        val city = binding.addPropertyAddressCityEdit.editableText?.toString()
        val pointsOfInterestList = convertInterestStringToClass(interestList)

        val agent = binding.addPropertyEstateAgentText.editableText.toString()
        val dateFormatted = getDate()

        if(price.equals("") || surface.equals("") || rooms.equals("") || address.equals("")
            || postalCode.equals("") || city.equals("")){
            Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_LONG).show()
        }else {

            val property = Property(id,
                kindResult,
                price!!.toInt(),
                surface!!.toDouble(),
                rooms!!.toInt(),
                description,
                firstPhoto,
                address!!,
                postalCode!!.toInt(),
                city!!,
                false,
                dateFormatted,
                agent,
            null)

            propertyVM.insertProperty(property)
            propertyVM.insertPhotos(list)
            propertyVM.insertPointsOfInterest(pointsOfInterestList.toList())
            sendNotification()
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(): Long {
        return System.currentTimeMillis()
    }

    private fun verifyEmptyData(field: String?): String? {
        if(field.equals("")) field.equals(null)

        return field
    }

    private fun convertInterestStringToClass(interest: MutableSet<String?>): MutableSet<PointsOfInterest?>{
        var point: PointsOfInterest?
        var interestClass: MutableSet<PointsOfInterest?> = mutableSetOf()
        for(item in interest){
            point = PointsOfInterest(id, item)
            interestClass.add(point)
        }
        return interestClass
    }



    private fun configureEstateAgentInputText() {
        val estateAgents = resources.getStringArray(R.array.Estate_agents)
        val arrayAdapter = ArrayAdapter(this, R.layout.estate_agent_list_items, estateAgents)
        val autoCompleteTextView: AutoCompleteTextView = findViewById(R.id.add_property_estate_agent_text)
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

            setPhotoInArea(cardChoosed, photoName, photoDescription)

        }
    })

    private fun setPhotoInArea(card: Int, photo: String?, photoDescription: String?) {
        var image: ImageView = binding.addPropertyPhoto1
        val photoFile = File(photo)
        val myBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        photoListString.add(photo)

        when (card) {
            1 -> {
                image = binding.addPropertyPhoto1
                binding.addPropertyPhoto1Text.text = ""
            }
            2 -> {
                image = binding.addPropertyPhoto2
                binding.addPropertyPhoto2Text.text = ""
            }
            3 -> {
                image = binding.addPropertyPhoto3
                binding.addPropertyPhoto3Text.text = ""
            }
            4 -> {
                image = binding.addPropertyPhoto4
                binding.addPropertyPhoto4Text.text = ""
            }
        }

        val addPhoto = Photo(id, photo, photoDescription)
        photoList.add(addPhoto)

        Glide.with(this)
            .load(myBitmap)
            .centerCrop()
            .into(image)
    }

    private fun uniqueId():String = UUID.randomUUID().toString()

    private fun sendNotification(){
        val title = getString(R.string.adding_of_property)
        val textContent = getString(R.string.add_property_went_well)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.outline_business_black_48)
            .setContentTitle(title)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)){
            notify(notificationID, builder.build())
        }
    }


}