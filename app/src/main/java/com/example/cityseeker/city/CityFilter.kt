package com.example.cityseeker.city

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cityseeker.R
import kotlinx.coroutines.launch


class CityFilter : Fragment() {
    val gsonName = "cities.json"
    private lateinit var recycler: RecyclerView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var cities: List<CityData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_filter , container , false)
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        recycler = view.findViewById(R.id.cities)
        searchView = view.findViewById(R.id.search)
        cities = HandleDateOfJson.getCities(requireActivity() , gsonName)

        setupRecycler()
        setupSearchFilter()

    }


    private fun setupSearchFilter() {
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    (recycler.adapter as CityAdapter).updateData(emptyList())
                } else {
                    // Show progress bar while loading data

                    lifecycleScope.launch {
                        try {
                            val filteredCities =
                                HandleDateOfJson.findCity(requireActivity() , gsonName , newText)
                            (recycler.adapter as CityAdapter).updateData(filteredCities)
                        } catch (e: Exception) {
                            Log.e("Error" , "${e.message}")
                        }
                    }
                }
                return true
            }
        })
    }


    private fun setupRecycler() {
        recycler.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CityAdapter(emptyList()) { cityData ->
            openMap(cityData)
        }
        recycler.adapter = adapter
    }

    private fun openMap(city: CityData) {
        val uri =
            Uri.parse("geo:${city.coord?.lat},${city.coord?.lon}?q=${city.coord?.lat},${city.coord?.lon}(${city.name})")
        val intent = Intent(Intent.ACTION_VIEW , uri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(
                requireActivity() ,
                "Google Maps is not installed. Please install it to view the map." ,
                Toast.LENGTH_SHORT
            ).show()
            val playStoreIntent = Intent(
                Intent.ACTION_VIEW ,
                Uri.parse("market://details?id=com.google.android.apps.maps")
            )
            startActivity(playStoreIntent)
        }
    }

    //save data
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("DATA" , searchView.query.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getString("DATA")
    }
}
