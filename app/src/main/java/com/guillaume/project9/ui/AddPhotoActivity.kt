package com.guillaume.project9.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.guillaume.project9.databinding.ActivityAddPhotoBinding
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotoBinding
    private var photoFile: File? = null
    private var mCurrentPhotoPath: String? = null
    private var cardChoosed = 100
    private var description: String? = null
    private var photoName: String? = null

    companion object {
        private const val CAMERA_PERMISSION_CODE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //todo set toolbar to go back
        /*val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)*/

        val otherBundle = intent.extras
        cardChoosed = otherBundle!!.getInt("card_number")
        photoName = otherBundle.getString("photo_name")
        description = otherBundle.getString("photo_description")
        if(photoName != null){
            val photoFile = File(photoName)
            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.addPhotoImage.setImageBitmap(bitmap)
            if(description != null){
                binding.addPhotoDescriptionEdit.setText(description)
            }
        }
        Log.i("INTENT_OTHER_ACTIVITY", "Data received : $cardChoosed")

        pickOrloadPhotos()
        onClickValidateButton()
    }

    private fun pickOrloadPhotos() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            binding.addPhotoCameraImage.setOnClickListener {
                takePhoto()
            }

            binding.addPhotoGalleryImage.setOnClickListener {
                selectPhotoFromGallery()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickOrloadPhotos()
            } else {
                Toast.makeText(
                    this,
                    "You don't have the permission to use camera, please grant him !",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()
        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(
                this,
                "com.guillaume.project9.fileprovider", photoFile!!
            )

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            Log.i("URI", photoURI.toString())
            Log.i("Photo_string", mCurrentPhotoPath.toString())
            pickPhoto.launch(intent)
        }
    }

    private fun selectPhotoFromGallery() {
        loadImage.launch("image/*")
    }


    private val loadImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.addPhotoImage.setImageURI(uri)
                contentResolver.openInputStream(uri)?.use {
                    photoFile = createImageFile()
                    val bytes = it.readBytes()
                    photoFile?.writeBytes(bytes)
                }
            }
        }


    private val pickPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                binding.addPhotoImage.setImageBitmap(myBitmap)
            }
        }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun onClickValidateButton() {
        binding.addPhotoValidateButton.setOnClickListener {
            description = binding.addPhotoDescriptionEdit.text.toString()
            photoName = photoFile.toString()
            Log.i("PHOTO_DESCRIPTION", description!!)

            val intent = Intent()
            intent.putExtra("card_number", cardChoosed)
            intent.putExtra("photo_name", photoName)
            intent.putExtra("photo_description", description)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}