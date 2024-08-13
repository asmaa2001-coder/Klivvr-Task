package com.example.cityseeker.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cityseeker.model.CityData
import com.example.cityseeker.model.CityRepository
import com.example.cityseeker.databinding.FragmentCityFilterBinding
import com.example.cityseeker.presenter.CityPresenter
import com.example.cityseeker.presenter.CityPresenterImpl
import kotlinx.coroutines.launch


class CityFilter : Fragment() , CityViewer {
    val gsonName = "cities.json"
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CityAdapter
    private lateinit var cityPresenter: CityPresenter
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    lateinit var progressBar: ProgressBar
    private lateinit var cities: List<CityData>
    private var _binding: FragmentCityFilterBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityFilterBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        progressBar = _binding?.progressBar!!
        cityPresenter = CityPresenterImpl(requireActivity() , this , CityRepository , gsonName)

        lifecycleScope.launch {
            cities = CityRepository.getCities(requireActivity() , gsonName)
        }

        setupRecycler()
        setupSearchFilter()

    }


    private fun setupSearchFilter() {
        searchView = _binding?.search!!
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    (binding.cities.adapter as CityAdapter).updateData(
                        emptyList()
                    )
                } else {
                    lifecycleScope.launch {
                        try {
                            showLoading()
                            val filteredCities =
                                CityRepository.findCity(requireActivity() , gsonName , newText)
                            (binding.cities.adapter as CityAdapter).updateData(
                                filteredCities
                            )
                            hideLoading()
                        } catch (e: Exception) {
                            Log.e("Error" , "${e.message}")
                        }
                        hideLoading()
                    }
                }
                return true
            }
        })
    }


    private fun setupRecycler() {
        recycler = _binding?.cities!!
        recycler.layoutManager = LinearLayoutManager(requireActivity())
        adapter = CityAdapter(emptyList()) { city ->
            cityPresenter.onClickedAction(city)
        }
        recycler.adapter = adapter
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

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun displayCities(cities: List<CityData>) {
        adapter.updateData(cities)
    }

    override fun showError(error: String) {
        Toast.makeText(requireActivity() , error , Toast.LENGTH_SHORT).show()
    }
}
