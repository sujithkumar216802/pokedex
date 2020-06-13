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
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sujithkumar.pokedex.Json;
import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.adapter.OnlyNameAdapter;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.model.NameandUrlList;
import com.sujithkumar.pokedex.retrofit;
import com.sujithkumar.pokedex.viewmodel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Region extends Fragment {
    static ArrayList<NameandUrl> searchname = new ArrayList<>();
    NameandUrlList name = new NameandUrlList();
    OnlyNameAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager layout;
    ArrayList<NameandUrl> regionlist = new ArrayList<>();
    ProgressBar load;
    viewmodel rep;
    boolean search, isloading;
    String Searchstring;
    SwipeRefreshLayout refresh;
    Json json;

    static boolean isNumeric(final String string) {

        return string.chars().allMatch(Character::isDigit);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.region, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh = view.findViewById(R.id.refresh);
        recycler = view.findViewById(R.id.recycler);
        layout = new LinearLayoutManager(requireContext());
        recycler.setLayoutManager(layout);
        adapter = new OnlyNameAdapter(regionlist);
        recycler.setAdapter(adapter);
        load = view.findViewById(R.id.progressBar);
        rep = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(viewmodel.class);
        adapter.setclicker((pos, imageView, getname) -> {
            if (!search)
                rep.setRegion(regionlist.get(pos - 1).getName());
            else
                rep.setRegion(searchname.get(pos - 1).getName());
            hideKeyboard();
            Navigation.findNavController(view).navigate(R.id.pokemonRegionPage,
                    null,
                    null);
        });
        json = retrofit.getapi().create(Json.class);
        search = false;
        isloading = true;
        init(view);

        refresh.setOnRefreshListener(() -> {
            hideKeyboard();
            Navigation.findNavController(view).navigate(R.id.action_nav_region_self);
        });


    }


    void init(View view) {
        Call<NameandUrlList> regionnlist = json.getregionlist();
        if (regionlist.size() == 0)
            regionnlist.enqueue(new Callback<NameandUrlList>() {
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
                    regionlist.addAll(new ArrayList<>(name.getResults()));
                    adapter.notifyDataSetChanged();
                    isloading = false;
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
        else {
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
                    adapter.change(regionlist);
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
        for (int i = 0; i < regionlist.size(); i++) {
            if (regionlist.get(i).getName().toLowerCase().contains(Searchstring.toLowerCase())) {
                searchname.add(regionlist.get(i));
            }
        }
        adapter.change(searchname);
        if (searchname.size()==0){
            Snackbar.make(requireView(),"Not Available",BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }


    private void searchid(String id) {
        int i = Integer.parseInt(id);
        if (i <= regionlist.size() && i > 0)
            searchname.add(regionlist.get(i - 1));
        else
            Snackbar.make(requireView(),"ID Not Available",BaseTransientBottomBar.LENGTH_LONG).show();
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





































