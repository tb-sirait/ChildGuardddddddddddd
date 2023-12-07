package com.example.gpstracker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil

import java.text.DecimalFormat

class MapsView : Fragment() {

    private lateinit var mMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        val uper = LatLng(-6.228241, 106.788967)
        val kranji = LatLng(-6.224860160583242, 106.9797861304309)
        val ortu = LatLng(-6.135565, 106.177429)

        // Hitung jarak menggunakan SphericalUtil
        val distance: Double = SphericalUtil.computeDistanceBetween(uper, kranji)
        val distanceOrtukeAnak1: Double = (SphericalUtil.computeDistanceBetween(ortu, uper))/1000
        val distanceOrtukeAnak2: Double = (SphericalUtil.computeDistanceBetween(ortu, kranji))/1000
        val distanceInKm = distance / 1000 // Ubah ke kilometer
        val decimalFormat = DecimalFormat("#.#")
        val roundedDistance = decimalFormat.format(distanceInKm)

        setCustomMarkerIcon(uper, "UniversitasPertamina", distanceInKm)
        setCustomMarkerIcon(kranji, "StasiunKranji", distanceInKm)
        setCustomMarkerIconOrtu(ortu, "Orang Tua",distanceOrtukeAnak1, distanceOrtukeAnak2)
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

    // Apply Marker and Marker Option on Map (anak)
    private fun setCustomMarkerIcon(location: LatLng, name: String, JarakAntarKeduanya: Double) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title(name)
            .snippet("$JarakAntarKeduanya km")

        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.icon_anak)
        )

        markerOptions.icon(bitmapDescriptor)
        mMap.addMarker(markerOptions)
    }

    // Apply Marker and Marker Option on Map (Ortu)
    private fun setCustomMarkerIconOrtu(location: LatLng, name: String, JarakKeAnak1: Double, JarakKeAnak2: Double) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title(name)
            .snippet("$JarakKeAnak1 km, $JarakKeAnak2 km")

        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.icon_ortu)
        )

        markerOptions.icon(bitmapDescriptor)
        mMap.addMarker(markerOptions)
    }

    @SuppressLint("SetTextI18n")
    private fun createCustomMarker(nama: String, jarakAntarAnak: Double): Bitmap? {
        val markerView = layoutInflater.inflate(R.layout.custom_marker, null)

        // Set text or customize the content of the marker view
        val nama = markerView.findViewById<TextView>(R.id.nama_anak)
        val jarakAntarAnak = markerView.findViewById<TextView>(R.id.alamat)

        nama.text = nama.toString()
        jarakAntarAnak.text = "Jarak antar titik: $jarakAntarAnak km"

        // Measure and layout the marker view
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)

        // Create a bitmap from the marker view
        val bitmap = Bitmap.createBitmap(
            markerView.measuredWidth,
            markerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        markerView.draw(canvas)

        return bitmap
    }
}