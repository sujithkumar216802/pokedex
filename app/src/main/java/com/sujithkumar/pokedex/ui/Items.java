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
import com.sujithkumar.pokedex.adapter.RecycleAdapter;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.model.NameandUrlList;
import com.sujithkumar.pokedex.model.item.itemmodel;
import com.sujithkumar.pokedex.retrofit;
import com.sujithkumar.pokedex.viewmodel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Items extends Fragment {

    static RecycleAdapter adapter;
    static RecyclerView recycler;
    static GridLayoutManager layout;
    static ArrayList<String> spritelink = new ArrayList<>();
    static boolean isloading = false, search = false;
    static ArrayList<itemmodel> itemData = new ArrayList<>();
    static ArrayList<NameandUrl> all = new ArrayList<>();
    static String Searchstring;
    ProgressBar load;
    viewmodel rep;
    NameandUrlList name = new NameandUrlList();
    int visibleitemcount = 0;
    int totalitemcount = 0;
    int pastvisibleitemcount = 0;
    Json json;
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
        return inflater.inflate(R.layout.items, container, false);
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
        refresh.setOnRefreshListener(() -> {
            hideKeyboard();
            Navigation.findNavController(view).navigate(R.id.action_nav_item_self);
        });
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
                    else {
                        Snackbar.make(requireView(), "END OF PAGE", BaseTransientBottomBar.LENGTH_LONG).show();
                    }

                }

            }
        });

        init(view);


    }

    void init(View view) {
        if (all.size() == 0) {
            Call<NameandUrlList> getallitems = json.getitemlistall();
            getallitems.enqueue(new Callback<NameandUrlList>() {
                @Override
                public void onResponse(Call<NameandUrlList> call, Response<NameandUrlList> response) {
                    if (!response.isSuccessful()) {
                        Snackbar.make(view, "Network Issue , Please Reload", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                        load.setVisibility(View.GONE);
                        isloading = false;
                        return;
                    }
                    assert response.body() != null;
                    all.addAll(response.body().getResults());
                    adapter.change(all, spritelink);
                    getsprite();
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
                    adapter.change(all, spritelink);
                } else {
                    isloading = true;
                    load.setVisibility(View.VISIBLE);
                    searchname = new ArrayList<>();
                    searchsprite = new ArrayList<>();
                    adapter.change(searchname, searchsprite);
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

    private void getsprite() {
        if (!search) {
            List<Observable<itemmodel>> requests = new ArrayList<>();
            int tempp = spritelink.size();
            for (int i = tempp; i < Math.min(tempp + 20, all.size()); i++) {
                requests.add(json.getitem(all.get(i).getUrl()));
            }

            Observable.zip(requests, objects -> {
                for (Object object : objects) {
                    itemmodel temp = (itemmodel) object;
                    itemData.add(temp);
                    spritelink.add(temp.getSprite().getDefaultt());
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
        } // calls the next 20

        else {
            List<Observable<itemmodel>> requests = new ArrayList<>();
            int tempp = searchsprite.size();
            for (int i = tempp; i < Math.min(tempp + 20, searchname.size()); i++) {
                requests.add(json.getitem(searchname.get(i).getUrl()));
            }
            if (requests.size() > 0) {
                temp(Searchstring, requests);
            } else {
                isloading = false;
                load.setVisibility(View.GONE);
                Snackbar.make(requireView(), "Nothing left to load", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }


    }

    void temp(String constraint, List<Observable<itemmodel>> requests) {
        Observable.zip(requests, objects -> {

            if (constraint.equals(Searchstring)) {
                for (Object object : objects) {
                    itemmodel temp = (itemmodel) object;
                    searchsprite.add(temp.getSprite().getDefaultt());
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
        getsprite();
    }

    private void searchid(String id) {
        int i = Integer.parseInt(id);
        Call<itemmodel> itemmodeid = json.getitembyid(i);
        itemmodeid.enqueue(new Callback<itemmodel>() {
            @Override
            public void onResponse(Call<itemmodel> call, Response<itemmodel> response) {
                if (!response.isSuccessful()) {
                    Snackbar.make(requireView(), "ID not found", BaseTransientBottomBar.LENGTH_LONG)
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
                    searchsprite.add(response.body().getSprite().getDefaultt());
                    searchname.add(temp);
                    adapter.change(searchname, searchsprite);
                    load.setVisibility(View.GONE);
                    isloading = false;
                }
            }

            @Override
            public void onFailure(Call<itemmodel> call, Throwable t) {
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





