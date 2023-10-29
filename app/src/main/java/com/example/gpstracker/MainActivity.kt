package com.example.gpstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment: Fragment = MapsView()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.maps_container, fragment)
            .commit()

    }
}