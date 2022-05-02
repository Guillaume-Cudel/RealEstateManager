package com.guillaume.project9.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.bumptech.glide.Glide
import com.guillaume.project9.databinding.ActivityAddPhotoBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotoBinding
    private var photoFile: File? = null
    private var photoUri: Uri? = null
    private var mCurrentPhotoPath: String? = null
    private var cardChoosed = 100

    companion object {
        private const val CAMERA_PERMISSION_CODE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val actionBar = actionBar
        //actionBar?.setDisplayHomeAsUpEnabled(true)


        val otherBundle = getIntent().extras
        cardChoosed = otherBundle!!.getInt("card_number")

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
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                photoFile = createImageFile()
                if(photoFile != null){
                    val photoURI = FileProvider.getUriForFile(this,
                        "com.guillaume.project9.fileprovider", photoFile!!)

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    Log.i("URI", photoURI.toString())
                    Log.i("Photo_string", mCurrentPhotoPath.toString())
                pickPhoto.launch(intent)
                }
            }

            binding.addPhotoGalleryImage.setOnClickListener {
                //val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                photoFile = createImageFile()
                if(photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.guillaume.project9.fileprovider", photoFile!!
                    )

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    Log.i("URI", photoURI.toString())
                    //galleryResult.launch(i)
                loadImage.launch("image/*")
                //todo check it
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    val loadImage = registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
        //binding.addPhotoImage.setImageURI(it)
        //photoFile = File(it.path!!)
        //val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
        //val bitmap: Bitmap = this.contentResolver.loadThumbnail(it, Size(200, 200), null)

        //todo recove and convert the image to file

        Glide.with(this)
            .load(it)
            .centerCrop()
            .into(binding.addPhotoImage)
    })

    /*val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null){
            val bundle = it.data!!.extras?.get("data")
            val uri: Uri = bundle as Uri
            val bitmap: Bitmap = bundle as Bitmap

            Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(binding.addPhotoImage)

        }


    }*/


    var pickPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)

            //val bundle = it.data!!.extras?.get("data")
            //val uri: Uri = it.data!!.extras?.get(MediaStore.EXTRA_OUTPUT) as Uri

                Glide.with(this)
                    .load(myBitmap)
                    .centerCrop()
                    .into(binding.addPhotoImage)
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

    private fun onClickValidateButton(){
        binding.addPhotoValidateButton.setOnClickListener {
            val description = binding.addPhotoDescriptionEdit.text.toString()
            Log.i("PHOTO_DESCRIPTION", description)

            val intent = Intent()
            intent.putExtra("card_number", cardChoosed)
            intent.putExtra("photo_file", photoFile)
            intent.putExtra("photo_description", description)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    /*fun convertImageUriToFile(imageUri: Uri, activity: Activity): File? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(
                //MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                //MediaStore.Images.ImageColumns.ORIENTATION
            )
            //cursor = activity.managedQuery(imageUri, proj, null, null, null)

            this.contentResolver.query(imageUri, proj, null, null, null)
                ?.use {
                    cursor ->
                    while (cursor.moveToNext()){

                    }
                }

            val file_ColumnIndex: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val orientation_ColumnIndex: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION)
            if (cursor.moveToFirst()) {
                val orientation: String = cursor
                    .getString(orientation_ColumnIndex)
                return File(cursor.getString(file_ColumnIndex))
            }
            null
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }*/
}