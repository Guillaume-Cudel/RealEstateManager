package com.guillaume.project9.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.guillaume.project9.R
import com.guillaume.project9.databinding.ActivityEditPropertyBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.PointsOfInterest
import com.guillaume.project9.model.Property
import com.guillaume.project9.viewmodel.PropertyViewModel
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EditPropertyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPropertyBinding
    private var property: Property? = null
    private var interestList: List<PointsOfInterest> = listOf()
    private var stringInterestList: MutableSet<String?> = mutableSetOf()
    private var photosList: List<Photo> = listOf()
    private var photosMutableList: MutableSet<Photo> = mutableSetOf()
    private var cardChoosed: Int = 0
    private var sold = false
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((this.application as PropertysApplication).repository)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        property = intent.extras!!.getSerializable("property_to_edit") as Property
        propertyVM.getPointsOfInterestByProperty(property!!.propertyId).observe(this, Observer {
            interestList = it
            displayInterest(interestList)
            stringInterestList = setStringInterestList(interestList)
            setListOfInterest()
        })
        propertyVM.getPhotosByProperty(property!!.propertyId).observe(this, Observer {
            photosList = it
            photosMutableList = photosList.toMutableSet()
            if (photosList.isNotEmpty()) {
                displayPhotos(photosList)
                addOrModifyPhoto()
            }

        })
        setDataToEditor(property!!)
        handleSellButton(property!!.sold)

        binding.editPropertySaveButton.setOnClickListener {
            handleSaveButton()
        }
    }


    private fun setDataToEditor(property: Property) {
        binding.editPropertyPriceEdit.setText(property.price.toString())
        binding.editPropertySurfaceEdit.setText(property.surface.toString())
        binding.editPropertyRoomsEdit.setText(property.rooms?.toString())
        binding.editPropertyAddressEdit.setText(property.address)
        binding.editPropertyAddressPostalCodeEdit.setText(property.postalCode.toString())
        binding.editPropertyAddressCityEdit.setText(property.cityAddress)
        binding.editPropertyDescriptionEdit.setText(property.description)
    }

    private fun displayInterest(interestList: List<PointsOfInterest>) {
        for (item in interestList) {
            if (item.pointOfInterest.equals("School"))
                binding.editPropertySchoolBox.isChecked = true
            if (item.pointOfInterest.equals("Park"))
                binding.editPropertyParcBox.isChecked = true
            if (item.pointOfInterest.equals("Transport"))
                binding.editPropertyTransportBox.isChecked = true
            if (item.pointOfInterest.equals("Shop"))
                binding.editPropertyShopBox.isChecked = true
        }
    }

    private fun displayPhotos(photos: List<Photo>) {
        /*val children = binding.editPhotoLinearLayout.children.toList()

        if(children.size >= photos.size) {
            photos.forEachIndexed { index, photo ->
                (children[index] as CardView).setImageBitmap(BitmapFactory.decodeFile(File(photo.photos.toString()).absolutePath))
            }
        }*/
        when (photos.size) {
            1 -> {
                binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
            }
            2 -> {
                binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
            }
            3 -> {
                binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
                binding.editDetailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2].photos.toString()).absolutePath))
            }
            4 -> {
                binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
                binding.editDetailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2].photos.toString()).absolutePath))
                binding.editDetailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3].photos.toString()).absolutePath))
            }
            5 -> {
                binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
                binding.editDetailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2].photos.toString()).absolutePath))
                binding.editDetailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3].photos.toString()).absolutePath))
                binding.editDetailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4].photos.toString()).absolutePath))
            }
            6 -> {
                binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
                binding.editDetailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2].photos.toString()).absolutePath))
                binding.editDetailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3].photos.toString()).absolutePath))
                binding.editDetailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4].photos.toString()).absolutePath))
                binding.editDetailCard6Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[5].photos.toString()).absolutePath))
            }
        }
    }

    private fun setStringInterestList(interestList: List<PointsOfInterest?>): MutableSet<String?> {
        var stringList: MutableSet<String?> = mutableSetOf()
        for (item in interestList) {
            stringList.add(item?.pointOfInterest)
        }
        return stringList
    }

    private fun handleSellButton(soldProperty: Boolean) {

        if(soldProperty){
            binding.editPropertySellButton.isChecked = true
            binding.editPropertySellButton.isClickable = false
        }else {
            binding.editPropertySellButton.setOnClickListener {
                sold = binding.editPropertySellButton.isChecked
            }
        }
    }

    private fun addOrModifyPhoto() {
        binding.editDetailCard1.setOnClickListener {
            cardChoosed = 0
            actionChoice(binding.editDetailCard1Image, cardChoosed)
        }
        binding.editDetailCard2.setOnClickListener {
            cardChoosed = 1
            actionChoice(binding.editDetailCard2Image, cardChoosed)
        }
        binding.editDetailCard3.setOnClickListener {
            cardChoosed = 2
            actionChoice(binding.editDetailCard3Image, cardChoosed)
        }
        binding.editDetailCard4.setOnClickListener {
            cardChoosed = 3
            actionChoice(binding.editDetailCard4Image, cardChoosed)
        }
        binding.editDetailCard5.setOnClickListener {
            cardChoosed = 4
            actionChoice(binding.editDetailCard5Image, cardChoosed)
        }
        binding.editDetailCard6.setOnClickListener {
            cardChoosed = 5
            actionChoice(binding.editDetailCard6Image, cardChoosed)
        }
    }

    private fun actionChoice(imageView: ImageView, itemList: Int) {
        val builder = AlertDialog.Builder(this).create()
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
        builder.show()
    }

    private fun launchAddPhotoActivity() {
        val intent = Intent(this, AddPhotoActivity::class.java)
        intent.putExtra("card_number", cardChoosed)
        Log.i("INTENT:", "$cardChoosed is send !")
        getPhotoContent.launch(intent)
    }

    private val getPhotoContent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {
            val receiveData = it.data

            val bundle: Bundle? = receiveData?.extras
            if (bundle != null) {
                cardChoosed = bundle.getInt("card_number")
                val photoDescription = bundle.getString("photo_description")
                val photoName = bundle.getString("photo_name")
                Log.i("ACTIVITY1_data_received", "Received data: $photoDescription")

                setPhotoInArea(cardChoosed, photoName, photoDescription)
            }
        })

    private fun setPhotoInArea(card: Int, photo: String?, photoDescription: String?) {
        var image: ImageView = binding.editDetailCard1Image
        val photoFile = File(photo)
        val myBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

        when (card) {
            0 -> image = binding.editDetailCard1Image
            1 -> image = binding.editDetailCard2Image
            2 -> image = binding.editDetailCard3Image
            3 -> image = binding.editDetailCard4Image
            4 -> image = binding.editDetailCard5Image
            5 -> image = binding.editDetailCard6Image
        }

        removeOldPhotoData(image, card)
        val addPhoto = Photo(property!!.propertyId, photo, photoDescription)
        photosMutableList.add(addPhoto)
        image.setImageBitmap(myBitmap)
    }

    private fun removeOldPhotoData(image: ImageView, item: Int) {
        if (image.drawable != null) {
            val photo = photosList[item]
            photosMutableList.remove(photo)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSaveButton() {
        val price = binding.editPropertyPriceEdit.editableText?.toString()
        val surface = binding.editPropertySurfaceEdit.editableText?.toString()
        val rooms = binding.editPropertyRoomsEdit.editableText?.toString()
        val description: String? = binding.editPropertyDescriptionEdit.editableText?.toString()
        val address = binding.editPropertyAddressEdit.editableText?.toString()
        val postalCode = binding.editPropertyAddressPostalCodeEdit.editableText?.toString()
        val city = binding.editPropertyAddressCityEdit.editableText?.toString()
        var dateFormatted = property!!.launchOrSellDate
        if (sold) {
            dateFormatted = getDate()
        }
        val newInterestList = convertInterestStringToClass(stringInterestList)

        if (price.equals("") || surface.equals("") || rooms.equals("") || address.equals("")
            || postalCode.equals("") || city.equals("")) {
            Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_LONG).show()
        } else {
            property = Property(
                property!!.propertyId,
                property!!.kind,
                price!!.toInt(),
                surface!!.toDouble(),
                rooms!!.toInt(),
                description,
                property?.photo,
                address!!,
                postalCode!!.toInt(),
                city!!,
                sold,
                dateFormatted,
                property!!.agent
            )

            propertyVM.updateProperty(property!!)
            propertyVM.deletePhotos(property!!.propertyId)
            propertyVM.insertPhotos(photosMutableList.toList())
            propertyVM.deleteInterest(property!!.propertyId)
            propertyVM.insertPointsOfInterest(newInterestList.toList())
            backToPropertyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(): String {
        val zoneId = ZoneId.of("Europe/Paris")
        val date = LocalDateTime.now(zoneId)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return date.format(formatter)
    }

    private fun setListOfInterest() {
        binding.editPropertySchoolBox.setOnClickListener {
            val school = "School"
            if (binding.editPropertySchoolBox.isChecked) {
                stringInterestList.add(school)
            } else stringInterestList.remove(school)
        }
        binding.editPropertyParcBox.setOnClickListener {
            val park = "Park"
            if (binding.editPropertyParcBox.isChecked) {
                stringInterestList.add(park)
            } else stringInterestList.remove(park)
        }
        binding.editPropertyTransportBox.setOnClickListener {
            val transport = "Transport"
            if (binding.editPropertyTransportBox.isChecked) {
                stringInterestList.add(transport)
            } else stringInterestList.remove(transport)
        }
        binding.editPropertyShopBox.setOnClickListener {
            val shop = "Shop"
            if (binding.editPropertyShopBox.isChecked) {
                stringInterestList.add(shop)
            } else stringInterestList.remove(shop)
        }
    }

    private fun convertInterestStringToClass(interest: MutableSet<String?>): MutableSet<PointsOfInterest?> {
        var point: PointsOfInterest?
        var interestClass: MutableSet<PointsOfInterest?> = mutableSetOf()
        for (item in interest) {
            point = PointsOfInterest(property!!.propertyId, item)
            interestClass.add(point)
        }
        return interestClass
    }

    private fun backToPropertyList(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}