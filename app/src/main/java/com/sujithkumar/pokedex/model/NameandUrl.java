package com.sujithkumar.pokedex.model;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.GET;

public class NameandUrl {
    @SerializedName("name")
    String name;
    @SerializedName("url")
    String url;

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
