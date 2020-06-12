package com.sujithkumar.pokedex;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouritePokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavouritePokemon favouritePokemon);

    @Delete
    void delete(FavouritePokemon favouritePokemon);

    @Query("DELETE FROM favourite_pokemon")
    void deleteall();

    @Query("SELECT * FROM favourite_pokemon")
    LiveData<List<FavouritePokemon>> getallfavouritepokwmon();

}
