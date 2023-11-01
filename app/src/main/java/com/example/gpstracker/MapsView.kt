package com.example.gpstracker

import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsView : Fragment() {

    private lateinit var mMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        val uper = LatLng(-6.228241, 106.788967)
        setCustomMarkerIcon(uper, "UniversitasPertamina")
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(uper))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    // Apply Marker and Marker Option on Map
    private fun setCustomMarkerIcon(location: LatLng, name: String) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title(name)

        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.icon_anak)
        )

        markerOptions.icon(bitmapDescriptor)
        // Kemudian tambahkan marker ke peta
         mMap.addMarker(markerOptions)
    }
}