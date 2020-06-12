package com.sujithkumar.pokedex.ui;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sujithkumar.pokedex.Json;
import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.adapter.OnlyNameAdapter;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.model.NameandUrlList;
import com.sujithkumar.pokedex.recyclerclicklistner;
import com.sujithkumar.pokedex.retrofit;
import com.sujithkumar.pokedex.viewmodel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Type extends Fragment {
    static ArrayList<NameandUrl> searchname = new ArrayList<>();
    NameandUrlList name = new NameandUrlList();
    OnlyNameAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager layout;
    viewmodel rep;
    ArrayList<NameandUrl> typelist = new ArrayList<>();
    ProgressBar load;
    boolean search, isloading;
    String Searchstring;

    static boolean isNumeric(final String string) {

        return string.chars().allMatch(Character::isDigit);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.recycler);
        layout = new LinearLayoutManager(requireContext());
        recycler.setLayoutManager(layout);
        adapter = new OnlyNameAdapter(typelist);
        recycler.setAdapter(adapter);
        load = view.findViewById(R.id.progressBar);
        rep = new ViewModelProvider(requireActivity()).get(viewmodel.class);
        adapter.setclicker((pos, imageView, getname) -> {
            rep.setCurrenttype(getname.getUrl());
            rep.setCurrenttypename(getname.getName());
            hideKeyboard();
            Navigation.findNavController(view).navigate(R.id.pokemonTypelist,
                    null,
                    null);
        });
        search = false;
        isloading = true;
        Json json = retrofit.getapi().create(Json.class);

        Call<NameandUrlList> typeelist = json.gettypelist();

        if (typelist.size() == 0) {
            typeelist.enqueue(new Callback<NameandUrlList>() {
                @Override
                public void onResponse(Call<NameandUrlList> call, Response<NameandUrlList> response) {
                    if (!response.isSuccessful()) {
                        Snackbar.make(view, "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                        load.setVisibility(View.GONE);
                        isloading = false;
                        return;
                    }

                    name = response.body();
                    assert name != null;
                    typelist.addAll(new ArrayList<>(name.getResults()));
                    adapter.notifyDataSetChanged();
                    load.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<NameandUrlList> call, Throwable t) {
                    Snackbar.make(view, "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                    load.setVisibility(View.GONE);
                    isloading = false;
                }
            });
        } else {
            load.setVisibility(View.GONE);
        }


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
                    adapter.change(typelist);
                } else {
                    searchname = new ArrayList<>();
                    adapter.change(searchname);
                    if (!isNumeric(newText)) {
                        Searchstring = newText;
                        searchfunction();
                    } else {
                        Searchstring = newText;
                        searchid(Searchstring);
                    }
                }
                return false;
            }
        });

        searchView.setQueryHint("Name / ID");
    }

    private void searchfunction() {
        for (int i = 0; i < typelist.size(); i++) {
            if (typelist.get(i).getName().toLowerCase().contains(Searchstring.toLowerCase())) {
                searchname.add(typelist.get(i));
            }
        }
        adapter.change(searchname);
    }


    private void searchid(String id) {
        int i = Integer.parseInt(id);
        if (i <= typelist.size() && i > 0)
            searchname.add(typelist.get(i - 1));
        adapter.change(searchname);

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
