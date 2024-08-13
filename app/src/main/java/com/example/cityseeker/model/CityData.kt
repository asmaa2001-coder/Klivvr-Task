package com.example.cityseeker.model

data class CityData(
    val country: String,
    val name: String,
    val _id: Int,
    val coord: Coordinate
)data class Coordinate(val lon:Double,val lat:Double)
