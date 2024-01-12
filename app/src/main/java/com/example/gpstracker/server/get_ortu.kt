package com.example.gpstracker.server

data class get_ortu(
    val status: String,
    val message: String,
    val `data`: List<get_ortuItem>
)