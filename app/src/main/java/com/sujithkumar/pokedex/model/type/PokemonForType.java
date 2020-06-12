package com.sujithkumar.pokedex.model.type;

import com.google.gson.annotations.SerializedName;
import com.sujithkumar.pokedex.model.NameandUrl;

public class PokemonForType {
    public NameandUrl getPokemon() {
        return pokemon;
    }

    public void setPokemon(NameandUrl pokemon) {
        this.pokemon = pokemon;
    }

    int slot;

    @SerializedName("pokemon")
    NameandUrl pokemon;

}
