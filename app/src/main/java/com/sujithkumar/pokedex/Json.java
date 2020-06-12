package com.sujithkumar.pokedex;

import com.sujithkumar.pokedex.model.LocationAreaModel;
import com.sujithkumar.pokedex.model.LocationModel;
import com.sujithkumar.pokedex.model.MainGeneration;
import com.sujithkumar.pokedex.model.NameandUrlList;
import com.sujithkumar.pokedex.model.RegionModel;
import com.sujithkumar.pokedex.model.item.itemmodel;
import com.sujithkumar.pokedex.model.pokemon.PokemonData;
import com.sujithkumar.pokedex.model.pokemon.evolutionchain;
import com.sujithkumar.pokedex.model.pokemon.pokemonspecies;
import com.sujithkumar.pokedex.model.type.TypePokemon;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Json {


    @GET
    Observable<PokemonData> getpokemon(@Url String url);

    @GET("pokemon/{id}")
    Call<PokemonData> getpokemonbyid(@Path("id") int id);

    @GET
    Call<PokemonData> getpokemoncall(@Url String url);

    @GET
    Observable<itemmodel> getitem(@Url String url);

    @GET("item/{id}")
    Call<itemmodel> getitembyid(@Path("id") int id);

    @GET("pokemon?limit=9999")
    Call<NameandUrlList> getpokemonlistall();

    @GET("item?limit=9999")
    Call<NameandUrlList> getitemlistall();


    @GET("location?limit=9999")
    Call<NameandUrlList> getlocationlistall();

    @GET("region")
    Call<NameandUrlList> getregionlist();



    @GET("region/{name}")
    Call<RegionModel> getregion(@Path("name") String name);

    @GET
    Call<MainGeneration> getgeneration(@Url String url);



    @GET("location/{id}")
    Call<LocationModel> getlocationbyid(@Path("id") int id);



    @GET("type")
    Call<NameandUrlList> gettypelist();


    @GET
    Call<TypePokemon> gettype(@Url String url);


    @GET
    Call<pokemonspecies> getpokemonspecies(@Url String url);


}
