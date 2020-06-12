package com.sujithkumar.pokedex.model.pokemon;

import com.google.gson.annotations.SerializedName;
import com.sujithkumar.pokedex.model.NameandUrl;

public class abilities {
    @SerializedName("ability")
    NameandUrl ability;
    boolean is_hidden;
    Integer slot;

    public NameandUrl getAbility() {
        return ability;
    }
}

