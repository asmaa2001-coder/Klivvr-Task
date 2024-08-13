package com.example.cityseeker.presenter

import com.example.cityseeker.model.CityData

interface CityPresenter {
    suspend fun loadCities()
    fun onClickedAction(city:CityData)
    fun onDestroy()
}