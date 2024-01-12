package com.example.gpstracker.server

data class get_ortuItem(
    val email: String,
    val id_parent: Int,
    val koordinat_lattitude: Double,
    val koordinat_longtitude: Double,
    val username: String
)