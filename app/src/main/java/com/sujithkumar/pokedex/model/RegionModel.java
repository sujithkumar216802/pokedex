package com.sujithkumar.pokedex.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegionModel {

    @SerializedName("main_generation")
    NameandUrl main_generation;
    @SerializedName("locations")
    ArrayList<NameandUrl> locations;

    public NameandUrl getMain_generation() {
        return main_generation;
    }

    public ArrayList<NameandUrl> getLocations() {
        return locations;
    }
}
