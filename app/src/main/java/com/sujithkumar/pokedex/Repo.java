package com.sujithkumar.pokedex;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repo {
    FavouritePokemonDao dao;
    LiveData<List<FavouritePokemon>> favouritepokemon;

    public Repo(Application application) {
        FavouritePokemonDatabase database = FavouritePokemonDatabase.getInstance(application);
        dao = database.favouritePokemonDao();
        favouritepokemon = dao.getallfavouritepokwmon();
    }

    void insert(FavouritePokemon obj) {

        new InsertAsync(dao).execute(obj);
    }

    void delete(FavouritePokemon obj) {
        new DeletetAsync(dao).execute(obj);
    }

    void deleteallfavourite() {
        new DeleteallAsync(dao).execute();

    }

    public LiveData<List<FavouritePokemon>> getAllFavourite() {
        return favouritepokemon;
    }


    private static class InsertAsync extends AsyncTask<FavouritePokemon, Void, Void> {

        private FavouritePokemonDao dao;

        InsertAsync(FavouritePokemonDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavouritePokemon... favouritePokemons) {
            dao.insert(favouritePokemons[0]);
            return null;
        }
    }


    private static class DeletetAsync extends AsyncTask<FavouritePokemon, Void, Void> {

        private FavouritePokemonDao dao;

        DeletetAsync(FavouritePokemonDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavouritePokemon... favouritePokemons) {
            dao.delete(favouritePokemons[0]);
            return null;
        }
    }


    private static class DeleteallAsync extends AsyncTask<Void, Void, Void> {

        private FavouritePokemonDao dao;

        DeleteallAsync(FavouritePokemonDao dao) {
            this.dao = dao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteall();
            return null;
        }
    }


}
