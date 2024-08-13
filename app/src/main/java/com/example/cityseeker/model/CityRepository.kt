package com.example.cityseeker.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

/*
 * We use the Singleton pattern to efficiently load and store the city data once.
 * This avoids reloading and parsing the large JSON file every time we perform a search,
 * which improves performance.
 *
 * We use the `suspend` keyword for the `findCity` function to handle search operations asynchronously.
 * This ensures that the search is performed in the background, keeping the app responsive.
 *
 * Initially, we explored algorithms like Trie nodes and binary search. Tries are efficient for prefix searches,
 * but they required reloading data frequently. Binary search was chosen for its efficiency with sorted data,
 * and it's used after sorting the data.
 *
 * In summary, the Singleton pattern reduces data loading time, and `suspend` functions ensure smooth,
 * non-blocking search operations.
 */

object CityRepository {

    private fun readJsonFile(context: Context , fileName: String): String {
        context.assets.open(fileName).use { inputStream ->
            return InputStreamReader(inputStream).readText()
        }
    }

    private suspend fun parseCities(context: Context , fileName: String): List<CityData> {
        val json = readJsonFile(context , fileName)
        val cityType = object : TypeToken<List<CityData>>() {}.type
        val cities: List<CityData> = Gson().fromJson(json , cityType)
        return cities.sortedBy { it.name }
    }

    suspend fun  getCities(context: Context , fileName: String): List<CityData> {
        return parseCities(context , fileName)
    }

    suspend fun findCity(context: Context , fileName: String , prefix: String): List<CityData> {
        return withContext(Dispatchers.IO) {
            val cities = parseCities(context, fileName)
            cities.filter { it.name.startsWith(prefix, ignoreCase = true) }
        }
    }

}
