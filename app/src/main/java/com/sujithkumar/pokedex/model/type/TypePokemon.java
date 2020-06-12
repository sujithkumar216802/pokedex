package com.sujithkumar.pokedex.model.type;

import com.google.gson.annotations.SerializedName;
import com.sujithkumar.pokedex.model.type.PokemonForType;

import java.util.ArrayList;

public class TypePokemon {


    @SerializedName("pokemon")
    ArrayList<PokemonForType> pokemon;

    public ArrayList<PokemonForType> getPokemon() {
        return pokemon;
    }

    public void setPokemon(ArrayList<PokemonForType> pokemon) {
        this.pokemon = pokemon;
    }
}
