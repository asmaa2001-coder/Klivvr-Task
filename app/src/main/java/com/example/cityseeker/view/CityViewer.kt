package com.example.cityseeker.view

import com.example.cityseeker.model.CityData

interface CityViewer {
    fun showLoading()
    fun hideLoading()
    fun displayCities(cities:List<CityData>)
    fun showError(error:String)
}