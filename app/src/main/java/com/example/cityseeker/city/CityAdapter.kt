package com.example.cityseeker.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cityseeker.R

class CityAdapter(
    var locations: List<CityData> , private val onItemClick: (CityData) -> Unit

) :
    RecyclerView.Adapter<CityAdapter.CityViewHelper>() {
    var itemOnClick: ((CityData) -> Unit)? = null

    class CityViewHelper(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.city_title)
        val countryName: TextView = itemView.findViewById(R.id.country_title)
        val lonValue: TextView = itemView.findViewById(R.id.lon_title)
        val latValue: TextView = itemView.findViewById(R.id.lat_title)



    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): CityViewHelper {
        return CityViewHelper(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_item_content , parent , false)
        )
    }

    override fun getItemCount() = locations.size

    fun updateData(newCities: List<CityData>) {
        this.locations = newCities
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CityViewHelper , position: Int) {
        val city = locations[position]
        holder.cityName.text = city.name
        holder.countryName.text = city.country
        holder.latValue.text = "Lat : ${city.coord?.lat.toString()}"
        holder.lonValue.text = "Lon : ${city.coord?.lon.toString()}"
        holder.itemView.setOnClickListener { onItemClick(city) }
   }
}