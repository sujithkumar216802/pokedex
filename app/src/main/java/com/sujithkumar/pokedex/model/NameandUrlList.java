package com.sujithkumar.pokedex.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NameandUrlList {
    @SerializedName("results")
    ArrayList<NameandUrl> results = new ArrayList<>();
    String next = "";

    public ArrayList<NameandUrl> getResults() {
        return results;
    }

    public String getNext() {
        return next;
    }
}
