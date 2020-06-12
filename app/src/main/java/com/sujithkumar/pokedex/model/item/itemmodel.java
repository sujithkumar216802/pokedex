package com.sujithkumar.pokedex.model.item;

import com.google.gson.annotations.SerializedName;

public class itemmodel {
    @SerializedName("sprites")
    public sprite spritee;

    @SerializedName("name")
    public String name;

    public String getName() {
        return name;
    }

    public sprite getSprite() {
        return spritee;
    }
}

