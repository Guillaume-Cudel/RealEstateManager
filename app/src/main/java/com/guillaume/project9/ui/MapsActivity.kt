package com.guillaume.project9.ui

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.guillaume.project9.R
import com.guillaume.project9.di.PropertyViewModelFactory
import com.guillaume.project9.di.PropertysApplication
import com.guillaume.project9.model.Property
import com.guillaume.project9.viewmodel.PropertyViewModel

class MapsActivity : AppCompatActivity() {

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
            val bounds = LatLngBounds.builder()
            locationList?.forEach {
                val boundsLatLng = LatLng(it.latitude, it.longitude)
                bounds.include(boundsLatLng )
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
            addMarkers(googleMap)

            googleMap.uiSettings.isZoomControlsEnabled = true
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
         locationList?.forEach { location ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    //.title(place.name)
                    .position(location)
            )
        }
    }

}