package com.guillaume.project9.ui

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.guillaume.project9.R
import com.guillaume.project9.databinding.ActivityEditPropertyBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.PointsOfInterest
import com.guillaume.project9.model.Property
import com.guillaume.project9.viewmodel.PropertyViewModel
import java.io.File

class EditPropertyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPropertyBinding
    private var property: Property? = null
    private var interestList: List<PointsOfInterest> = listOf()
    private var photosList: List<Photo> = listOf()
    private var photosMutableList: MutableSet<Photo> = mutableSetOf()
    private var cardChoosed: Int = 0
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((this.application as PropertysApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        property = intent.extras!!.getSerializable("property_to_edit") as Property
        propertyVM.getPointsOfInterestByProperty(property!!.propertyId).observe(this, Observer {
            interestList = it
            displayInterest(interestList)
        })
        propertyVM.getPhotosByProperty(property!!.propertyId).observe(this, Observer {
            photosList = it
            photosMutableList = photosList.toMutableSet()
            if(photosList.isNotEmpty()){
                displayPhotos(photosList)
            }

        })
        setDataToEditor(property!!)
        addOrModifyPhoto()

        //todo set on save button
    }


    private fun setDataToEditor(property: Property){
        binding.editPropertyPriceEdit.setText(property.price.toString())
        binding.editPropertySurfaceEdit.setText(property.surface.toString())
        binding.editPropertyRoomsEdit.setText(property.rooms?.toString())
        binding.editPropertyAddressEdit.setText(property.address)
        binding.editPropertyAddressPostalCodeEdit.setText(property.postalCode.toString())
        binding.editPropertyAddressCityEdit.setText(property.cityAddress)
        binding.editPropertyDescriptionEdit.setText(property.description)
    }

    private fun displayInterest(interestList: List<PointsOfInterest>){
        for(item in interestList){
            if(item.pointOfInterest.equals("School"))
                binding.editPropertySchoolBox.isChecked = true
            if (item.pointOfInterest.equals("Park"))
                binding.editPropertyParcBox.isChecked = true
            if (item.pointOfInterest.equals("Transport"))
                binding.editPropertyTransportBox.isChecked = true
            if (item.pointOfInterest.equals("Shop"))
                binding.editPropertyShopBox.isChecked = true
        }
    }

    private fun displayPhotos(photos: List<Photo>){
        when(photos.size){
            1 -> {binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))}
            2 -> {binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))}
            3 -> {binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
                binding.editDetailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2].photos.toString()).absolutePath))}
            4 -> {binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
                binding.editDetailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2].photos.toString()).absolutePath))
                binding.editDetailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3].photos.toString()).absolutePath))}
            5 -> {binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
                binding.editDetailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2].photos.toString()).absolutePath))
                binding.editDetailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3].photos.toString()).absolutePath))
                binding.editDetailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4].photos.toString()).absolutePath))}
            6 -> {binding.editDetailCard1Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[0].photos.toString()).absolutePath))
                binding.editDetailCard2Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[1].photos.toString()).absolutePath))
                binding.editDetailCard3Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[2].photos.toString()).absolutePath))
                binding.editDetailCard4Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[3].photos.toString()).absolutePath))
                binding.editDetailCard5Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[4].photos.toString()).absolutePath))
                binding.editDetailCard6Image.setImageBitmap(BitmapFactory.decodeFile(File(photos[5].photos.toString()).absolutePath))}
        }
    }

    private fun addOrModifyPhoto(){
        binding.editDetailCard1.setOnClickListener { cardChoosed = 0
            actionChoice(binding.editDetailCard1Image, cardChoosed) }
        binding.editDetailCard2.setOnClickListener { cardChoosed = 1
            actionChoice(binding.editDetailCard2Image, cardChoosed) }
        binding.editDetailCard3.setOnClickListener { cardChoosed = 2
            actionChoice(binding.editDetailCard3Image, cardChoosed) }
        binding.editDetailCard4.setOnClickListener { cardChoosed = 3
            actionChoice(binding.editDetailCard4Image, cardChoosed) }
        binding.editDetailCard5.setOnClickListener { cardChoosed = 4
            actionChoice(binding.editDetailCard5Image, cardChoosed) }
        binding.editDetailCard6.setOnClickListener { cardChoosed = 5
            actionChoice(binding.editDetailCard6Image, cardChoosed) }
    }

    private fun actionChoice(imageView: ImageView, itemList: Int){

        val photo = photosList[itemList]
        val builder = AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.dialog_picture_choice,null)
        val remove = view.findViewById<ImageView>(R.id.alert_remove_image)
        val gallery = view.findViewById<ImageView>(R.id.alert_gallery_image)
        builder.setView(view)
        remove.setOnClickListener {
            imageView.setImageDrawable(null)
            photosMutableList.remove(photo)
            builder.dismiss()
        }
        gallery.setOnClickListener {
            builder.dismiss()
            launchAddPhotoActivity()
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

    private fun launchAddPhotoActivity(){
        val intent = Intent(this, AddPhotoActivity::class.java)
        intent.putExtra("card_number", cardChoosed)
        Log.i("INTENT:",  "$cardChoosed is send !")
        getPhotoContent.launch(intent)
    }

    private val getPhotoContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
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

    private fun removeOldPhotoData(image: ImageView, item: Int){
        if(image.drawable != null){
            val photo = photosList[item]
            photosMutableList.remove(photo)
        }
    }

    private fun handleSaveButton(){

    }

}