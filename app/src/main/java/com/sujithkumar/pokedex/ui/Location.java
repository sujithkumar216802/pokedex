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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sujithkumar.pokedex.Json;
import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.adapter.OnlyNameAdapter;
import com.sujithkumar.pokedex.model.LocationModel;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.model.NameandUrlList;
import com.sujithkumar.pokedex.retrofit;
import com.sujithkumar.pokedex.viewmodel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Location extends Fragment {

    static OnlyNameAdapter adapter;
    static RecyclerView recycler;
    static GridLayoutManager layout;
    static boolean isloading = false, search = false;
    static ArrayList<NameandUrl> all = new ArrayList<>();
    static String Searchstring;
    ProgressBar load;
    viewmodel rep;
    NameandUrlList name = new NameandUrlList();
    retrofit api;
    int visibleitemcount = 0;
    int totalitemcount = 0;
    int pastvisibleitemcount = 0;
    Json json;
    ArrayList<NameandUrl> searchname = new ArrayList<>();
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
        return inflater.inflate(R.layout.location, container, false);
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
        adapter = new OnlyNameAdapter(all);
        recycler.setAdapter(adapter);
        json = retrofit.getapi().create(Json.class);
        search = false;
        refresh.setOnRefreshListener(() -> {
            hideKeyboard();
            Navigation.findNavController(view).navigate(R.id.action_nav_location_self);
        });

        init(view);



/*
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
                        getsprite();
                        isloading = true;
                        load.setVisibility(View.VISIBLE);
                    } else if (searchname.size() != searchsprite.size() && search) {
                        getsprite();
                        isloading = true;
                        load.setVisibility(View.VISIBLE);
                    }

                }

            }
        });

*/


    }

    void init(View view) {
        if (all.size() == 0) {
            Call<NameandUrlList> getalllocation = json.getlocationlistall();
            getalllocation.enqueue(new Callback<NameandUrlList>() {
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
                    adapter.change(all);
                    isloading = false;
                    load.setVisibility(View.GONE);
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
                    isloading = false;
                    load.setVisibility(View.GONE);
                    Searchstring = "";
                    adapter.change(all);
                } else {
                    isloading = true;
                    load.setVisibility(View.VISIBLE);
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
        searchname = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getName().toLowerCase().contains(Searchstring.toLowerCase())) {
                searchname.add(all.get(i));
            }
        }
        isloading = false;
        load.setVisibility(View.GONE);
        adapter.change(searchname);
        if (searchname.size() == 0) {
            Snackbar.make(requireView(), "Not Available", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    private void searchid(String id) {
        int i = Integer.parseInt(id);
        Call<LocationModel> locationid = json.getlocationbyid(i);
        locationid.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
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
                    searchname.add(temp);
                    adapter.change(searchname);
                    load.setVisibility(View.GONE);
                    isloading = false;
                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Snackbar.make(requireView(), "Network Issue", BaseTransientBottomBar.LENGTH_LONG)
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

