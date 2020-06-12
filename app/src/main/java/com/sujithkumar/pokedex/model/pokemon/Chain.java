package com.sujithkumar.pokedex.model.pokemon;

import com.google.gson.annotations.SerializedName;
import com.sujithkumar.pokedex.model.NameandUrl;

import java.util.ArrayList;

public class Chain {

    @SerializedName("evolves_to")
    ArrayList<Chain> evolves_to;
    @SerializedName("species")
    NameandUrl species;
    private boolean is_baby;


    public NameandUrl getSpecies() {
        return species;
    }

    public ArrayList<Chain> getEvolves_to() {
        return evolves_to;
    }
}
