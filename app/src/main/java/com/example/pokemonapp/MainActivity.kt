package com.example.pokemonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray

data class Pokemon(
    val name: String,
    val imageUrl: String,
    val baseHappiness: Int,
    val captureRate: Int,
    val hatchCounter: Int
)

class MainActivity : AppCompatActivity() {
    private lateinit var rvPokemon: RecyclerView // RecyclerView for displaying Pokemon images
    private var pokemons = mutableListOf<Pokemon>() // List to store Pokemon data
    private var loadedPokemon = 0 // Counter for loaded Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPokemon = findViewById(R.id.pokemon_list)
        rvPokemon.layoutManager = LinearLayoutManager(this)

        getPokemonList()
    }

    private fun getPokemonList() {
        val client = AsyncHttpClient()
        client["https://pokeapi.co/api/v2/pokemon?limit=70", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.d("Pokemon", "JSON Response successful")
                val resultsArray = json.jsonObject.getJSONArray("results")
                for (i in 0 until resultsArray.length()) {
                    getPokemonDetails(resultsArray.getJSONObject(i).getString("url"))
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String, throwable: Throwable) {
                Log.e("Pokemon Error", "Error fetching pokemon list")
            }
        }]
    }

    private fun getPokemonDetails(url: String) {
        val client = AsyncHttpClient()
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val name = json.jsonObject.getString("name")
                val imageUrl = json.jsonObject.getJSONObject("sprites").getString("front_default")
                getSpeciesDetails(json.jsonObject.getJSONObject("species").getString("url"), name, imageUrl)
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String, throwable: Throwable) {
                Log.e("Pokemon Error", "Error fetching pokemon details for url $url")
                loadedPokemon++
                checkAllPokemonLoaded()
            }
        }]
    }

    private fun getSpeciesDetails(url: String, name: String, imageUrl: String) {
        val client = AsyncHttpClient()
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                val baseHappiness = json.jsonObject.getInt("base_happiness")
                val captureRate = json.jsonObject.getInt("capture_rate")
                val hatchCounter = json.jsonObject.getInt("hatch_counter")

                val pokemon = Pokemon(name, imageUrl, baseHappiness, captureRate, hatchCounter)
                pokemons.add(pokemon)
                loadedPokemon++
                checkAllPokemonLoaded()
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String, throwable: Throwable) {
                Log.e("Pokemon Error", "Error fetching species details for url $url")
                loadedPokemon++
                checkAllPokemonLoaded()
            }
        }]
    }

    private fun checkAllPokemonLoaded() {
        if (loadedPokemon == 70) { // Assuming you're loading 70 pokemons
            updateUIWithImages()
        }
    }

    private fun updateUIWithImages() {
        Log.d("Pokemon App", "All image URLs loaded: $pokemons")
        rvPokemon.adapter = PokemonAdapter(pokemons)
    }
}

