package com.sujithkumar.pokedex.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationModel {

    @SerializedName("areas")
    ArrayList<NameandUrl> areas;

    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }

    public ArrayList<NameandUrl> getAreas() {
        return areas;
    }
}
