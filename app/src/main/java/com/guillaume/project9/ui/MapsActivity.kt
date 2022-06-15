package com.guillaume.project9.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.guillaume.project9.R
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Property
import com.guillaume.project9.utils.PermissionUtils
import com.guillaume.project9.utils.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.guillaume.project9.utils.PermissionUtils.isPermissionGranted
import com.guillaume.project9.viewmodel.PropertyViewModel

class MapsActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

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
        setContentView(R.layout.activity_maps)


        propertyVM.allPropertys.observe(this, Observer {
            propertyList = it
            val addressList = getAddressList(propertyList)
            locationList = getLocationListFromAddress(addressList)
            addPropertysToMaps()
        })

    }


    private fun addPropertysToMaps(){

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            map = googleMap
            enableMyLocation()
            val bounds = LatLngBounds.builder()
            locationList?.forEach {
                val boundsLatLng = LatLng(it.latitude, it.longitude)
                bounds.include(boundsLatLng )
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
            addMarkers(googleMap)

            googleMap.uiSettings.isZoomControlsEnabled = true
            /*googleMap.setOnMarkerClickListener {

            }*/

        }
    }

    private fun getAddressList(list: List<Property>): MutableSet<String> {
        var fullAddress: String?
        val addressList: MutableSet<String> = mutableSetOf()
        for(item in list){
            fullAddress = "${item.address} ${item.cityAddress}, France"
            addressList.add(fullAddress)
        }
        return addressList
    }

    private fun getLocationListFromAddress(addressList: MutableSet<String>): List<LatLng>? {
        var location: LatLng?
        var locationList: MutableSet<LatLng> = mutableSetOf()
        for(item in addressList){
            location = convertAddressToLocation(item)
            if (location != null) {
                locationList.add(location)
            }
        }
        return if(locationList.isNotEmpty()) locationList.toList() else null
    }


    private fun convertAddressToLocation(address: String): LatLng? {
        val addressList: List<Address>
        val geoCoder = Geocoder(this)

        try {
            addressList = geoCoder.getFromLocationName(address, 1)
            if(addressList.isNotEmpty()) run {
                val singleAddress: Address = addressList[0]
                return LatLng(singleAddress.latitude, singleAddress.longitude)
            } else{
                return null
            }

        } catch(e: Exception){
            e.printStackTrace()
            return null
        }
    }


    private fun addMarkers(googleMap: GoogleMap) {
        //todo find how get good property on click marker, save LAtlng ?
         locationList?.forEach { location ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    //.title(place.name)

                    .position(location)
            )
        }
    }


    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            //addPropertysToMaps()
            return
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION
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



}