package com.example.mvvmproject.GoogleMapsLocations

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mvvmproject.databinding.ActivityMaps2Binding
import com.google.android.gms.maps.model.Marker

import androidx.core.widget.doOnTextChanged
import com.example.mvvmproject.R
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.stone.vega.library.VegaLayoutManager


class FindLocations : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps2Binding
    private lateinit var fusedlocation:FusedLocationProviderClient
    private lateinit var maps:GoogleMap
    private  lateinit var marker: Marker
    private lateinit var locationRequest: LocationRequest
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment
    lateinit var geocoder: Geocoder
     var context=this
    private lateinit var intenthandled: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        geocoder=Geocoder(this)

      //  Places.initialize(this,"AIzaSyCLmyUPoLMLvzdajIw_3AJ98k19Tll8ekg")

        val placeList:ArrayList<Place.Field>
        placeList= arrayListOf(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME)



        // Initialize the AutocompleteSupportFragment.


       /* autocompleteSupportFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
*/
        // Specify the types of place data to return.
    /*    autocompleteSupportFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("TAG", "Place: ${place.name}, ${place.id} ,${place.latLng}")
                val intent =Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,placeList).build(context)
                startActivityForResult(intent,102)
            }

            override fun onError(status: Status) {
                Log.i("TAG", "An error occurred: $status")
            }
        })
*/
        var list: ArrayList<Address>
        list= arrayListOf()


        val vega= VegaLayoutManager()
        vega.canScrollHorizontally()
        binding.recyclerview.layoutManager=vega

        binding.searchplaceeditext.doOnTextChanged { text, start, before, count ->
            binding.recyclerview.visibility=View.VISIBLE
            fusedlocation.removeLocationUpdates(locationCallback1)
                if (count<=0)
                {
                    binding.recyclerview.visibility=View.GONE
                    list.clear()
                    val adapter = LocationRecyclerAdapter(this, list)
                    binding.recyclerview.adapter = adapter
                }
               else {
                    val location =
                        geocoder.getFromLocationName(text.toString(), 1) as ArrayList<Address>
                    list.addAll(location)

                    val adapter = LocationRecyclerAdapter(this, list)
                    binding.recyclerview.adapter = adapter
                }
        }



        binding.searchplaceeditext.setOnClickListener {
      fusedlocation.removeLocationUpdates(locationCallback1)
            val listtemp=geocoder.getFromLocationName(binding.searchplaceeditext.text.toString().toString(), 1) as ArrayList<Address>
           binding.recyclerview.visibility= View.GONE
            val latlang=LatLng(listtemp.get(0).latitude,listtemp.get(0).longitude)
            marker.position=latlang
           maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang,14f))
        }








       /* binding.searchplaceeditext.setOnClickListener {


        }*/


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        fusedlocation= LocationServices.getFusedLocationProviderClient(this)
        locationRequest= LocationRequest.create()
        locationRequest.interval=0
        locationRequest.fastestInterval=10
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY


        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),100)
        }
        fusedlocation.requestLocationUpdates(locationRequest,locationCallback1, Looper.getMainLooper())

    }

    var locationCallback1: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            setLocations(locationResult.lastLocation)
            super.onLocationResult(locationResult)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        maps=googleMap
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        marker= mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))!!
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        maps.uiSettings.isZoomControlsEnabled=true
        maps.uiSettings.isTiltGesturesEnabled=true
        maps.uiSettings.isMyLocationButtonEnabled=true
        maps.uiSettings.isZoomGesturesEnabled=true
        maps.uiSettings.isRotateGesturesEnabled=true
    }


    fun setLocations(location: Location)
    {
        val latLng=LatLng(location.latitude,location.longitude)
        marker.position=latLng
        maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f))


    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode==100)
        {
            Toast.makeText(this,"granted",Toast.LENGTH_LONG).show()
        }else
        {
            Toast.makeText(this,"denied!",Toast.LENGTH_LONG).show()
        }




        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==102 && resultCode== RESULT_OK)
        {
        Toast.makeText(this,"in getting locations",Toast.LENGTH_LONG).show()
        val lists=Autocomplete.getPlaceFromIntent(data)
           val ob=lists.latLng
            val latlang=LatLng(ob.latitude,ob.longitude)
                marker.position=latlang
                autocompleteSupportFragment.setText(lists.name.toString())


        }

        super.onActivityResult(requestCode, resultCode, data)
    }



}