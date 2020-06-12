package com.sujithkumar.pokedex.model.pokemon;

import com.google.gson.annotations.SerializedName;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.model.PokemonEncounters;
import com.sujithkumar.pokedex.model.pokemon.evolutionurl;

import java.util.ArrayList;

public class pokemonspecies {
    public evolutionurl getObj() {
        return obj;
    }


    public ArrayList<PokemonEncounters> getVarieties() {
        return varieties;
    }

    @SerializedName("evolution_chain")
    evolutionurl obj;

    @SerializedName("varieties")
    ArrayList<PokemonEncounters> varieties;


    public NameandUrl getEvolves_from_species() {
        return evolves_from_species;
    }

    @SerializedName("evolves_from_species")
    NameandUrl evolves_from_species;

}

