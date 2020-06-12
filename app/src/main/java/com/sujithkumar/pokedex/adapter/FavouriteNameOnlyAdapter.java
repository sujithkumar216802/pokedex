package com.sujithkumar.pokedex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sujithkumar.pokedex.R;
import com.sujithkumar.pokedex.model.NameandUrl;
import com.sujithkumar.pokedex.recyclerclicklistner;

import java.util.ArrayList;

public class FavouriteNameOnlyAdapter extends  RecyclerView.Adapter<FavouriteNameOnlyAdapter.viewholder>{

    ArrayList<String> list;

    public FavouriteNameOnlyAdapter(ArrayList<String> listt) {
        list = listt;
    }

    @NonNull
    @Override
    public FavouriteNameOnlyAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.onlynametemplate, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteNameOnlyAdapter.viewholder holder, int position) {
        holder.name.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView name;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

        }
    }

}
