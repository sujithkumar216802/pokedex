package com.sujithkumar.pokedex.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.sujithkumar.pokedex.FavouritePokemon;
import com.sujithkumar.pokedex.Json;
import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.adapter.RecycleAdapter;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.model.NameandUrlList;
import com.sujithkumar.pokedex.model.pokemon.PokemonData;
import com.sujithkumar.pokedex.model.pokemon.pokemonspecies;
import com.sujithkumar.pokedex.retrofit;
import com.sujithkumar.pokedex.viewmodel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pokemon extends Fragment {
    static RecycleAdapter adapter;
    static RecyclerView recycler;
    static GridLayoutManager layout;
    static ArrayList<String> spritelink = new ArrayList<>();
    static boolean isloading = false, search = false;
    static ArrayList<PokemonData> pokemonData = new ArrayList<>();
    static ArrayList<NameandUrl> all = new ArrayList<>();
    static String Searchstring;
    final PokemonData[] favourite = {new PokemonData()};
    ProgressBar load;
    viewmodel rep;
    NameandUrlList name = new NameandUrlList();
    ArrayList<NameandUrl> evolutionlist = new ArrayList<>();
    retrofit api;
    int visibleitemcount = 0;
    int totalitemcount = 0;
    int pastvisibleitemcount = 0;
    Json json;
    Target target;
    ArrayList<NameandUrl> searchname = new ArrayList<>();
    ArrayList<String> searchsprite = new ArrayList<>();
    SwipeRefreshLayout refresh;

    static boolean isNumeric(final String string) {

        return string.chars().allMatch(Character::isDigit);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pokemon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rep = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(viewmodel.class);
        recycler = view.findViewById(R.id.recycler);
        layout = new GridLayoutManager(requireContext(), 1);
        recycler.setLayoutManager(layout);
        load = view.findViewById(R.id.progressBar);
        refresh = view.findViewById(R.id.refresh);
        json = retrofit.getapi().create(Json.class);
        search = false;
        isloading = true;
        adapter = new RecycleAdapter(all, spritelink);
        recycler.setAdapter(adapter);
        setclicklistner(view);
        refresh.setOnRefreshListener(() -> {
            hideKeyboard();
            Navigation.findNavController(view).navigate(R.id.action_nav_pokemon_self);
        });
        init(view);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleitemcount = layout.getChildCount();
                totalitemcount = layout.getItemCount();
                pastvisibleitemcount = layout.findFirstVisibleItemPosition();

                if ((visibleitemcount + pastvisibleitemcount == totalitemcount) && (!isloading)) {
                    if (spritelink.size() != all.size() && !search) {
                        isloading = true;
                        load.setVisibility(View.VISIBLE);
                        getsprite();
                    } else if ((searchname.size() != searchsprite.size() && search)) {
                        isloading = true;
                        load.setVisibility(View.VISIBLE);
                        getsprite();
                    }

                }

            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] array = stream.toByteArray();
                        Gson gson = new Gson();
                        String ability, evolution, moves;
                        ArrayList<String> abilitylist = new ArrayList<>(), movelist = new ArrayList<>();
                        int temp = favourite[0].getAbility().size();
                        for (int i = 0; i < temp; i++) {
                            abilitylist.add(favourite[0].getAbility().get(i).getAbility().getName());
                        }
                        temp = favourite[0].getMovess().size();
                        for (int i = 0; i < temp; i++) {
                            movelist.add(favourite[0].getMovess().get(i).getMove().getName());
                        }
                        ability = gson.toJson(abilitylist);
                        evolution = gson.toJson(evolutionlist);
                        moves = gson.toJson(movelist);
                        FavouritePokemon tempp = new FavouritePokemon(evolution, ability, moves, favourite[0].getId(), favourite[0].getName(), favourite[0].getHeight(), favourite[0].getWeight(), favourite[0].getBase_experience(), favourite[0].getStatss().get(0).getBase_stat(), favourite[0].getStatss().get(1).getBase_stat(), favourite[0].getStatss().get(2).getBase_stat(), favourite[0].getStatss().get(3).getBase_stat(), favourite[0].getStatss().get(4).getBase_stat(), favourite[0].getStatss().get(5).getBase_stat(), array);
                        rep.Insert(tempp);
                        Snackbar.make(requireView(), favourite[0].getName() + " Has Been Added To Favourites", BaseTransientBottomBar.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                };


                evolutionlist.clear();
                Call<PokemonData> pokemonDataCall;


                if (!search)
                    pokemonDataCall = json.getpokemoncall(all.get(viewHolder.getAdapterPosition()).getUrl());
                else
                    pokemonDataCall = json.getpokemoncall(searchname.get(viewHolder.getAdapterPosition()).getUrl());

                pokemonDataCall.enqueue(new Callback<PokemonData>() {
                    @Override
                    public void onResponse(Call<PokemonData> call, Response<PokemonData> response) {
                        if (!response.isSuccessful()) {
                            Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                                    .show();
                            load.setVisibility(View.GONE);
                            isloading = false;
                            return;
                        }

                        favourite[0] = response.body();
                        NameandUrl tempnamee = new NameandUrl();
                        tempnamee.setName(favourite[0].getName());
                        evolutionlist.add(tempnamee);
                        Call<pokemonspecies> evouel = json.getpokemonspecies(favourite[0].getSpecies().getUrl());
                        evolutioncall(evouel);


                    }

                    @Override
                    public void onFailure(Call<PokemonData> call, Throwable t) {
                        Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                        load.setVisibility(View.GONE);
                        isloading = false;
                    }
                });


            }
        }).attachToRecyclerView(recycler);
    }


    void init(View view) {
        if (all.size() == 0) {
            Call<NameandUrlList> getallpokemon = json.getpokemonlistall();
            getallpokemon.enqueue(new Callback<NameandUrlList>() {
                @Override
                public void onResponse(Call<NameandUrlList> call, Response<NameandUrlList> response) {
                    if (!response.isSuccessful()) {
                        Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                        load.setVisibility(View.GONE);
                        isloading = false;
                        return;
                    }

                    assert response.body() != null;
                    all.addAll(response.body().getResults());
                    adapter.change(all,spritelink);
                    getsprite();
                }

                @Override
                public void onFailure(Call<NameandUrlList> call, Throwable t) {
                    Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                    load.setVisibility(View.GONE);
                    isloading = false;
                }
            });
        } else {

            adapter.notifyDataSetChanged();
            isloading = false;
            load.setVisibility(View.GONE);
        }
        refresh.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchitem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchitem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideKeyboard();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search = true;
                if (newText == null || newText.length() == 0) {
                    search = false;
                    isloading=false;
                    load.setVisibility(View.GONE);
                    Searchstring="";
                    adapter.change(all, spritelink);
                } else {
                    isloading = true;
                    load.setVisibility(View.VISIBLE);
                    searchname = new ArrayList<>();
                    searchsprite = new ArrayList<>();
                    adapter.change(searchname, searchsprite);
                    Searchstring = newText;
                    if (!isNumeric(newText)) {
                        searchfunction();
                    } else {
                        searchid(Searchstring);
                    }
                }
                return false;
            }
        });
        searchView.setQueryHint("Name / ID");
    }

    private void getsprite() {
        if (!search) {
            List<Observable<PokemonData>> requests = new ArrayList<>();
            int tempp = spritelink.size();
            for (int i = tempp; i < Math.min(tempp + 20, all.size()); i++) {
                requests.add(json.getpokemon(all.get(i).getUrl()));
            }

            Observable.zip(requests, objects -> {
                for (Object object : objects) {
                    PokemonData temp = (PokemonData) object;
                    pokemonData.add(temp);
                    spritelink.add(temp.getSpritess());
                }

                requireActivity().runOnUiThread(() -> {
                    adapter.change(all, spritelink);
                    load.setVisibility(View.GONE);
                    isloading = false;
                });
                return new Object();
            }).subscribe(new Observer<Object>() {

                @Override
                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                }

                @Override
                public void onNext(@io.reactivex.rxjava3.annotations.NonNull Object o) {

                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                        load.setVisibility(View.GONE);
                        isloading = false;
                    });
                }

                @Override
                public void onComplete() {

                }
            });


        } else {
            List<Observable<PokemonData>> requests = new ArrayList<>();
            int tempp = searchsprite.size();
            for (int i = tempp; i < Math.min(tempp + 20, searchname.size()); i++) {
                requests.add(json.getpokemon(searchname.get(i).getUrl()));
            }

            if (requests.size() > 0)
                temp(Searchstring, requests);
            else {
                isloading = false;
                load.setVisibility(View.GONE);
                Snackbar.make(requireView(), "Nothing left to load", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }


    }

    private void setclicklistner(View view) {
        adapter.setclicker((pos, imageView, getnaem) -> {
            rep.setCurrentpokemonlink(getnaem.getUrl());
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(imageView, getnaem.getName())
                    .build();
            Call<PokemonData> temp = json.getpokemoncall(getnaem.getUrl());

            temp.enqueue(new Callback<PokemonData>() {
                @Override
                public void onResponse(Call<PokemonData> call, Response<PokemonData> response) {
                    if (!response.isSuccessful()) {
                        Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                        load.setVisibility(View.GONE);
                        isloading = false;
                        return;
                    }
                    rep.setCurrentpokemon(response.body());
                    hideKeyboard();
                    Navigation.findNavController(view).navigate(R.id.pokemonDataPage,
                            null,
                            null,
                            extras);
                }

                @Override
                public void onFailure(Call<PokemonData> call, Throwable t) {
                    Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                    load.setVisibility(View.GONE);
                    isloading = false;
                }
            });
        });


    }

    void temp(String constraint, List<Observable<PokemonData>> requests) {

        Observable.zip(requests, objects -> {
            if (constraint.equals(Searchstring)) {
                for (Object object : objects) {
                    PokemonData temp = (PokemonData) object;
                    searchsprite.add(temp.getSpritess());
                }
                requireActivity().runOnUiThread(() -> {
                    adapter.change(searchname, searchsprite);
                    load.setVisibility(View.GONE);
                    isloading = false;
                });
            }

            return new Object();
        }).subscribe(new Observer<Object>() {

            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Object o) {

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                    load.setVisibility(View.GONE);
                    isloading = false;
                });
            }

            @Override
            public void onComplete() {

            }
        });


    }

    void evolutioncall(Call<pokemonspecies> evouel) {
        evouel.enqueue(new retrofit2.Callback<pokemonspecies>() {
            @Override
            public void onResponse(Call<pokemonspecies> call, Response<pokemonspecies> response) {
                if (!response.isSuccessful()) {
                    Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                    load.setVisibility(View.GONE);
                    isloading = false;
                    return;
                }

                if (response.body().getEvolves_from_species() != null) {
                    evolutionlist.add(response.body().getEvolves_from_species());
                    Call<pokemonspecies> temp = json.getpokemonspecies(response.body().getEvolves_from_species().getUrl());
                    evolutioncall(temp);
                } else {
                    Collections.reverse(evolutionlist);

                    if (favourite[0].getSpritess() != null)
                        Picasso.get().load(favourite[0].getSpritess()).into(target);
                    else {
                        Gson gson = new Gson();
                        String ability, evolution, moves;
                        ArrayList<String> abilitylist = new ArrayList<>(), movelist = new ArrayList<>();
                        int temp = favourite[0].getAbility().size();
                        for (int i = 0; i < temp; i++) {
                            abilitylist.add(favourite[0].getAbility().get(i).getAbility().getName());
                        }
                        temp = favourite[0].getMovess().size();
                        for (int i = 0; i < temp; i++) {
                            movelist.add(favourite[0].getMovess().get(i).getMove().getName());
                        }
                        ability = gson.toJson(abilitylist);
                        evolution = gson.toJson(evolutionlist);
                        moves = gson.toJson(movelist);
                        byte[] array = new byte[0];
                        FavouritePokemon tempp = new FavouritePokemon(evolution, ability, moves, favourite[0].getId(), favourite[0].getName(), favourite[0].getHeight(), favourite[0].getWeight(), favourite[0].getBase_experience(), favourite[0].getStatss().get(0).getBase_stat(), favourite[0].getStatss().get(1).getBase_stat(), favourite[0].getStatss().get(2).getBase_stat(), favourite[0].getStatss().get(3).getBase_stat(), favourite[0].getStatss().get(4).getBase_stat(), favourite[0].getStatss().get(5).getBase_stat(), array);
                        rep.Insert(tempp);
                        Snackbar.make(requireView(), favourite[0].getName() + " Has Been Added To Favourites", BaseTransientBottomBar.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }
                    //evolutionadapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<pokemonspecies> call, Throwable t) {
                Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                        .show();
                load.setVisibility(View.GONE);
                isloading = false;
            }
        });
    }

    private void searchfunction() {
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getName().toLowerCase().contains(Searchstring.toLowerCase())) {
                searchname.add(all.get(i));
                if (spritelink.size() > i) {
                    searchsprite.add(spritelink.get(i));
                }
            }
        }
        adapter.change(searchname, searchsprite);
        isloading = true;
        load.setVisibility(View.VISIBLE);
        getsprite();
    }

    private void searchid(String id) {
        int i = Integer.parseInt(id);
        Call<PokemonData> pokemonbyid = json.getpokemonbyid(i);
        pokemonbyid.enqueue(new Callback<PokemonData>() {
            @Override
            public void onResponse(Call<PokemonData> call, Response<PokemonData> response) {
                if (!response.isSuccessful()) {
                    Snackbar.make(requireView(), "ID not available", BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                    load.setVisibility(View.GONE);
                    isloading = false;
                    return;
                }
                if (id.equals(Searchstring)) {
                    NameandUrl temp = new NameandUrl();
                    assert response.body() != null;
                    temp.setName(response.body().getName());
                    temp.setUrl(response.raw().request().url().toString());
                    searchsprite.add(response.body().getSpritess());
                    searchname.add(temp);
                    adapter.change(searchname, searchsprite);
                    load.setVisibility(View.GONE);
                    isloading = false;
                }
            }

            @Override
            public void onFailure(Call<PokemonData> call, Throwable t) {
                Snackbar.make(requireView(), "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                        .show();
                load.setVisibility(View.GONE);
                isloading = false;
            }
        });

    }


    public void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


}
