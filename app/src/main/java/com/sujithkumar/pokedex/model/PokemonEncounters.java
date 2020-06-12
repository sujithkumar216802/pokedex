package com.sujithkumar.pokedex.model;

import com.google.gson.annotations.SerializedName;

public class PokemonEncounters {

    public NameandUrl getPokemon() {
        return pokemon;
    }

    @SerializedName("pokemon")
    NameandUrl pokemon;

}
