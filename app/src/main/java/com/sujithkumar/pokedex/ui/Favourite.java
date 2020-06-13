package com.sujithkumar.pokedex.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sujithkumar.pokedex.FavouritePokemon;
import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.adapter.FavouriteAdapter;
import com.sujithkumar.pokedex.viewmodel;

import java.util.ArrayList;

public class Favourite extends Fragment {

    static FavouriteAdapter adapter;
    static RecyclerView recycler;
    static GridLayoutManager layout;
    viewmodel rep;
    ProgressBar load;
    ArrayList<FavouritePokemon> favlist;
    String Searchstring;

    ArrayList<FavouritePokemon> searchlist = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<Bitmap> images = new ArrayList<>();
    ArrayList<String> searchname = new ArrayList<>();
    ArrayList<Bitmap> searchimages = new ArrayList<>();

    boolean search;
    boolean isloading;

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
        return inflater.inflate(R.layout.favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rep = new ViewModelProvider(requireActivity()).get(viewmodel.class);
        recycler = view.findViewById(R.id.recycler);
        layout = new GridLayoutManager(requireContext(), 1);
        recycler.setLayoutManager(layout);
        load = view.findViewById(R.id.progressBar);
        adapter = new FavouriteAdapter(name, images);
        search = false;
        isloading = true;
        adapter.setclicker((pos, imageView) -> {
            FragmentNavigator.Extras extras;
            if (!search) {
                rep.setCurrentfavouritepokemon(favlist.get(pos));
                extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(imageView, favlist.get(pos).getName())
                        .build();
            } else {
                rep.setCurrentfavouritepokemon(searchlist.get(pos));
                extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(imageView, searchlist.get(pos).getName())
                        .build();
            }
            hideKeyboard();
            Navigation.findNavController(view).navigate(R.id.favouritePokemonDataPage,
                    null,
                    null,
                    extras);
        });

        recycler.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (!search) {
                    Snackbar.make(requireView(), favlist.get(viewHolder.getAdapterPosition()).getName() + " Has been removed from favourites ", BaseTransientBottomBar.LENGTH_LONG).show();
                    rep.delete(favlist.get(viewHolder.getAdapterPosition()));
                } else {
                    Snackbar.make(requireView(), searchlist.get(viewHolder.getAdapterPosition()).getName() + " as been removed from favourites ", BaseTransientBottomBar.LENGTH_LONG).show();
                    rep.delete(searchlist.get(viewHolder.getAdapterPosition()));
                }
            }
        }).attachToRecyclerView(recycler);


        rep.getFavourite().observe(requireActivity(), favouritePokemons -> {
            favlist = new ArrayList<>(favouritePokemons);
            name = new ArrayList();
            images = new ArrayList<>();
            for (int i = 0; i < favouritePokemons.size(); i++) {
                int tttt = favouritePokemons.get(i).getImage().length;
                Bitmap bitmap = BitmapFactory.decodeByteArray(favouritePokemons.get(i).getImage(), 0, tttt);
                images.add(bitmap);
                name.add(favouritePokemons.get(i).getName());
            }
            if (search)
                search();
            else
                adapter.change(name, images);
            load.setVisibility(View.GONE);
            isloading = false;
        });
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search = true;
                if (newText == null || newText.length() == 0) {
                    search = false;
                    isloading = false;
                    load.setVisibility(View.GONE);
                    Searchstring = "";
                    adapter.change(name, images);
                } else {
                    searchlist = new ArrayList<>();
                    searchname = new ArrayList<>();
                    searchimages = new ArrayList<>();
                    Searchstring = newText;
                    if (!isNumeric(newText)) {
                        search();
                    } else {
                        searchid();
                    }
                }
                return false;
            }
        });
        searchView.setQueryHint("Name /ID");

    }

    void search() {
        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).toLowerCase().contains(Searchstring.toLowerCase())) {
                searchname.add(name.get(i));
                searchimages.add(images.get(i));
                searchlist.add(favlist.get(i));
            }
        }
        adapter.change(searchname, searchimages);
    }

    void searchid() {
        int id = Integer.parseInt(Searchstring);
        for (int i = 0; i < favlist.size(); i++) {
            if (favlist.get(i).getId() == id) {
                searchname.add(name.get(i));
                searchimages.add(images.get(i));
                searchlist.add(favlist.get(i));
                break;
            }
        }
        adapter.change(searchname, searchimages);

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
