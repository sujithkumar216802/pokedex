package com.sujithkumar.pokedex.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationAreaModel {

    @SerializedName("pokemon_encounters")
    ArrayList<PokemonEncounters> pokemonEncounters;

    public ArrayList<PokemonEncounters> getPokemonEncounters() {
        return pokemonEncounters;
    }
}
