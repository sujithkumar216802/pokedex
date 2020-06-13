package com.sujithkumar.pokedex.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionInflater;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.sujithkumar.pokedex.Json;
import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.adapter.EvolutionAdapter;
import com.sujithkumar.pokedex.adapter.OnlyNameAdapter;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.model.pokemon.PokemonData;
import com.sujithkumar.pokedex.model.pokemon.pokemonspecies;
import com.sujithkumar.pokedex.retrofit;
import com.sujithkumar.pokedex.viewmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Response;


public class PokemonDataPage extends Fragment {


    RecyclerView ability, move, evolution;
    EvolutionAdapter evolutionadapter;
    OnlyNameAdapter abilityadapter, moveadapter;
    LinearLayoutManager abilitylayout, movelayout, evolutionlayout;
    viewmodel rep;
    PokemonData pokemonData;
    TextView name, height, weight, base_experience, hp, attack, defence, specialattack, specialdefence, speed, type, notavailable, id;
    ImageView imageView;
    Json json;
    ArrayList<NameandUrl> abilitylist = new ArrayList<>();
    ArrayList<NameandUrl> movelist = new ArrayList<>();
    ArrayList<NameandUrl> evolutionlist = new ArrayList<>();
    ImageButton Share;
    SwipeRefreshLayout refresh;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pokemoninfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiate(view);
        refresh.setOnRefreshListener(() -> {
            Call<pokemonspecies> e = json.getpokemonspecies(pokemonData.getSpecies().getUrl());
            evolutioncall(e);
        });
    }


    void abilityandmovesetup() {
        int temp = pokemonData.getAbility().size();
        for (int i = 0; i < temp; i++) {
            abilitylist.add(pokemonData.getAbility().get(i).getAbility());
        }
        temp = pokemonData.getMovess().size();
        for (int i = 0; i < temp; i++) {
            movelist.add(pokemonData.getMovess().get(i).getMove());
        }
    }


    void initiate(View view) {
        json = retrofit.getapi().create(Json.class);
        id = view.findViewById(R.id.ID);
        refresh = view.findViewById(R.id.refresh);
        notavailable = view.findViewById(R.id.notavailable);
        name = view.findViewById(R.id.name);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);
        base_experience = view.findViewById(R.id.base_experience);
        hp = view.findViewById(R.id.hp);
        attack = view.findViewById(R.id.attack);
        Share = view.findViewById(R.id.share);
        defence = view.findViewById(R.id.defence);
        specialattack = view.findViewById(R.id.special_attack);
        specialdefence = view.findViewById(R.id.special_defence);
        speed = view.findViewById(R.id.speed);
        type = view.findViewById(R.id.type);
        imageView = view.findViewById(R.id.image);
        ability = view.findViewById(R.id.ability);
        move = view.findViewById(R.id.move);
        evolution = view.findViewById(R.id.evolution);
        rep = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(viewmodel.class);


        if (pokemonData != null) {
            if (rep.getCurrentpokemon() != pokemonData) {
                pokemonData = rep.getCurrentpokemon();
                NameandUrl temp = new NameandUrl();
                temp.setName(pokemonData.getName());
                evolutionlist.add(temp);
                Call<pokemonspecies> evouel = json.getpokemonspecies(pokemonData.getSpecies().getUrl());
                evolutioncall(evouel);
                abilityandmovesetup();
            }
        } else {
            pokemonData = rep.getCurrentpokemon();
            NameandUrl temp = new NameandUrl();
            temp.setName(pokemonData.getName());
            evolutionlist.add(temp);
            Call<pokemonspecies> evouel = json.getpokemonspecies(pokemonData.getSpecies().getUrl());
            evolutioncall(evouel);
            abilityandmovesetup();
        }


        pokemonData = rep.getCurrentpokemon();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.sharedelementtransition));
        imageView.setTransitionName(pokemonData.getName());
        if (pokemonData.getSpritess() != null)
            Picasso.get().load(pokemonData.getSpritess()).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    startPostponedEnterTransition();
                }

                @Override
                public void onError(Exception e) {
                    Snackbar.make(requireView(), "Network issue , image cannot be loaded", BaseTransientBottomBar.LENGTH_LONG).show();
                    startPostponedEnterTransition();
                }
            });
        else {
            notavailable.setVisibility(View.VISIBLE);
            startPostponedEnterTransition();
        }


        id.setText("ID : " + pokemonData.getId());
        name.setText("Name : " + pokemonData.getName());
        height.setText("Height : " + pokemonData.getHeight());
        weight.setText("Weight : " + pokemonData.getWeight());
        base_experience.setText("Base Experience : " + pokemonData.getBase_experience());
        hp.setText("HP : " + pokemonData.getStatss().get(0).getBase_stat());
        attack.setText("Attack : " + pokemonData.getStatss().get(1).getBase_stat());
        defence.setText("Defence : " + pokemonData.getStatss().get(2).getBase_stat());
        specialattack.setText("Special Attack : " + pokemonData.getStatss().get(3).getBase_stat());
        specialdefence.setText("Special Defence : " + pokemonData.getStatss().get(4).getBase_stat());
        speed.setText("Speed : " + pokemonData.getStatss().get(5).getBase_stat());
        abilityadapter = new OnlyNameAdapter(abilitylist);
        abilitylayout = new LinearLayoutManager(requireContext());
        ability.setAdapter(abilityadapter);
        ability.setLayoutManager(abilitylayout);
        moveadapter = new OnlyNameAdapter(movelist);
        movelayout = new LinearLayoutManager(requireContext());
        move.setAdapter(moveadapter);
        move.setLayoutManager(movelayout);
        evolutionadapter = new EvolutionAdapter(evolutionlist);
        evolutionlayout = new LinearLayoutManager(requireContext());
        evolution.setAdapter(evolutionadapter);
        evolution.setLayoutManager(evolutionlayout);

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final File[] file = new File[1];
                final FileOutputStream[] fout = new FileOutputStream[1];
                Intent shareintent = new Intent(Intent.ACTION_SEND);
                Picasso.get().load(pokemonData.getSpritess()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            file[0] = new File(requireActivity().getCacheDir(), "pokemon.png");
                            fout[0] = new FileOutputStream(file[0]);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout[0]);
                            fout[0].flush();
                            fout[0].close();
                            file[0].setReadable(true, false);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Snackbar.make(requireView(), "Network issue , image cannot be loaded", BaseTransientBottomBar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

                if (pokemonData.getSpritess() != null) {
                    Uri photoURI = FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", file[0]);
                    shareintent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    shareintent.setType("img/png");
                } else
                    shareintent.setType("text/plain");

                shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareintent.putExtra(Intent.EXTRA_TEXT, rep.getCurrentpokemonlink());
                startActivity(Intent.createChooser(shareintent, "Share pokemon " + pokemonData.getName()));
            }


        });

    }

    void evolutioncall(Call<pokemonspecies> evouel) {
        evouel.enqueue(new retrofit2.Callback<pokemonspecies>() {
            @Override
            public void onResponse(Call<pokemonspecies> call, Response<pokemonspecies> response) {
                if (!response.isSuccessful()) {
                    Snackbar.make(requireView(), "Network issue , evolution cannot be loaded", BaseTransientBottomBar.LENGTH_LONG).show();
                    refresh.setRefreshing(false);
                    return;
                }

                if (response.body().getEvolves_from_species() != null) {
                    evolutionlist.add(response.body().getEvolves_from_species());
                    Call<pokemonspecies> temp = json.getpokemonspecies(response.body().getEvolves_from_species().getUrl());
                    evolutioncall(temp);
                } else {
                    Collections.reverse(evolutionlist);
                    evolutionadapter.notifyDataSetChanged();
                }
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<pokemonspecies> call, Throwable t) {
                Snackbar.make(requireView(), "Network issue , evolution cannot be loaded", BaseTransientBottomBar.LENGTH_LONG).show();
                refresh.setRefreshing(false);
            }
        });
    }


}
