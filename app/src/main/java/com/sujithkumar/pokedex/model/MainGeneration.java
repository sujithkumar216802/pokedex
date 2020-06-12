package com.sujithkumar.pokedex.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainGeneration {


    @SerializedName("pokemon_species")
    ArrayList<NameandUrl> pokemon_species;


    public ArrayList<NameandUrl> getPokemon_species() {
        return pokemon_species;
    }
}
