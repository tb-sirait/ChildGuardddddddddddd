package com.example.gpstracker.server

data class put_ortu
    (val status: String,
     val message: String,
     val `data`: List<put_ortu>)
