package com.sujithkumar.pokedex;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = FavouritePokemon.class , version = 3,exportSchema = false)
public abstract class  FavouritePokemonDatabase extends RoomDatabase {


    private static FavouritePokemonDatabase instance;
    public abstract FavouritePokemonDao favouritePokemonDao();

    public static synchronized FavouritePokemonDatabase getInstance(Context context){
        if(instance ==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),FavouritePokemonDatabase.class,"favourite_pokemon_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
