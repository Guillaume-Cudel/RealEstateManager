package com.guillaume.project9.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.guillaume.project9.R
import com.guillaume.project9.databinding.ActivityMapsBinding
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Property
import com.guillaume.project9.utils.PermissionUtils
import com.guillaume.project9.utils.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.guillaume.project9.utils.PermissionUtils.isPermissionGranted
import com.guillaume.project9.viewmodel.PropertyViewModel

class MapsActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var permissionDenied = false
    private var propertyList: List<Property> = listOf()
    private var locationList: List<LatLng>? = listOf()
    private val propertyVM: PropertyViewModel by viewModels {
        PropertyViewModelFactory((this.application as PropertysApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mapToolbar)
        binding.mapToolbar.title = "@string/app_name"



        propertyVM.allPropertys.observe(this, Observer {
            propertyList = it
            val addressList = getAddressList(propertyList)
            locationList = getLocationListFromAddress(addressList)
            addPropertysToMaps()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val add = menu.findItem(R.id.action_bar_add_property)
        add.isVisible = false
        val map = menu.findItem(R.id.action_bar_map)
        map.isVisible = false
        val search = menu.findItem(R.id.action_bar_search_property)
        search.isVisible = false

        return true
    }

    /*@RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_bar_add_property -> startActivity(Intent(this, AddPropertyActivity::class.java))
            R.id.action_bar_map -> verifyNetwork()
        }
        return super.onOptionsItemSelected(item)
    }*/


    private fun addPropertysToMaps() {

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            map = googleMap
            enableMyLocation()
            val bounds = LatLngBounds.builder()
            locationList?.forEach {
                val boundsLatLng = LatLng(it.latitude, it.longitude)
                bounds.include(boundsLatLng)
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
            addMarkers(googleMap)

            googleMap.uiSettings.isZoomControlsEnabled = true
            //todo check how to click on marker
            googleMap.setOnMarkerClickListener(this)


        }
    }

    private fun getAddressList(list: List<Property>): List<String> {
        /*var fullAddress: String?
        val addressList: MutableSet<String> = mutableSetOf()*/
        /*for (item in list) {
            fullAddress = "${item.address} ${item.cityAddress}, France"
            addressList.add(fullAddress)
        }*/
        return list.map {item ->
            "${item.address} ${item.cityAddress}, France"
        }
        //return addressList
    }

    private fun getLocationListFromAddress(addressList: List<String>): List<LatLng>? {
        /*addressList.map {
            convertAddressToLocation(it)
        }.filter { it != null }*/

        var location: LatLng?
        val locationList: MutableList<LatLng> = mutableListOf()
        for (item in addressList) {
            location = convertAddressToLocation(item)
            if (location != null) {
                locationList.add(location)
                updateProperty(item, location)
            }
        }
        return if (locationList.isNotEmpty()) locationList.toList() else null
    }

    private fun convertAddressToLocation(address: String): LatLng? {
        val addressList: List<Address>
        val geoCoder = Geocoder(this)

        try {
            addressList = geoCoder.getFromLocationName(address, 1)
            if (addressList.isNotEmpty()) run {
                val singleAddress: Address = addressList[0]
                return LatLng(singleAddress.latitude, singleAddress.longitude)
            } else {
                return null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun updateProperty(address: String, location: LatLng) {
        for (item in propertyList) {
            if(item.location == null || item.location == "") {
                val fullAddress = "${item.address} ${item.cityAddress}, France"
                val lat = location.latitude
                val lng = location.longitude
                val stringLocation = "${lat.toString()}+${lng.toString()}"
                if (fullAddress == address) {
                    val updateProperty = Property(
                        item.propertyId,
                        item.kind,
                        item.price,
                        item.surface,
                        item.rooms,
                        item.description,
                        item.photo,
                        item.address,
                        item.postalCode,
                        item.cityAddress,
                        item.sold,
                        item.launchOrSellDate,
                        item.agent,
                        stringLocation
                    )
                    propertyVM.updateProperty(updateProperty)
                }
            }
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {
        propertyList.forEach { property ->
            if (property.location != null) {
                val latlng = property.location!!.split("+")
                val location = LatLng(latlng[0].toDouble(), latlng[1].toDouble())

                val marker = googleMap.addMarker(
                    MarkerOptions()
                        //.title(place.name)
                        .position(location)
                )
                marker?.tag = property
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            //addPropertysToMaps()
            return
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(LOCATION_PERMISSION_REQUEST_CODE, true)
                .show(supportFragmentManager, "dialog")
            return
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            enableMyLocation()
        } else {
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val propertyMarker = p0.tag as Property

        //val intent = Intent(this, MainActivity::class.java)
        /*val bundle = Bundle()
        bundle.putSerializable("property", propertyMarker)
        intent.putExtras(bundle)*/



        val bundle = Bundle()
        bundle.putSerializable("property", propertyMarker)

        val transaction = supportFragmentManager.beginTransaction()
        val fragment2 = PropertyDetailFragment()
        fragment2.arguments = bundle

        transaction.replace(R.id.map_fragment_frame_layout, fragment2)
        transaction.addToBackStack(null)
        transaction.commit()
        //finish()
        //startActivity(intent)


        return false
    }


    /*val bundle = Bundle()
    bundle.putSerializable("property", property)

    val transaction = this.parentFragmentManager.beginTransaction()
    val fragment2 = PropertyDetailFragment()
    fragment2.arguments = bundle

    transaction.replace(R.id.propertyListFragment, fragment2)
    transaction.addToBackStack(null)
    transaction.commit()*/






}