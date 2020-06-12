package com.sujithkumar.pokedex;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sujithkumar.pokedex.model.item.itemmodel;
import com.sujithkumar.pokedex.model.pokemon.PokemonData;

import java.util.List;

public class viewmodel extends AndroidViewModel {

    String currenttype, currenttypename;
    String currentpokemonlink;
    Repo repo;
    PokemonData currentpokemon;
    itemmodel currentitem;
    LiveData<List<FavouritePokemon>> favourite;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    String region;

    public String getCurrentpokemonlink() {
        return currentpokemonlink;
    }

    public void setCurrentpokemonlink(String currentpokemonlink) {
        this.currentpokemonlink = currentpokemonlink;
    }


    public FavouritePokemon getCurrentfavouritepokemon() {
        return currentfavouritepokemon;
    }

    public void setCurrentfavouritepokemon(FavouritePokemon currentfavouritepokemon) {
        this.currentfavouritepokemon = currentfavouritepokemon;
    }

    FavouritePokemon currentfavouritepokemon;

    public viewmodel(@NonNull Application application) {
        super(application);
        repo = new Repo(application);
        favourite = repo.getAllFavourite();
    }

    public itemmodel getCurrentitem() {
        return currentitem;
    }

    public void setCurrentitem(itemmodel currentitem) {
        this.currentitem = currentitem;
    }

    public PokemonData getCurrentpokemon() {
        return currentpokemon;
    }

    public void setCurrentpokemon(PokemonData currentpokemon) {
        this.currentpokemon = currentpokemon;
    }

    public void Insert(FavouritePokemon obj) {
        repo.insert(obj);
    }

    public void delete(FavouritePokemon obj) {
        repo.delete(obj);
    }

    public void deleteall() {
        repo.deleteallfavourite();
    }

    public LiveData<List<FavouritePokemon>> getFavourite() {
        return favourite;
    }

    public String getCurrenttype() {
        return currenttype;
    }

    public void setCurrenttype(String currenttype) {
        this.currenttype = currenttype;
    }

    public String getCurrenttypename() {
        return currenttypename;
    }

    public void setCurrenttypename(String currenttypename) {
        this.currenttypename = currenttypename;
    }
}
