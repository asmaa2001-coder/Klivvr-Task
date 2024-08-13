package com.example.cityseeker.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.cityseeker.model.CityData
import com.example.cityseeker.model.CityRepository
import com.example.cityseeker.view.CityViewer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CityPresenterImpl(
    private val context: Context ,
    private var cityViewer: CityViewer? ,
    private val cityRepo: CityRepository,
    private val fileName:String
) : CityPresenter {

    override suspend fun loadCities() {
        cityViewer?.showLoading()
        val cities = withContext(Dispatchers.IO) {
            cityRepo.getCities(context , fileName)
        }
        if (cities.isNotEmpty()) {
            cityViewer?.displayCities(cities)
        } else {
            cityViewer?.showError("No cities to display")
        }
        cityViewer?.hideLoading()
    }


    override fun onClickedAction(city: CityData) {

        val uri =
            Uri.parse("geo:${city.coord?.lat},${city.coord?.lon}?q=${city.coord?.lat},${city.coord?.lon}(${city.name})")

        val intent = Intent(Intent.ACTION_VIEW , uri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context , "Google Maps is not installed. Please install it to view the map." , Toast.LENGTH_SHORT).show()

            val playStoreIntent = Intent(
                Intent.ACTION_VIEW ,
                Uri.parse("market://details?id=com.google.android.apps.maps")
            )
            context.startActivity(playStoreIntent)
        }

    }

    override fun onDestroy() {
        cityViewer = null
    }
}