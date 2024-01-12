package com.example.gpstracker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
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
import okhttp3.OkHttpClient
import retrofit2.Retrofit

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

    // Take all data with passing from API using Bearer Token by Retrofit Library
    object RetrofitClient {
        private const val BASE_URL = "http://127.0.0.1:8000/parents"

        private val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val apiService: APIService = retrofit.create(APIService::class.java)
    }

    // Main Process of Managing data from API to Map Visualization
//    private fun makeApiCall() {
//        val call = RetrofitClient.apiService.getData("73ob73y64nt3n653k4l1")
//        call.enqueue(object : Callback<ApiResponse> {
//            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    if (data != null) {
//                        // Handle the response data
//                        val yourDataList = data.data
//
//                        for (item in yourDataList) {
//                            val name = item.name
//                            val date = item.date
//                            val lat = item.lat
//                            val lon = item.lon
//                            val heading = item.heading.toFloat()
//                            val calcspeed = item.calcspeed
//                            val imo = item.IMO
//                            val mmsi = item.MMSI
//
//                            Log.d("API Response", "Data: $data")
//                            // Create a LatLng object using the latitude and longitude
//                            val location = LatLng(lat, lon)
//
//                            // Marker ini khusus untuk mengetahui lokasi, nama kapal, dan arah kapal melaju menggunakan custom marker
//                            setCustomMarkerIcon(location, name, heading, calcspeed, date, imo, mmsi)
//                        }
//                    }
//                } else {
//                    // Handle unsuccessful response (e.g., non-2xx status codes)
//                    val errorResponseCode = response.code() // HTTP status code
//                    val errorMessage = response.errorBody()?.string()
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                // Handle failure
//                // This method is called when the API call fails, for example, due to a network issue.
//                val errorServer = ErrorServer()
//                childFragmentManager.beginTransaction()
//                    .replace(R.id.map, errorServer)
//                    .addToBackStack(null) // Optional, adds the fragment to the back stack
//                    .commit()
//            }
//        })
//    }

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