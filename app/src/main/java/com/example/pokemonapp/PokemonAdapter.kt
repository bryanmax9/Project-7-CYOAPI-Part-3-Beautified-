package com.example.pokemonapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val pokemonList: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonImage: ImageView = view.findViewById(R.id.pokemon_image)
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
        val pokemonDetails: TextView = view.findViewById(R.id.pokemon_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = pokemonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.pokemonName.text = pokemon.name
        holder.pokemonDetails.text = "Happiness: ${pokemon.baseHappiness}, Capture Rate: ${pokemon.captureRate}, Hatch Counter: ${pokemon.hatchCounter}"

        Glide.with(holder.itemView.context)
            .load(pokemon.imageUrl)
            .circleCrop() // This will make the image circular
            .into(holder.pokemonImage)
    }
}
