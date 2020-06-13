package com.sujithkumar.pokedex.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.transition.TransitionInflater;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sujithkumar.pokedex.FavouritePokemon;
import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.adapter.EvolutionAdapter;
import com.sujithkumar.pokedex.adapter.FavouriteNameOnlyAdapter;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FavouritePokemonDataPage extends Fragment {

    RecyclerView ability, move, evolution;
    EvolutionAdapter evolutionadapter;
    FavouriteNameOnlyAdapter abilityadapter, moveadapter;
    LinearLayoutManager abilitylayout, movelayout, evolutionlayout;
    viewmodel rep;
    FavouritePokemon favouritePokemon;
    TextView name, height, weight, base_experience, hp, attack, defence, specialattack, specialdefence, speed, type, notavailable, id;
    ImageView imageView;
    ArrayList<String> abilitylist = new ArrayList<>();
    ArrayList<String> movelist = new ArrayList<>();
    ArrayList<NameandUrl> evolutionlist = new ArrayList<>();
    ImageButton Share;


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
    }


    void initiate(View view) {
        id = view.findViewById(R.id.ID);
        notavailable = view.findViewById(R.id.notavailable);
        name = view.findViewById(R.id.name);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);
        base_experience = view.findViewById(R.id.base_experience);
        hp = view.findViewById(R.id.hp);
        attack = view.findViewById(R.id.attack);
        defence = view.findViewById(R.id.defence);
        Share = view.findViewById(R.id.share);
        specialattack = view.findViewById(R.id.special_attack);
        specialdefence = view.findViewById(R.id.special_defence);
        speed = view.findViewById(R.id.speed);
        type = view.findViewById(R.id.type);
        imageView = view.findViewById(R.id.image);
        ability = view.findViewById(R.id.ability);
        move = view.findViewById(R.id.move);
        evolution = view.findViewById(R.id.evolution);
        rep = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(viewmodel.class);
        favouritePokemon = rep.getCurrentfavouritepokemon();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.sharedelementtransition));
        imageView.setTransitionName(favouritePokemon.getName());
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(favouritePokemon.getImage(), 0, favouritePokemon.getImage().length));
        if (favouritePokemon.getImage().length == 0)
            notavailable.setVisibility(View.VISIBLE);
        startPostponedEnterTransition();
        id.setText("ID : " + favouritePokemon.getId());
        name.setText("Name : " + favouritePokemon.getName());
        height.setText("Height : " + favouritePokemon.getHeight());
        weight.setText("Weight : " + favouritePokemon.getWeight());
        base_experience.setText("Base Experience : " + favouritePokemon.getBaseexperience());
        hp.setText("HP : " + favouritePokemon.getHp());
        attack.setText("Attack : " + favouritePokemon.getAttack());
        defence.setText("Defence : " + favouritePokemon.getDefence());
        specialattack.setText("Special Attack : " + favouritePokemon.getSpecialattack());
        specialdefence.setText("Special Defence : " + favouritePokemon.getSpecialdefence());
        speed.setText("Speed : " + favouritePokemon.getSpeed());


        Gson gson = new Gson();

        java.lang.reflect.Type stringlist = new TypeToken<ArrayList<String>>() {
        }.getType();

        abilitylist = gson.fromJson(favouritePokemon.getAbilities(), stringlist);
        movelist = gson.fromJson(favouritePokemon.getMoves(), stringlist);
        stringlist = new TypeToken<ArrayList<NameandUrl>>() {
        }.getType();
        evolutionlist = gson.fromJson(favouritePokemon.getEvolution(), stringlist);


        abilityadapter = new FavouriteNameOnlyAdapter(abilitylist);
        abilitylayout = new LinearLayoutManager(requireContext());
        ability.setAdapter(abilityadapter);
        ability.setLayoutManager(abilitylayout);
        moveadapter = new FavouriteNameOnlyAdapter(movelist);
        movelayout = new LinearLayoutManager(requireContext());
        move.setAdapter(moveadapter);
        move.setLayoutManager(movelayout);
        evolutionadapter = new EvolutionAdapter(evolutionlist);
        evolutionlayout = new LinearLayoutManager(requireContext());
        evolution.setAdapter(evolutionadapter);
        evolution.setLayoutManager(evolutionlayout);


        Share.setOnClickListener(v -> {

            if (favouritePokemon.getImage().length != 0) {
                Intent shareintent = new Intent(Intent.ACTION_SEND);
                final File[] file = new File[1];
                final FileOutputStream[] fout = new FileOutputStream[1];
                Bitmap bitmap = BitmapFactory.decodeByteArray(favouritePokemon.getImage(), 0, favouritePokemon.getImage().length);
                try {
                    file[0] = new File(requireActivity().getCacheDir(), "pokemon.png");
                    fout[0] = new FileOutputStream(file[0]);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout[0]);
                    fout[0].flush();
                    fout[0].close();
                    file[0].setReadable(true, false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", file[0]);
                shareintent.putExtra(Intent.EXTRA_STREAM, photoURI);
                shareintent.setType("img/png");
                shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareintent.putExtra(Intent.EXTRA_TEXT, favouritePokemon.getName());
                startActivity(Intent.createChooser(shareintent, "Share pokemon Via "));
            } else {
                Intent shareintent = new Intent(Intent.ACTION_SEND);
                shareintent.setType("text/plain");
                shareintent.putExtra(Intent.EXTRA_TEXT, favouritePokemon.getName());
                startActivity(Intent.createChooser(shareintent, "Share pokemon Via "));
            }


        });


    }


}
