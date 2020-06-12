package com.sujithkumar.pokedex.model.pokemon;


import com.google.gson.annotations.SerializedName;
import com.sujithkumar.pokedex.model.NameandUrl;

import java.util.ArrayList;

public class evolutionchain {

    @SerializedName("chain")
    Chain ChainObject;
    private float id;

    public Chain getChain() {
        return ChainObject;
    }

    public float getId() {
        return id;
    }


}


